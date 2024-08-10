package com.faroc.gymanager.unit.application.subscriptions.commands

import com.faroc.gymanager.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.application.subscriptions.commands.createsubscription.CreateSubscriptionCommand
import com.faroc.gymanager.application.subscriptions.commands.createsubscription.CreateSubscriptionHandler
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.admins.errors.AdminErrors
import com.faroc.gymanager.domain.shared.events.DomainEvent
import com.faroc.gymanager.domain.subscriptions.SubscriptionType
import net.datafaker.Faker
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CreateSubscriptionHandlerTests extends Specification {
    Faker faker
    AdminsGateway mockAdminsGateway
    ApplicationEventPublisher mockEventPublisher
    UUID adminId
    CreateSubscriptionHandler sut
    UUID userId
    Admin admin
    SubscriptionType subscriptionType
    CreateSubscriptionCommand command

    def setup() {
        faker = new Faker()
        mockAdminsGateway = Mock(AdminsGateway)
        mockEventPublisher = Mock(ApplicationEventPublisher)
        subscriptionType = SubscriptionType.Free
        adminId = UUID.randomUUID()
        userId = UUID.randomUUID()
        admin = new Admin(adminId, userId)
        command = new CreateSubscriptionCommand(subscriptionType, adminId)

        sut = new CreateSubscriptionHandler(mockAdminsGateway, mockEventPublisher)
    }
    

    def "when admin does not exist show throw not found exception"() {
        given:
        mockAdminsGateway.findById(adminId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == AdminErrors.NOT_FOUND
    }

    def "when admin exists should add subscription of type"() {
        given:
        mockAdminsGateway.findById(adminId) >> Optional.of(admin)

        when:
        def subscription = sut.handle(command)

        then:
        subscription.getAdminId() == adminId
        subscription.getSubscriptionType() == subscriptionType
        1 * mockAdminsGateway.update (admin)
        1 * mockEventPublisher.publishEvent(_ as DomainEvent)
    }
}
