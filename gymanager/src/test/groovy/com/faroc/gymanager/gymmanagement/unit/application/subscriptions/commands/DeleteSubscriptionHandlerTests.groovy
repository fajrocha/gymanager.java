package com.faroc.gymanager.gymmanagement.unit.application.subscriptions.commands

import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException
import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.deletesubscription.unsubscribeCommand
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.deletesubscription.DeleteSubscriptionHandler
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.admins.Admin
import com.faroc.gymanager.gymmanagement.domain.admins.errors.AdminErrors
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors
import com.faroc.gymanager.gymmanagement.unit.application.subscriptions.utils.SubscriptionsTestsFactory
import net.datafaker.Faker
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class DeleteSubscriptionHandlerTests extends Specification {
    Faker faker
    UUID subscriptionId
    UUID adminId
    Subscription subscription
    unsubscribeCommand command

    AdminsGateway mockAdminsGateway
    SubscriptionsGateway mockSubscriptionsGateway
    ApplicationEventPublisher mockAppEventPublisher
    DeleteSubscriptionHandler sut

    def setup() {
        faker = new Faker()

        subscription = SubscriptionsTestsFactory.create()
        subscriptionId = subscription.getId()
        adminId = subscription.getAdminId()
        command = new unsubscribeCommand(subscriptionId)

        mockAdminsGateway = Mock(AdminsGateway)
        mockSubscriptionsGateway = Mock(SubscriptionsGateway)
        mockAppEventPublisher = Mock(ApplicationEventPublisher)

        sut = new DeleteSubscriptionHandler(mockAdminsGateway, mockSubscriptionsGateway, mockAppEventPublisher)
    }

    def "when subscription does not exist returns not found exception"() {
        given:
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == SubscriptionErrors.NOT_FOUND
    }

    def "when admin does not exist returns not found exception"() {
        given:
        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)
        mockAdminsGateway.findById(adminId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.getDetail() == AdminErrors.NOT_FOUND
    }

    def "when called should delete subscription and call domain events"() {
        given:
        def userId = UUID.randomUUID()
        def admin = Admin.map(
                adminId,
                userId,
                subscriptionId
        )

        mockSubscriptionsGateway.findById(subscriptionId) >> Optional.of(subscription)
        mockAdminsGateway.findById(adminId) >> Optional.of(admin)

        when:
        sut.handle(command)

        then:
        admin.getSubscriptionId() == null
        1 * mockAppEventPublisher._
    }
}
