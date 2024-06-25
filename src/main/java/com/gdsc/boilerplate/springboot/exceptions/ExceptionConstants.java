package com.gdsc.boilerplate.springboot.exceptions;

public enum ExceptionConstants {

	INVALID_AUTHENTICATED(102, "invalid_authentication"),

	INTERNAL_SERVER_ERROR(103, "internal_server_error"),

	INVALID_SYNTAX(104, "invalid_syntax"),

	EMAIL_ALREADY_EXISTS(108, "email_already_exists"),

	USER_ID_NOT_EXISTS( 112, "not_found_user_id"),

	UNAUTHORIZED(113,"unauthorized"),

	ROLE_ID_NOT_EXISTS( 115, "not_found_role_id"),

	ADMIN_UNAUTHORIZED(114,"admin_unauthorized"),

	UNAUTHORIZED_ADMIN_DELETE_OTHER_ADMINS(113,"unauthorized_admin_delete_other_admins");


	private final int code;
	private final String messageName;

	ExceptionConstants(int code, String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public int getCode() {
		return code;
	}

	public String getMessageName() {
		return messageName;
	}
}
