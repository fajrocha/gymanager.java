package com.faroc.gymanager.unit.domain.users

import com.faroc.gymanager.common.domain.exceptions.ConflictException
import com.faroc.gymanager.usermanagement.domain.users.User
import com.faroc.gymanager.usermanagement.domain.users.UserProfileTypes
import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors
import com.faroc.gymanager.unit.domain.users.utils.UsersTestsFactory
import net.datafaker.Faker
import spock.lang.Specification

class UsersTests extends Specification {

    Faker faker
    String password
    String wrongPassword
    User user

    PasswordHasher mockPasswordHasher

    def setup() {
        faker = new Faker()
        password = faker.internet().password()
        wrongPassword = faker.internet().password()
        user = UsersTestsFactory.create(password)

        mockPasswordHasher = Mock(PasswordHasher)

    }

    def "when creating admin profile should add profile"() {
        when:
        user.createAdminProfile()

        then:
        user.getAdminId() != null
    }

    def "when admin profile already exists should throw conflict exception"() {
        when:
        user.createAdminProfile()
        user.createAdminProfile()

        then:
        var ex = thrown(ConflictException)
        ex.detail == UserErrors.CONFLICT_ADMIN_PROFILE
    }

    def "when password is valid should return true"() {
        mockPasswordHasher.validatePassword(password, user.getPasswordHash()) >> true

        when:
        def validResult = user.validatePassword(password, mockPasswordHasher)

        then:
        validResult
    }

    def "when password is invalid should return false"() {
        given:
        mockPasswordHasher.validatePassword(wrongPassword, user.getPasswordHash()) >> false

        when:
        def validResult = user.validatePassword(password, mockPasswordHasher)

        then:
        !validResult
    }

    def "when user has profiles should return them"() {
        given:
        user.createAdminProfile()
        def expectedProfiles = List.of(UserProfileTypes.ADMIN)

        when:
        def actualProfiles = user.getUserProfiles()

        then:
        actualProfiles == expectedProfiles
    }
}
