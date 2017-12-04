package edu.sjsu.cloudbread.response;

import java.io.Serializable;

public class GenericResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private String statusCode;
	
	public GenericResponse() {
		
	}
	public GenericResponse(String message, String statusCode) {
		super();
		this.message = message;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "GenericResponse [message=" + message + ", statusCode=" + statusCode + "]";
	}
}
