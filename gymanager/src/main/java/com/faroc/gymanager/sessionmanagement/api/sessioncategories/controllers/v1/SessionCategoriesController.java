package com.faroc.gymanager.sessionmanagement.api.sessioncategories.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.responses.AddSessionCategoriesResponse;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionCategoryRequest;
import com.faroc.gymanager.sessionmanagement.application.sessions.commands.addcategories.AddSessionCategoriesCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController("Session Categories Controller V1")
@RequestMapping("v1/sessions/session-categories")
@Tag(name = "Session Categories", description = "Requests to manage session categories available.")
public class SessionCategoriesController {
    private final Pipeline pipeline;

    @Autowired
    public SessionCategoriesController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<AddSessionCategoriesResponse> addSessionCategories(
            @RequestBody @Valid AddSessionCategoryRequest addSessionCategoryRequest) {
        var sessionCategories = addSessionCategoryRequest.sessionCategories();
        var command = new AddSessionCategoriesCommand(sessionCategories);

        var result = command.execute(pipeline);

        return new ResponseEntity<>(new AddSessionCategoriesResponse(result), HttpStatus.CREATED);
    }
}
