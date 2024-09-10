package com.faroc.gymanager.usermanagement.api.users.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.usermanagement.application.admins.commands.addadmin.AddAdminCommand;
import com.faroc.gymanager.usermanagement.application.participants.commands.addparticpant.AddParticipantCommand;
import com.faroc.gymanager.usermanagement.application.trainers.commands.addtrainer.AddTrainerCommand;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AdminCreatedResponse;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.ParticipantCreatedResponse;
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.TrainerCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Users Controller V1")
@RequestMapping("v1/users/{userId}/profiles")
@Tag(name = "Users", description = "Requests to manage user related attributes like profiles.")
public class UsersController {
    private final Pipeline pipeline;

    @Autowired
    public UsersController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/admin")
    @Operation(
            summary = "Add admin profile to user.",
            description = "Adds an admin profile to user which requests it, giving him admin privileges."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile added response, which includes the admin id.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdminCreatedResponse.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User on the request token is not the same as the user requesting to add the profile",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found, for example when the user does not exist.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during adding profile request.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public AdminCreatedResponse createAdminProfile(
            @Parameter(description = "User id to add admin profile") @PathVariable UUID userId) {
        var command = new AddAdminCommand(userId);

        var adminId = command.execute(pipeline);

        return new AdminCreatedResponse(adminId);
    }

    @PostMapping("/trainer")
    @Operation(
            summary = "Add trainer profile to user.",
            description = "Adds a trainer profile to user which requests it, giving him trainer privileges."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile added response, which includes the trainer id.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TrainerCreatedResponse.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User on the request token is not the same as the user requesting to add the profile",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found, for example when the user does not exist.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during adding profile request.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public TrainerCreatedResponse createTrainerProfile(
            @Parameter(description = "User id to add trainer profile") @PathVariable UUID userId) {
        var command = new AddTrainerCommand(userId);

        var trainerId = command.execute(pipeline);

        return new TrainerCreatedResponse(trainerId);
    }

    @PostMapping("/participant")
    @Operation(
            summary = "Add participant profile to user.",
            description = "Adds a participant profile to user which requests it, giving him participant privileges."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile added response, which includes the participant id.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ParticipantCreatedResponse.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User on the request token is not the same as the user requesting to add the profile",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found, for example when the user does not exist.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during adding profile request.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public ParticipantCreatedResponse createParticipantProfile(
            @Parameter(description = "User id to add participant profile") @PathVariable UUID userId) {
        var command = new AddParticipantCommand(userId);

        var participantId = command.execute(pipeline);

        return new ParticipantCreatedResponse(participantId);
    }
}
