package com.faroc.gymanager.api.users.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.users.mappers.UsersResponseMappers;
import com.faroc.gymanager.application.users.commands.addadmin.AddAdminCommand;
import com.faroc.gymanager.users.responses.UserAdminCreatedResponse;
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
    public UserAdminCreatedResponse createAdminProfile(@PathVariable UUID userId) {
        var command = new AddAdminCommand(userId);

        var adminId = command.execute(pipeline);

        return UsersResponseMappers.toResponse(adminId);
    }
}
