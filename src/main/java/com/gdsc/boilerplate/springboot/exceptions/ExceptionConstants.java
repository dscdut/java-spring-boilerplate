package com.gdsc.boilerplate.springboot.exceptions;

public enum ExceptionConstants {
	INVALID_INPUT_DATA(101, "invalid_input_data"),

	INVALID_AUTHENTICATED(102, "invalid_authentication"),

	INTERNAL_SERVER_ERROR(103, "internal_server_error"),
	
    EMAIL_ALREADY_EXISTS(104, "email_already_exists");
			
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
