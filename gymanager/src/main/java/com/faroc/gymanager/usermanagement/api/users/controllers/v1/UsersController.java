package com.faroc.gymanager.usermanagement.api.users.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.usermanagement.application.admins.commands.addadmin.AddAdminCommand;
import com.faroc.gymanager.usermanagement.application.participants.commands.addparticpant.AddParticipantCommand;
import com.faroc.gymanager.usermanagement.application.trainers.commands.addtrainer.AddTrainerCommand;
import com.faroc.gymanager.usermanagement.users.responses.AdminCreatedResponse;
import com.faroc.gymanager.usermanagement.users.responses.ParticipantCreatedResponse;
import com.faroc.gymanager.usermanagement.users.responses.TrainerCreatedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Users Controller V1")
@RequestMapping("v1/users/{userId}/profiles")
@Tag(name = "Users")
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
