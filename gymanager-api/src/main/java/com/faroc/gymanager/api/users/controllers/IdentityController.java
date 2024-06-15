package com.faroc.gymanager.api.users.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.users.mappers.UsersRequestMappers;
import com.faroc.gymanager.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.application.security.exceptions.PasswordComplexityException;
import com.faroc.gymanager.users.requests.LoginRequest;
import com.faroc.gymanager.users.requests.RegisterRequest;
import com.faroc.gymanager.users.responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class IdentityController {
    private final Pipeline pipeline;

    @Autowired
    public IdentityController(Pipeline pipeline) {

        this.pipeline = pipeline;
    }

    @PostMapping("register")
    public AuthResponse userRegistration(@RequestBody RegisterRequest request) {
        var command = UsersRequestMappers.toCommand(request);

        var result = command.execute(pipeline);

        return UsersResponseMappers.toResponse(result);
    }

    @PostMapping("login")
    public AuthResponse userLogin(@RequestBody LoginRequest request) {
        var command = UsersRequestMappers.toCommand(request);

        var result = command.execute(pipeline);

        return UsersResponseMappers.toResponse(result);
    }

    @ExceptionHandler(PasswordComplexityException.class)
    public ProblemDetail handlePasswordRegexException(PasswordComplexityException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getDetail());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
}