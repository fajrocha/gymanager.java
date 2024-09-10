package com.faroc.gymanager.usermanagement.api.users.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.common.api.contracts.responses.ValidationProblemDetail;
import com.faroc.gymanager.usermanagement.api.users.mappers.UsersRequestMappers;
import com.faroc.gymanager.usermanagement.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.usermanagement.application.users.exceptions.EmailAlreadyExistsException;
import com.faroc.gymanager.common.application.security.exceptions.PasswordComplexityException;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.LoginRequest;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.RegisterRequest;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController("Identity Controller V1")
@RequestMapping("v1/authentication")
@Tag(name = "Identity", description = "Requests to manage authentication for the user.")
public class IdentityController {
    private final Pipeline pipeline;

    @Autowired
    public IdentityController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("register")
    @Operation(
            summary = "Register user on the platform.",
            description = "Registers user to the Gymanager platform."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration response with the user JWT token.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid registration data on the request body.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationProblemDetail.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during registration.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public AuthResponse userRegistration(@Valid @RequestBody RegisterRequest request) {
        var command = UsersRequestMappers.toCommand(request);

        var result = command.execute(pipeline);

        return UsersResponseMappers.toResponse(result);
    }

    @PostMapping("login")
    @Operation(
            summary = "Login user on the platform.",
            description = "Login user to the Gymanager platform."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login response with the user JWT token.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid login data on the request body.",
                    content = {
                            @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ValidationProblemDetail.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User login fails due to not existing or wrong credentials",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during login.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public AuthResponse userLogin(@Valid @RequestBody LoginRequest request) {
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