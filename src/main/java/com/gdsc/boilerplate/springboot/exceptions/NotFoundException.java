package com.gdsc.boilerplate.springboot.exceptions;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
