package unit.application.users.commands

import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher
import com.faroc.gymanager.application.users.gateways.AdminsGateway
import com.faroc.gymanager.application.security.CurrentUserProvider
import com.faroc.gymanager.application.security.DTOs.CurrentUserDTO
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException
import com.faroc.gymanager.application.users.commands.addadmin.AddAdminCommand
import com.faroc.gymanager.application.users.commands.addadmin.AddAdminHandler
import com.faroc.gymanager.application.users.gateways.UsersGateway
import com.faroc.gymanager.domain.admins.Admin
import com.faroc.gymanager.domain.users.User
import com.faroc.gymanager.domain.users.errors.UserErrors
import net.datafaker.Faker
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class AddAdminHandlerTests extends Specification {

    UUID userId
    UsersGateway mockUsersGateway
    CurrentUserProvider mockCurrentUserProvider
    DomainEventsPublisher mockDomainEventsPublisher
    AddAdminCommand command
    AddAdminHandler sut
    Faker faker
    String firstName
    String lastName
    String email
    String password
    String passwordHash

    def setup() {
        faker = new Faker()

        userId = UUID.randomUUID()
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        passwordHash = new BCryptPasswordEncoder().encode(password)

        mockUsersGateway = Mock(UsersGateway)
        mockCurrentUserProvider = Mock(CurrentUserProvider)
        mockDomainEventsPublisher = Mock(DomainEventsPublisher)
        command = new AddAdminCommand(userId)
        
        sut = new AddAdminHandler(
                mockUsersGateway,
                mockCurrentUserProvider,
                mockDomainEventsPublisher
        )
    }

    def "when current user does not match user on request"() {
        given:
        mockCurrentUserProvider.getCurrentUser() >> new CurrentUserDTO(UUID.randomUUID(), List.of(), List.of())

        when:
        sut.handle(command)

        then:
        thrown(UnauthorizedException)
    }

    def "when user to add admin profile not found should throw not found exception"() {
        given:
        mockCurrentUserProvider.getCurrentUser() >> new CurrentUserDTO(userId, List.of(), List.of())
        mockUsersGateway.findById(userId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.detail == UserErrors.NOT_FOUND
    }

    def "when user exists should return admin id and create admin profile"() {
        given:
        def user = User.MapFromStorage(
                userId,
                firstName,
                lastName,
                email,
                passwordHash,
                null,
                null,
                null
        )
        mockCurrentUserProvider.getCurrentUser() >> new CurrentUserDTO(userId, List.of(), List.of())
        mockUsersGateway.findById(userId) >> Optional.of(user)

        when:
        def actualResult = sut.handle(command)

        then:
        1 * mockUsersGateway.update(user)
        1 * mockDomainEventsPublisher.publishEventsFromAggregate(user)
        actualResult == user.getAdminId()
    }
}
