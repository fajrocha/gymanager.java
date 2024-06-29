package unit.domain.admins

import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.admins.errors.AdminErrors
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.subscriptions.Subscription
import spock.lang.Specification
import unit.domain.subscriptions.utils.SubscriptionsFactory

class AdminTests extends Specification {
    UUID userId
    Subscription subscription

    def setup() {
        userId = UUID.randomUUID()
        subscription = SubscriptionsFactory.create(UUID.randomUUID())
    }

    def "when unsubscribing and id given does not match should throw unexpected exception"() {
        given:
        def invalidSubscriptionId = UUID.randomUUID()
        def admin = new Admin(userId)
        admin.setSubscription(subscription)

        when:
        admin.deleteSubscription(invalidSubscriptionId)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING
    }

    def "when unsubscribing and id given matches should delete subscription"() {
        given:
        def admin = new Admin(userId)
        admin.setSubscription(subscription)

        when:
        admin.deleteSubscription(subscription.getId())

        then:
        admin.getSubscriptionId() == null
    }

    def "when subscribing and no existing subscription should add subscription"() {
        given:
        def admin = new Admin(userId)

        when:
        admin.setSubscription(subscription)

        then:
        admin.getSubscriptionId() == subscription.getId()
    }

    def "when subscribing and subscription already exists should throw conflict exception"() {
        given:
        def newSubscription = SubscriptionsFactory.create()

        def admin = new Admin(userId)
        admin.setSubscription(subscription)

        when:
        admin.setSubscription(newSubscription)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == AdminErrors.CONFLICT_SUBSCRIPTION
    }
}
