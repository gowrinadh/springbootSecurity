package com.test.beans;

import java.io.Serializable;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

public class AppUserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private String userName;

	@NotNull
	private String password;

	@Transient
	private String token;

	public AppUserBean() {
		super();
	}

	public AppUserBean(@NotNull String userName, @NotNull String password, String token) {
		super();
		this.userName = userName;
		this.password = password;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "AppUserBean [userName=" + userName + ", password=" + password + ", token=" + token + "]";
	}

}
