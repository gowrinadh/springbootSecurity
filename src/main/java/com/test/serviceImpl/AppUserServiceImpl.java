package com.test.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;

import com.test.beans.AppUserBean;
import com.test.exceptions.AuthenticationException;
import com.test.exceptions.InvalidJwtAuthenticationException;
import com.test.model.ApplicationUser;
import com.test.repo.ApplicationUserRepository;
import com.test.response.Response;
import com.test.security.SecurityConstants;
import com.test.service.AppUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AppUserServiceImpl implements AppUserService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Response registerService(ApplicationUser user) {
		try {
			applicationUserRepository.findByUsername(user.getUsername());
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			if (user != null)
				applicationUserRepository.save(user);
			return new Response(HttpStatus.OK, "", "Registration Success", "", user);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), "Registration Failed", "", null);
		}
	}

	@Override
	public Response loginService(AppUserBean loginRequest) {
		try {
			if (loginRequest.getUserName() == null || loginRequest.getPassword() == null) {
				return new Response(HttpStatus.BAD_REQUEST, "username and password not empty",
						"username and password not empty", "", null);
			} else {
				Authentication authentication = authenticate(loginRequest.getUserName(), loginRequest.getPassword());
				// SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = generateToken(authentication);
				loginRequest.setPassword("");
				loginRequest.setToken(SecurityConstants.TOKEN_PREFIX + jwt);
				return new Response(HttpStatus.OK, "successfully logged in !", "successfully logged in !", "",
						loginRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), "login to the application Failed", "", null);
		}
	}

	@Override
	public Response signoutService(HttpServletRequest request, HttpServletResponse response) {
		try {
			new SecurityContextLogoutHandler().logout(request, response, null);
			new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
					.logout(request, response, null);
			return new Response(HttpStatus.OK, "Successfully logged out !", "Successfully logged out !", "", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(HttpStatus.BAD_REQUEST, "signout not happened ", "signout not happened ", "", null);
		}
	}

	private String generateToken(Authentication auth) {
		String token = null;
		try {
			Claims claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
			claims.put("scopes", ((User) auth.getPrincipal()).getAuthorities().stream().map(s -> s.toString())
					.collect(Collectors.toList()));

			token = Jwts.builder().setClaims(claims).setIssuer("gowrinadh")
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 * @return 
	 */
	private Authentication authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials!", e);
		}
	}

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token",e);
        }
    }
	@Override
	public List<ApplicationUser> getAllUserService() {
		return applicationUserRepository.findAll();
	}

}
