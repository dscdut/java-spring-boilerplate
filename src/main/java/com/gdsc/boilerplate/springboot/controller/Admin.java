package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.*;
import com.gdsc.boilerplate.springboot.security.dto.UpdateUserRequest;
import com.gdsc.boilerplate.springboot.security.dto.UpdateUserResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/admin")
public class Admin {

    final private UserService userService;

    final private ExceptionMessageAccessor accessor;

    final private ValidationMessageAccessor validationMessageAccessor;

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value="/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            final Long idLong = Long.parseLong(id);
            userService.deleteUserById(idLong);

        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException(validationMessageAccessor.getMessage(null, "not_found_user_id"));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable String id,@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        if (id == null || id.isEmpty() || !id.matches("\\d+")) {
            throw new InvalidSyntaxException(validationMessageAccessor.getMessage(null, "not_found_user_id"));
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
