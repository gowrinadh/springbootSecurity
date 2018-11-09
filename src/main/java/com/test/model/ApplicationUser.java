package com.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "app_user")
public class ApplicationUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("userName")
	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Transient
	private String authToken;

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*@JsonIgnore
	@JsonProperty(value = "user_password")*/
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}