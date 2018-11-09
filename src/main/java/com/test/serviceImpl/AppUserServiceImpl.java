package com.test.serviceImpl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.beans.AppUserBean;
import com.test.model.ApplicationUser;
import com.test.repo.ApplicationUserRepository;
import com.test.response.Response;
import com.test.security.SecurityConstants;
import com.test.service.AppUserService;

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
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
								loginRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = generateToken(authentication);
				loginRequest.setToken(jwt);
				return new Response(HttpStatus.OK, "successfully logged in !", "successfully logged in !", "", loginRequest);
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
			token = Jwts.builder().setSubject(((User) auth.getPrincipal()).getUsername())
					.claim("user", new ObjectMapper().writeValueAsString(((User) auth.getPrincipal())))
					.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

}
