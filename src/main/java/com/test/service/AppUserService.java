package com.test.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.beans.AppUserBean;
import com.test.model.ApplicationUser;
import com.test.response.Response;

public interface AppUserService {

	Response registerService(ApplicationUser user);

	Response loginService(AppUserBean loginRequest);

	Response signoutService(HttpServletRequest request, HttpServletResponse response);
	
	List<ApplicationUser> getAllUserService();
}
