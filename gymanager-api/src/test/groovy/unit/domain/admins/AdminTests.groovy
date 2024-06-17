package unit.domain.admins

import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.admins.errors.AdminErrors
import com.faroc.gymanager.domain.admins.exceptions.SubscriptionIdNotMatchingException
import spock.lang.Specification

class AdminTests extends Specification {

    def "when deleting subscription and id given does not match should throw not matching exception"() {
        given:
        var userId = UUID.randomUUID()
        var subscriptionId = UUID.randomUUID()
        var invalidSubscriptionId = UUID.randomUUID()

        var admin = new Admin(userId)
        admin.setSubscription(subscriptionId)

        when:
        admin.deleteSubscription(invalidSubscriptionId)

        then:
        var ex = thrown(SubscriptionIdNotMatchingException)
        ex.getDetail() == AdminErrors.SUBSCRIPTION_ID_NOT_MATCHING
    }

    def "when deleting subscription and id given matches should delete subscription"() {
        given:
        var userId = UUID.randomUUID()
        var subscriptionId = UUID.randomUUID()

        var admin = new Admin(userId)
        admin.setSubscription(subscriptionId)

        when:
        admin.deleteSubscription(subscriptionId)

        then:
        admin.getSubscriptionId() == null
    }
}
