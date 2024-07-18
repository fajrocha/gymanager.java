package com.faroc.gymanager.api.users.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.application.users.commands.addadmin.AddAdminCommand;
import com.faroc.gymanager.application.users.commands.addparticpant.AddParticipantCommand;
import com.faroc.gymanager.application.users.commands.addtrainer.AddTrainerCommand;
import com.faroc.gymanager.users.responses.AdminCreatedResponse;
import com.faroc.gymanager.users.responses.ParticipantCreatedResponse;
import com.faroc.gymanager.users.responses.TrainerCreatedResponse;
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
    public AdminCreatedResponse createAdminProfile(@PathVariable UUID userId) {
        var command = new AddAdminCommand(userId);

        var adminId = command.execute(pipeline);

        return new AdminCreatedResponse(adminId);
    }

    @PostMapping("/trainer")
    public TrainerCreatedResponse createTrainerProfile(@PathVariable UUID userId) {
        var command = new AddTrainerCommand(userId);

        var trainerId = command.execute(pipeline);

        return new TrainerCreatedResponse(trainerId);
    }

    @PostMapping("/participant")
    public ParticipantCreatedResponse createParticipantProfile(@PathVariable UUID userId) {
        var command = new AddParticipantCommand(userId);

        var participantId = command.execute(pipeline);

        return new ParticipantCreatedResponse(participantId);
    }
}
