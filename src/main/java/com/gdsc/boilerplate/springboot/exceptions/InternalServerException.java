package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class InternalServerException extends RuntimeException {

	private static final long serialVersionUID = -108187574408704575L;

}
