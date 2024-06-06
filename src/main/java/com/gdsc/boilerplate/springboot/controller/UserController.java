package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.InvalidSyntaxRegistrationException;
import com.gdsc.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @SneakyThrows
    @DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        BindingResult bindingResult = new DataBinder(id).getBindingResult();
        try {
            final Long idLong = Long.parseLong(id);
            userService.deleteUserById(idLong);

        } catch (NumberFormatException e) {
            throw new InvalidSyntaxRegistrationException();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
