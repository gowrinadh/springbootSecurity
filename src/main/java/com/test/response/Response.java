package com.test.response;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;

public class Response {

	private long timestamp;
	private HttpStatus status;
	private String error;
	private String message;
	private String path;
	private Object resultObject;

	public Response(HttpStatus status, String error, String message, String path, Object resultObject) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.resultObject = resultObject;
		this.timestamp = new Timestamp(new Date().getTime()).getTime();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	@Override
	public String toString() {
		return "Response [timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message=" + message
				+ ", path=" + path + ", resultObject=" + resultObject + "]";
	}

	
}
