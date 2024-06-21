package unit.application.users.commands

import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.application.users.DTOs.AuthDTO
import com.faroc.gymanager.application.users.commands.loginuser.LoginCommand
import com.faroc.gymanager.application.users.commands.loginuser.LoginHandler
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException
import com.faroc.gymanager.application.users.gateways.TokenGenerator
import com.faroc.gymanager.application.users.gateways.UsersGateway
import com.faroc.gymanager.domain.users.User
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher
import net.datafaker.Faker
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class LoginHandlerTests extends Specification {

    UsersGateway mockUsersGateway
    PasswordHasher mockPasswordHasher
    TokenGenerator mockTokenGenerator
    Faker faker
    String firstName
    String lastName
    String email
    String password
    String wrongPassword
    LoginHandler sut
    String passwordHash
    String token
    LoginCommand loginCommand

    def setup() {
        faker = new Faker()
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        wrongPassword = faker.internet().password()
        passwordHash = new BCryptPasswordEncoder().encode(password)
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
        thrown(ResourceNotFoundException)
    }

    def "when password does not match should throw unauthorized exception"() {
        given:
        def loginCommand = new LoginCommand(email, wrongPassword)
        def user = User.MapFromStorage(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                passwordHash,
                null,
                null,
                null
        )

        mockUsersGateway.findByEmail(email) >> Optional.of(user)
        mockPasswordHasher.validatePassword(loginCommand.password(), passwordHash) >> false

        when:
        sut.handle(loginCommand)

        then:
        thrown(UnauthorizedException)
    }

    def "when password matches should login and generate token"() {
        given:
        def user = User.MapFromStorage(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                passwordHash,
                null,
                null,
                null
        )

        mockUsersGateway.findByEmail(email) >> Optional.of(user)
        mockPasswordHasher.validatePassword(loginCommand.password(), passwordHash) >> true
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
