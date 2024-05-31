package com.gdsc.boilerplate.springboot.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 3548751235479950216L;
	private final int code;
	private final String errorMessage;

}
