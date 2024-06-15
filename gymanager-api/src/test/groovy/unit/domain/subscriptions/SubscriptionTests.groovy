package unit.domain.subscriptions

import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.subscriptions.Subscription
import com.faroc.gymanager.domain.subscriptions.SubscriptionErrors
import com.faroc.gymanager.domain.subscriptions.SubscriptionType
import net.datafaker.Faker
import spock.lang.Specification

class SubscriptionTests extends Specification {

    Faker faker
    UUID adminId
    UUID gymId
    Subscription subscription

    def setup() {
        faker = new Faker()
        adminId = UUID.randomUUID()
        gymId = UUID.randomUUID()
        subscription = new Subscription(
                adminId,
                SubscriptionType.Free
        )
    }

    def "when subscription is free should have corresponding max gyms"() {
        given:
        var subscription = new Subscription(
                adminId,
                SubscriptionType.Free
        )

        expect:
        subscription.getMaxGyms() == Subscription.MAX_GYMS_FREE
    }

    def "when subscription is starter should have corresponding max gyms"() {
        given:
        var subscription = new Subscription(
                adminId,
                SubscriptionType.Starter
        )

        expect:
        subscription.getMaxGyms() == Subscription.MAX_GYMS_STARTER
    }

    def "when subscription is pro should have corresponding max gyms"() {
        given:
        var subscription = new Subscription(
                adminId,
                SubscriptionType.Pro
        )

        expect:
        subscription.getMaxGyms() == Subscription.MAX_GYMS_PRO
    }

    def "when adding a gym to subscription should add the gym"() {
        given:
        subscription.addGym(gymId)

        expect:
        subscription.hasGym(gymId)
    }

    def "when adding a gym that already exists on subscription should throw conflict exception"() {
        given:
        subscription.addGym(gymId)

        when:
        subscription.addGym(gymId)

        then:
        var ex = thrown(ConflictException)
        ex.getDetail() == SubscriptionErrors.CONFLICT_GYM
    }

    def "when removing gym from subscription should remove gym"() {
        given:
        subscription.addGym(gymId)
        subscription.removeGym(gymId)

        expect:
        !subscription.hasGym(gymId)
    }
}
