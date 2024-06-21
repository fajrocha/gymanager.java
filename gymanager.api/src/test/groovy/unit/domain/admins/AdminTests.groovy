package unit.domain.admins

import com.faroc.gymanager.application.shared.exceptions.UnexpectedException
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.admins.errors.AdminErrors
import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import spock.lang.Specification

class AdminTests extends Specification {
    UUID userId
    UUID subscriptionId

    def setup() {
        userId = UUID.randomUUID()
        subscriptionId = UUID.randomUUID()
    }

    def "when deleting subscription and id given does not match should throw unexpected exception"() {
        given:
        def invalidSubscriptionId = UUID.randomUUID()
        def admin = new Admin(userId)
        admin.setSubscription(subscriptionId)

        when:
        admin.deleteSubscription(invalidSubscriptionId)

        then:
        def ex = thrown(UnexpectedException)
        ex.getDetail() == AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING
    }

    def "when deleting subscription and id given matches should delete subscription"() {
        given:
        def admin = new Admin(userId)
        admin.setSubscription(subscriptionId)

        when:
        admin.deleteSubscription(subscriptionId)

        then:
        admin.getSubscriptionId() == null
    }

    def "when adding subscription and no existing subscription should add subscription"() {
        given:
        def admin = new Admin(userId)

        when:
        admin.setSubscription(subscriptionId)

        then:
        admin.getSubscriptionId() == subscriptionId
    }

    def "when adding subscription and subscription already exists should throw conflict exception"() {
        given:
        var newSubscriptionId = UUID.randomUUID()

        var admin = new Admin(userId)
        admin.setSubscription(subscriptionId)

        when:
        admin.setSubscription(newSubscriptionId)

        then:
        def ex = thrown(ConflictException)
        ex.getDetail() == AdminErrors.CONFLICT_SUBSCRIPTION
    }
}
