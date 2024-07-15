package com.faroc.gymanager.api.users.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.application.users.commands.addadmin.AddAdminCommand;
import com.faroc.gymanager.application.users.commands.addparticpant.AddParticipantCommand;
import com.faroc.gymanager.application.users.commands.addtrainer.AddTrainerCommand;
import com.faroc.gymanager.users.responses.ProfileCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/profiles")
public class UsersController {
    private final Pipeline pipeline;

    @Autowired
    public UsersController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/admin")
    public ProfileCreatedResponse createAdminProfile(@PathVariable UUID userId) {
        var command = new AddAdminCommand(userId);

        var adminId = command.execute(pipeline);

        return UsersResponseMappers.toResponse(adminId);
    }

    @PostMapping("/trainer")
    public ProfileCreatedResponse createTrainerProfile(@PathVariable UUID userId) {
        var command = new AddTrainerCommand(userId);

        var trainerId = command.execute(pipeline);

        return UsersResponseMappers.toResponse(trainerId);
    }

    @PostMapping("/participant")
    public ProfileCreatedResponse createParticipantProfile(@PathVariable UUID userId) {
        var command = new AddParticipantCommand(userId);

        var participantId = command.execute(pipeline);

        return UsersResponseMappers.toResponse(participantId);
    }
}
