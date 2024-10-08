package com.faroc.gymanager.usermanagement.unit.application.commands

import com.faroc.gymanager.common.application.security.exceptions.UnauthorizedException
import com.faroc.gymanager.usermanagement.application.users.dtos.AuthDTO
import com.faroc.gymanager.usermanagement.application.users.commands.loginuser.LoginCommand
import com.faroc.gymanager.usermanagement.application.users.commands.loginuser.LoginHandler
import com.faroc.gymanager.usermanagement.application.users.gateways.TokenGenerator
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway
import com.faroc.gymanager.usermanagement.domain.users.User
import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher
import net.datafaker.Faker
import spock.lang.Specification
import com.faroc.gymanager.usermanagement.unit.domain.users.utils.UsersTestsFactory

class LoginHandlerTests extends Specification {

    Faker faker
    String password
    String wrongPassword
    LoginHandler sut
    String token
    LoginCommand loginCommand
    User user
    String email

    UsersGateway mockUsersGateway
    PasswordHasher mockPasswordHasher
    TokenGenerator mockTokenGenerator

    def setup() {
        faker = new Faker()
        password = faker.internet().password()
        wrongPassword = faker.internet().password()

        user = UsersTestsFactory.create(password)

        email = user.getEmail()
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0Ijox" +
                "NTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        mockUsersGateway = Mock(UsersGateway)
        mockPasswordHasher = Mock(PasswordHasher)
        mockTokenGenerator = Mock(TokenGenerator)

        sut = new LoginHandler(
                mockUsersGateway,
                mockPasswordHasher,
                mockTokenGenerator
        )

        loginCommand = new LoginCommand(email, password)
    }

    def "when email does not exist should throw not found exception"() {
        given:
        mockUsersGateway.findByEmail(email) >> Optional.empty()

        when:
        sut.handle(loginCommand)

        then:
        thrown(UnauthorizedException)
    }

    def "when password does not match should throw unauthorized exception"() {
        given:
        def loginCommand = new LoginCommand(email, wrongPassword)

        mockUsersGateway.findByEmail(email) >> Optional.of(user)
        mockPasswordHasher.validatePassword(loginCommand.password(), user.getPasswordHash()) >> false

        when:
        sut.handle(loginCommand)

        then:
        thrown(UnauthorizedException)
    }

    def "when password matches should login and generate token"() {
        given:
        mockUsersGateway.findByEmail(email) >> Optional.of(user)
        mockPasswordHasher.validatePassword(loginCommand.password(), user.getPasswordHash()) >> true
        mockTokenGenerator.generate(user) >> token

        def expectedResult = new AuthDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                token
        )

        when:
        def actualResult = sut.handle(loginCommand)

        then:
        actualResult == expectedResult
    }
}
