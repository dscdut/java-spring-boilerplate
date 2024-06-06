package com.gdsc.boilerplate.springboot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationExceptionResponse {

    private List<String> details;

    private int error_code;

    private String message;

}
