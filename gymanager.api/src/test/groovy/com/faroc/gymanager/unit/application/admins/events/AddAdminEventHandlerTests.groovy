package com.faroc.gymanager.unit.application.admins.events

import com.faroc.gymanager.application.admins.events.AddAdminEventHandler
import com.faroc.gymanager.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException
import com.faroc.gymanager.domain.users.events.AddAdminEvent
import spock.lang.Specification

class AddAdminEventHandlerTests extends Specification {

    UUID userId
    UUID adminId
    AddAdminEvent event
    AddAdminEventHandler sut
    AdminsGateway mockAdminsGateway

    def setup() {
        userId = UUID.randomUUID()
        adminId = UUID.randomUUID()
        event = new AddAdminEvent(adminId, userId)
        
        mockAdminsGateway = Mock(AdminsGateway)

        sut = new AddAdminEventHandler(mockAdminsGateway)
    }

    def "when add admin event is triggered should create admin"() {
        when:
        sut.handle(event)

        then:
        1 * mockAdminsGateway.create(_ as Admin)
    }

    def "when add admin event is triggered and creating admin fails should throw eventual consistency exception"() {
        given:
        mockAdminsGateway.create(_ as Admin) >> { throw new RuntimeException() }

        when:
        sut.handle(event)

        then:
        thrown(EventualConsistencyException)
    }
}
