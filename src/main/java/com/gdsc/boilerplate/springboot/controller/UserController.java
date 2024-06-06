package com.gdsc.boilerplate.springboot.controller;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdsc.boilerplate.springboot.configuration.dto.PageDto;
import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

	final private UserService userService;

	@GetMapping
	public ResponseEntity<PageDto<UserDto>> filterUser(
			@Positive(message = "{greater_zero}") 
			@RequestParam(name = "page_size", required = false, defaultValue = DefaultConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
			
			@Positive(message = "{greater_zero}") 
			@RequestParam(name = "page", required = false, defaultValue = DefaultConstants.DEFAULT_PAGE) Integer page) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(userService.getPage(pageable));
	}
}
