package org.mackenzine.notifications.model;

public class Customer {
	private String userId;
	private String password;
	private String email;

	public Customer(final String userId, final String password, final String email) {
		this.userId = userId;
		this.password = password;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
