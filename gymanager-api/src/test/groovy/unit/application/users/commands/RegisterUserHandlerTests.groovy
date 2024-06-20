package unit.application.users.commands

import com.faroc.gymanager.application.users.DTOs.AuthDTO
import com.faroc.gymanager.application.users.commands.registeruser.RegisterUserCommand
import com.faroc.gymanager.application.users.commands.registeruser.RegisterUserHandler
import com.faroc.gymanager.application.users.exceptions.EmailAlreadyExistsException
import com.faroc.gymanager.application.users.gateways.TokenGenerator
import com.faroc.gymanager.application.users.gateways.UsersGateway
import com.faroc.gymanager.domain.users.User
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher
import net.datafaker.Faker
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class RegisterUserHandlerTests extends Specification {

    String firstName
    String lastName
    String email
    String password
    String passwordHash

    UsersGateway mockUsersGateway
    PasswordHasher mockPasswordHasher
    TokenGenerator mockTokenGenerator
    RegisterUserHandler sut
    Faker faker
    String token

    def setup() {
        faker = new Faker()
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        passwordHash = new BCryptPasswordEncoder().encode(password)
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0Ijox" +
                "NTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        mockUsersGateway = Mock(UsersGateway)
        mockPasswordHasher = Mock(PasswordHasher)
        mockTokenGenerator = Mock(TokenGenerator)

        sut = new RegisterUserHandler(
                mockUsersGateway,
                mockPasswordHasher,
                mockTokenGenerator
        )
    }

    def "when registering user and email already exists should throw exception "() {
        given:
        def registerUserCommand = new RegisterUserCommand(
                firstName,
                lastName,
                email,
                password
        )

        mockUsersGateway.emailExists(registerUserCommand.email()) >> true

        when:
        sut.handle(registerUserCommand)

        then:
        thrown(EmailAlreadyExistsException)
    }

    def "when email given is not in use should save user"() {
        given:
        def registerUserCommand = new RegisterUserCommand(
                firstName,
                lastName,
                email,
                password
        )

        mockUsersGateway.emailExists(registerUserCommand.email()) >> false
        mockPasswordHasher.hashPassword(password) >> passwordHash
        mockTokenGenerator.generate(_ as User) >> token

        def expectedAuthDTO = new AuthDTO(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                token
        )

        when:
        def actualAuthDto = sut.handle(registerUserCommand)

        then:
        1 * mockUsersGateway.save(_ as User)
        actualAuthDto.firstName() == expectedAuthDTO.firstName()
        actualAuthDto.lastName() == expectedAuthDTO.lastName()
        actualAuthDto.email() == expectedAuthDTO.email()
        actualAuthDto.token() == expectedAuthDTO.token()
    }

    def "when generating password hash throws exception should rethrow exception"() {
        given:
        def registerUserCommand = new RegisterUserCommand(
                firstName,
                lastName,
                email,
                password
        )

        mockUsersGateway.emailExists(registerUserCommand.email()) >> false
        mockPasswordHasher.hashPassword(password) >> { throw new RuntimeException("Exception on saving user") }

        when:
        sut.handle(registerUserCommand)

        then:
        thrown(RuntimeException)
    }

    def "when saving user throws exception should rethrow exception "() {
        given:
        def registerUserCommand = new RegisterUserCommand(
                firstName,
                lastName,
                email,
                password
        )

        mockUsersGateway.emailExists(registerUserCommand.email()) >> false
        mockPasswordHasher.hashPassword(password) >> passwordHash
        mockUsersGateway.save(_ as User) >> { throw new RuntimeException("Exception on saving user") }

        when:
        sut.handle(registerUserCommand)

        then:
        thrown(RuntimeException)
    }

    def "when generating token throws exception should rethrow exception"() {
        given:
        def registerUserCommand = new RegisterUserCommand(
                firstName,
                lastName,
                email,
                password
        )

        mockUsersGateway.emailExists(registerUserCommand.email()) >> false
        mockPasswordHasher.hashPassword(password) >> passwordHash
        mockTokenGenerator.generate(_ as User) >> { throw new RuntimeException("Exception on generating token") }

        when:
        sut.handle(registerUserCommand)

        then:
        thrown(RuntimeException)
    }
}
