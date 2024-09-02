package com.faroc.gymanager.usermanagement.api.users.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.usermanagement.api.users.mappers.UsersRequestMappers;
import com.faroc.gymanager.usermanagement.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.usermanagement.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.common.application.security.exceptions.PasswordComplexityException;
import com.faroc.gymanager.usermanagement.users.requests.LoginRequest;
import com.faroc.gymanager.usermanagement.users.requests.RegisterRequest;
import com.faroc.gymanager.usermanagement.users.responses.AuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController("Identity Controller V1")
@RequestMapping("v1/authentication")
@Tag(name = "Identity")
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