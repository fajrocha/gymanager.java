package unit.application.users.commands

import com.faroc.gymanager.application.admins.gateways.AdminsGateway
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
    AdminsGateway mockAdminsGateway
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

        userId = UUID.randomUUID();
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        passwordHash = new BCryptPasswordEncoder().encode(password)

        mockUsersGateway = Mock(UsersGateway);
        mockAdminsGateway = Mock(AdminsGateway);
        command = new AddAdminCommand(userId);
        
        sut = new AddAdminHandler(
                mockUsersGateway,
                mockAdminsGateway
        )
    }

    def "when user to add admin profile not found should throw not found exception"() {
        given:

        mockUsersGateway.findById(userId) >> Optional.empty()

        when:
        sut.handle(command)

        then:
        var ex = thrown(ResourceNotFoundException)
        ex.detail == UserErrors.NOT_FOUND
    }

    def "when user exists should return admin id and create admin profile"() {
        given:
        var user = User.MapFromStorage(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                passwordHash,
                null,
                null,
                null
        )
        mockUsersGateway.findById(userId) >> Optional.of(user)

        when:
        var actualResult = sut.handle(command)

        then:
        1 * mockUsersGateway.update(user)
        1 * mockAdminsGateway.save(_ as Admin)
        actualResult == user.getAdminId()
    }
}
