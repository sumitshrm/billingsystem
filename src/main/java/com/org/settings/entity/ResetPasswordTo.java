package com.org.settings.entity;

import com.org.domain.LogUser;

public class ResetPasswordTo {

	private String currentPassword;
	
	private String newPassword;
	
	private String confirmPassword;
	
	public ResetPasswordTo() {
		super();
	}
	
	public ResetPasswordTo(String currentPassword) {
		super();
		this.currentPassword = currentPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "ResetPasswordTo [currentPassword=" + currentPassword
				+ ", newPassword=" + newPassword + ", confirmPassword="
				+ confirmPassword + "]";
	}
	
	
}
