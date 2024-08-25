package com.faroc.gymanager.unit.application.subscriptions.commands

import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.createsubscription.SubscribeCommand
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.createsubscription.SubscribeHandler
import com.faroc.gymanager.gymmanagement.domain.admins.Admin
import com.faroc.gymanager.gymmanagement.domain.admins.errors.AdminErrors
import com.faroc.gymanager.common.domain.events.DomainEvent
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType
import net.datafaker.Faker
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class SubscribeHandlerTests extends Specification {
    Faker faker
    AdminsGateway mockAdminsGateway
    ApplicationEventPublisher mockEventPublisher
    UUID adminId
    SubscribeHandler sut
    UUID userId
    Admin admin
    SubscriptionType subscriptionType
    SubscribeCommand command

    def setup() {
        faker = new Faker()
        mockAdminsGateway = Mock(AdminsGateway)
        mockEventPublisher = Mock(ApplicationEventPublisher)
        subscriptionType = SubscriptionType.Free
        adminId = UUID.randomUUID()
        userId = UUID.randomUUID()
        admin = new Admin(adminId, userId)
        command = new SubscribeCommand(subscriptionType, adminId)

        sut = new SubscribeHandler(mockAdminsGateway, mockEventPublisher)
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
