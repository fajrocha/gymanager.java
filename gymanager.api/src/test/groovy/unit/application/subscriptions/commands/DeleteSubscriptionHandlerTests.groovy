package unit.application.subscriptions.commands

import an.awesome.pipelinr.Pipeline
import com.faroc.gymanager.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.application.subscriptions.commands.deletesubscription.DeleteSubscriptionCommand
import com.faroc.gymanager.application.subscriptions.commands.deletesubscription.DeleteSubscriptionHandler
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.admins.errors.AdminErrors
import com.faroc.gymanager.domain.subscriptions.Subscription
import com.faroc.gymanager.domain.subscriptions.SubscriptionType
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors
import net.datafaker.Faker
import spock.lang.Specification

class DeleteSubscriptionHandlerTests extends Specification {
    Faker faker
    AdminsGateway mockAdminsGateway
    SubscriptionsGateway mockSubscriptionsGateway
    Pipeline mockPipeline
    UUID subscriptionId
    UUID adminId
    SubscriptionType subscriptionType
    DeleteSubscriptionCommand command
    Subscription subscription

    DeleteSubscriptionHandler sut

    def setup() {
        faker = new Faker()
        mockAdminsGateway = Mock(AdminsGateway)
        mockSubscriptionsGateway = Mock(SubscriptionsGateway)
        mockPipeline = Mock(Pipeline)
        subscriptionId = UUID.randomUUID()
        adminId = UUID.randomUUID()
        subscriptionType = SubscriptionType.Free
        command = new DeleteSubscriptionCommand(subscriptionId)

        subscription = Subscription.mapFromStorage(
                subscriptionId,
                adminId,
                subscriptionType,
                1,
                new UUID[0]
        )

        sut = new DeleteSubscriptionHandler(mockPipeline, mockAdminsGateway, mockSubscriptionsGateway)
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
        def admin = Admin.mapFromStorage(
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
        1 * mockPipeline._
    }
}
