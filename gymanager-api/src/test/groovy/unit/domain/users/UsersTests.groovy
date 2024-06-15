package unit.domain.users

import com.faroc.gymanager.domain.shared.exceptions.ConflictException
import com.faroc.gymanager.domain.users.User
import com.faroc.gymanager.domain.users.UserProfileTypes
import com.faroc.gymanager.domain.users.abstractions.PasswordHasher
import com.faroc.gymanager.domain.users.errors.UserErrors
import net.datafaker.Faker
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class UsersTests extends Specification {

    Faker faker
    String firstName
    String lastName
    String email
    String password
    String wrongPassword
    String passwordHash
    PasswordHasher mockPasswordHasher

    def setup() {
        faker = new Faker()
        firstName = faker.name().firstName()
        lastName = faker.name().lastName()
        email = faker.internet().emailAddress()
        password = faker.internet().password()
        wrongPassword = faker.internet().password()
        passwordHash = new BCryptPasswordEncoder().encode(password)

        mockPasswordHasher = Mock(PasswordHasher)

    }

    def "when creating admin profile should add profile"() {
        given:
        var user = new User(firstName, lastName, email, passwordHash)

        when:
        user.createAdminProfile()

        then:
        user.getAdminId() != null
    }

    def "when admin profile already exists should throw conflict exception"() {
        given:
        var user = new User(firstName, lastName, email, passwordHash)

        when:
        user.createAdminProfile()
        user.createAdminProfile()

        then:
        var ex = thrown(ConflictException)
        ex.detail == UserErrors.CONFLICT_ADMIN_PROFILE
    }

    def "when password is valid should return true"() {
        given:
        var user = new User(firstName, lastName, email, passwordHash)
        mockPasswordHasher.validatePassword(password, passwordHash) >> true

        when:
        var validResult = user.validatePassword(password, mockPasswordHasher)

        then:
        validResult
    }

    def "when password is invalid should return false"() {
        given:
        var user = new User(firstName, lastName, email, passwordHash)
        mockPasswordHasher.validatePassword(wrongPassword, passwordHash) >> false

        when:
        var validResult = user.validatePassword(password, mockPasswordHasher)

        then:
        !validResult
    }

    def "when user has profiles should return them"() {
        given:
        var user = new User(firstName, lastName, email, passwordHash)
        user.createAdminProfile()
        var expectedProfiles = List.of(UserProfileTypes.ADMIN);

        when:
        var actualProfiles = user.getUserProfiles();

        then:
        actualProfiles == expectedProfiles
    }
}
