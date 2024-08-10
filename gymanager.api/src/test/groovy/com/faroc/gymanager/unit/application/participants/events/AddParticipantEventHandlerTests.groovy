package com.faroc.gymanager.unit.application.participants.events


import com.faroc.gymanager.application.participants.events.AddParticipantEventHandler
import com.faroc.gymanager.application.participants.gateways.ParticipantsGateway
import com.faroc.gymanager.domain.participants.Participant
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.domain.users.events.AddParticipantEvent
import spock.lang.Specification

class AddParticipantEventHandlerTests extends Specification {

    UUID userId
    UUID participantId
    AddParticipantEvent event
    AddParticipantEventHandler sut
    ParticipantsGateway mockParticipantsGateway

    def setup() {
        userId = UUID.randomUUID()
        participantId = UUID.randomUUID()
        event = new AddParticipantEvent(participantId, userId)
        
        mockParticipantsGateway = Mock(ParticipantsGateway)

        sut = new AddParticipantEventHandler(mockParticipantsGateway)
    }

    def "when event is triggered should create participant"() {
        when:
        sut.handle(event)

        then:
        1 * mockParticipantsGateway.create(_ as Participant)
    }

    def "when event is triggered and creating participant fails should throw eventual consistency exception"() {
        given:
        mockParticipantsGateway.create(_ as Participant) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
