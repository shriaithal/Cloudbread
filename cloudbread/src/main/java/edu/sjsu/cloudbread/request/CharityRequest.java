package edu.sjsu.cloudbread.request;

import java.io.Serializable;

public class CharityRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String requestId;
	private String status;
	private String userName;

	public CharityRequest() {

	}

	public CharityRequest(String requestId, String status) {

		super();

		this.requestId = requestId;

		this.status = status;

	}

	public String getRequestId() {

		return requestId;

	}

	public void setRequestId(String requestId) {

		this.requestId = requestId;

	}

	public String getStatus() {

		return status;

	}

	public void setStatus(String status) {

		this.status = status;

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
