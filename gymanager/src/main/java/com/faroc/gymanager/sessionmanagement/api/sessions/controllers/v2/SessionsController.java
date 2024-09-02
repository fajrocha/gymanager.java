package com.faroc.gymanager.sessionmanagement.api.sessions.controllers.v2;

import com.faroc.gymanager.sessionmanagement.sessions.requests.AddSessionRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Sessions Controller V2")
@RequestMapping("v2/rooms/{roomId}/sessions")
@Tag(name = "Sessions")
public class SessionsController {

    public SessionsController() {
    }

    @PostMapping
    public String addSession(@PathVariable UUID roomId, @RequestBody AddSessionRequest addSessionRequest) {
        return "No implementation";
    }
}
