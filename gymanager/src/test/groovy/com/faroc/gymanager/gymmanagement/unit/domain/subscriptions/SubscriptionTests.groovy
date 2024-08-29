package com.faroc.gymanager.gymmanagement.unit.domain.subscriptions

import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors
import com.faroc.gymanager.gymmanagement.unit.domain.subscriptions.utils.SubscriptionsFactory
import spock.lang.Specification

class SubscriptionTests extends Specification {
    UUID adminId
    UUID gymId
    Subscription subscription

    def setup() {
        adminId = UUID.randomUUID()
        gymId = UUID.randomUUID()
        subscription = SubscriptionsFactory.create()
    }

    def "when subscription is free should have corresponding max gyms"() {
        given:
        def subscription = SubscriptionsFactory.create(SubscriptionType.Free)

        expect:
        subscription.getMaxGyms() == Subscription.MAX_GYMS_FREE
    }

    def "when subscription is starter should have corresponding max gyms"() {
        given:
        def subscription = SubscriptionsFactory.create(SubscriptionType.Starter)

        expect:
        subscription.getMaxGyms() == Subscription.MAX_GYMS_STARTER
    }

    def "when subscription is pro should have corresponding max gyms"() {
        given:
        def subscription = SubscriptionsFactory.create(SubscriptionType.Pro)

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
        def ex = thrown(ConflictException)
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
