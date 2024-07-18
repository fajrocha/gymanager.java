package com.faroc.gymanager.api.sessions;

import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.sessions.requests.AddSessionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/rooms/{roomId}/sessions")
public class SessionsController {

    @GetMapping
    public AddSessionRequest addSession(@RequestBody AddSessionRequest addSessionRequest) {
        return addSessionRequest;
    }
}
