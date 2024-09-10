package com.faroc.gymanager.sessionmanagement.api.sessions.controllers.v2;

import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Add session to a room.",
            description = "Trainer adds a session to a given room."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Just here as a skeleton for future versions of controllers, always returns same response."
            )
    })
    public String addSession(@PathVariable UUID roomId, @RequestBody AddSessionRequest addSessionRequest) {
        return "No implementation";
    }
}
