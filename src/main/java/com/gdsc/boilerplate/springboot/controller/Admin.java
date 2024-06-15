package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.*;
import com.gdsc.boilerplate.springboot.dto.UpdateUserRequest;
import com.gdsc.boilerplate.springboot.dto.UpdateUserResponse;
import com.gdsc.boilerplate.springboot.service.UserService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import com.gdsc.boilerplate.springboot.utils.ValidationMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/admin")
public class Admin {

    final private UserService userService;

    final private ExceptionMessageAccessor accessor;

    final private ValidationMessageAccessor validationMessageAccessor;

    @DeleteMapping(value="/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable @Pattern(regexp = "0|[1-9]\\d*", message = "{user_id_invalid}") String id)  {

        final Long idLong = Long.parseLong(id);
        userService.deleteUserById(idLong);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable @Pattern(regexp = "0|[1-9]\\d*", message = "{user_id_invalid}") String id,
                                                         @Valid @RequestBody UpdateUserRequest updateUserRequest,  BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("id")) {
            bindingResult.addError(new ObjectError("id", validationMessageAccessor.getMessage(null, "user_id_invalid")));
        }

        final Long idLong = Long.parseLong(id);
        final UpdateUserResponse updateUserResponse = userService.updateUser(idLong, updateUserRequest);

        return ResponseEntity.ok(updateUserResponse);

    }

    @ExceptionHandler(UserIdNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ApiExceptionResponse> handleUserIdNotExistsException(UserIdNotExistsException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.USER_ID_NOT_EXISTS.getCode(),
                accessor.getMessage(null, ExceptionConstants.USER_ID_NOT_EXISTS.getMessageName()));
        log.warn("UserIdNotExistsException: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RoleIdNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ApiExceptionResponse> handleRoleIdNotExistsException(RoleIdNotExistsException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.ROLE_ID_NOT_EXISTS.getCode(),
                accessor.getMessage(null, ExceptionConstants.ROLE_ID_NOT_EXISTS.getMessageName()));
        log.warn("UserIdNotExistsException: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<ApiExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.ADMIN_UNAUTHORIZED.getCode(),
                accessor.getMessage(null, ExceptionConstants.ADMIN_UNAUTHORIZED.getMessageName()));
        log.warn("AccessDeniedException: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
