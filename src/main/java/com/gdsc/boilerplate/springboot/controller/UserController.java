package com.gdsc.boilerplate.springboot.controller;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.gdsc.boilerplate.springboot.configuration.dto.PageDto;
import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.service.UserService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.gdsc.boilerplate.springboot.exceptions.ApiExceptionResponse;
import com.gdsc.boilerplate.springboot.exceptions.ExceptionConstants;
import com.gdsc.boilerplate.springboot.exceptions.UserIdNotExistsException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {

	final private UserService userService;

	final private ExceptionMessageAccessor accessor;

	@GetMapping
	public ResponseEntity<PageDto<UserDto>> filterUser(
			@Positive(message = "{greater_zero}") @RequestParam(name = "page_size", required = false, defaultValue = DefaultConstants.DEFAULT_PAGE_SIZE) Integer pageSize,

			@Positive(message = "{greater_zero}") @RequestParam(name = "page", required = false, defaultValue = DefaultConstants.DEFAULT_PAGE) Integer page) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(userService.getPage(pageable));
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteUser(@PathVariable @Positive Long id) {

		userService.deleteUserById(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete succesfully!");
	}

	@ExceptionHandler(UserIdNotExistsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<ApiExceptionResponse> handleUserIdNotExistsException(UserIdNotExistsException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.USER_ID_NOT_EXISTS.getCode(),
				accessor.getMessage(null, ExceptionConstants.USER_ID_NOT_EXISTS.getMessageName()));
		log.warn("UserIdNotExistsException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}
