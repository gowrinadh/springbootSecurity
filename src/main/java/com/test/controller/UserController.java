package com.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.beans.AppUserBean;
import com.test.model.ApplicationUser;
import com.test.response.Response;
import com.test.service.AppUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AppUserService appUserService;

	/*
	 * Sign-up
	 */
	@PostMapping("/auth/sign-up")
	public Response signUp(@RequestBody ApplicationUser user) {
		return appUserService.registerService(user);
	}

	/*
	 * Login
	 */
	@PostMapping("/auth/login")
	public Response login(@Valid @RequestBody AppUserBean loginRequest) {
		System.out.println(loginRequest.toString());
		return appUserService.loginService(loginRequest);
	}

	/*
	 * Signout/Logout
	 */
	@GetMapping("/signout")
	public Response signOut(HttpServletRequest request, HttpServletResponse response) {
		// clearing the Cookies and invalidation the session ( logout )
		return appUserService.signoutService(request, response);
	}
}