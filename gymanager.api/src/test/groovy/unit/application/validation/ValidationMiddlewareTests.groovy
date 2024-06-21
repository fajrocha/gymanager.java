package unit.application.validation

import an.awesome.pipelinr.Command
import com.faroc.gymanager.application.shared.exceptions.ValidationException
import com.faroc.gymanager.application.users.DTOs.AuthDTO
import com.faroc.gymanager.application.users.commands.loginuser.LoginCommand
import com.faroc.gymanager.application.users.commands.loginuser.LoginUserValidator
import com.faroc.gymanager.application.validation.ValidationMiddleware
import com.faroc.gymanager.application.validation.ValidatorsAggregator
import net.datafaker.Faker
import spock.lang.Specification

class ValidationMiddlewareTests extends Specification {
    Faker faker
    UUID userId
    String firstName
    String lastName
    String email
    String password
    String token
    LoginCommand command

    ValidatorsAggregator validatorsAggregator
    Command.Middleware.Next mockNext
    ValidationMiddleware sut

    def setup() {
        faker = new Faker()
        userId = UUID.randomUUID()
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0Ijox" +
                "NTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        command = new LoginCommand(email, password)

        validatorsAggregator = new ValidatorsAggregator()
        mockNext = Mock(Command.Middleware.Next)

        sut = new ValidationMiddleware(validatorsAggregator)
    }

    def "when there is no validator should return result from next"() {
        given:
        def authDto = new AuthDTO(userId, firstName, lastName, email, token)
        mockNext.invoke() >> authDto

        when:
        def result = sut.invoke(command, mockNext)

        then:
        result == authDto
    }

    def "when there is validator and validation succeeds should return result from next"() {
        given:
        validatorsAggregator.getValidatorHashMap().put(command.getClass(), new LoginUserValidator())

        def authDto = new AuthDTO(userId, firstName, lastName, email, token);
        mockNext.invoke() >> authDto

        when:
        def result = sut.invoke(command, mockNext)

        then:
        result == authDto
    }

    def "when there is validator and validation fails should throw exception with "() {
        given:
        def command = new LoginCommand("", "")
        validatorsAggregator.getValidatorHashMap().put(command.getClass(), new LoginUserValidator())

        def authDto = new AuthDTO(userId, firstName, lastName, email, token);
        mockNext.invoke() >> authDto

        when:
        sut.invoke(command, mockNext)

        then:
        def ex = thrown(ValidationException)
        def modelState = ex.getModelState()

        modelState.size() == 2
        modelState.get(LoginUserValidator.EMAIL_FIELD_NAME).contains(LoginUserValidator.EMAIL_EMPTY_MESSAGE)
        modelState.get(LoginUserValidator.EMAIL_FIELD_NAME).contains(LoginUserValidator.EMAIL_INVALID_MESSAGE)
        modelState.get(LoginUserValidator.PASSWORD_FIELD_NAME).contains(LoginUserValidator.PASSWORD_EMPTY_MESSAGE)
    }
}
