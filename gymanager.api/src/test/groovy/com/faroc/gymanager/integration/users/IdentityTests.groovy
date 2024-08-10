package com.faroc.gymanager.integration.users

import com.faroc.gymanager.application.security.exceptions.PasswordComplexityException
import com.faroc.gymanager.application.shared.exceptions.ValidationException
import com.faroc.gymanager.application.users.gateways.UsersGateway
import com.faroc.gymanager.integration.users.utils.IdentityTestsHelpers
import com.faroc.gymanager.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.users.requests.LoginRequest
import com.faroc.gymanager.users.requests.RegisterRequest
import com.faroc.gymanager.users.responses.AuthResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.HttpStatus
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class IdentityTests extends Specification {
    static final String DATABASE_NAME = "gymanager"
    static final String DATABASE_USERNAME = "user"
    static final String DATABASE_PWD = "password"

    @ServiceConnection
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USERNAME)
            .withPassword(DATABASE_PWD)

    static {
        postgresContainer.start()
    }

    def setupSpec() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword())
                .load()
        flyway.migrate()
    }

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    UsersGateway usersGateway

    final String REGISTER_ENDPOINT = "/authentication/register"
    final String LOGIN_ENDPOINT = "/authentication/login"
    final String INVALID_LOGIN_PWD = "invalid"
    final Faker faker = new Faker()

    def setup() {
        RestAssured.baseURI = restTemplate.getRootUri()
    }

    def "when user requests to register should register the user"() {
        given:
        def request = new RegisterRequestsTestsBuilder().build()

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(REGISTER_ENDPOINT)

        then:
        response.statusCode() == HttpStatus.OK.value()
        def responseBody = response.body().as(AuthResponse.class)
        responseBody.firstName() == request.firstName()
        responseBody.lastName() == request.lastName()
        responseBody.email() == request.email()
        responseBody.token() != "" || responseBody.token() != null
    }

    def "when user requests to register but first and last names and email are invalid should return bad request"() {
        given:
        def request = new RegisterRequestsTestsBuilder()
                .withFirstName("")
                .withLastName("")
                .withEmail("invalidEmail")
                .build()

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(REGISTER_ENDPOINT)

        def responseBodyJson = response.body().jsonPath()

        then:
        response.statusCode() == HttpStatus.BAD_REQUEST.value()
        responseBodyJson.getString("detail") == ValidationException.DEFAULT_DETAIL
        responseBodyJson.getMap("errors").containsKey("email")
        responseBodyJson.getMap("errors").containsKey("firstName")
        responseBodyJson.getMap("errors").containsKey("lastName")
    }

    def "when user requests to register but password does not meet requirements invalid should return bad request"() {
        given:
        def invalidPwd = "invalidPwd"
        def request = new RegisterRequestsTestsBuilder().withPassword(invalidPwd).build()

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(REGISTER_ENDPOINT)

        def responseBodyJson = response.body().jsonPath()

        then:
        response.statusCode() == HttpStatus.BAD_REQUEST.value()
        responseBodyJson.getString("detail") == PasswordComplexityException.DEFAULT_DETAIL
    }

    def "when user requests to register but email is in use should return conflict"() {
        given:
        def requestExistingUser = new RegisterRequestsTestsBuilder().build()
        def requestUserSameEmail = new RegisterRequestsTestsBuilder()
                .withEmail(requestExistingUser.email())
                .build()

        when:
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestExistingUser)
                .when()
                .post(REGISTER_ENDPOINT)

        def responseUserSameEmail = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestUserSameEmail)
                .when()
                .post(REGISTER_ENDPOINT)

        then:
        responseUserSameEmail.statusCode() == HttpStatus.CONFLICT.value()
    }

    def "when user logins with wrong password should get unauthorized"() {
        given:
        def registerRequest = registerUser()
        final def wrongPwd = registerRequest.password() + '1'
        def loginRequest = new LoginRequest(registerRequest.email(), wrongPwd)

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(LOGIN_ENDPOINT)

        then:
        response.statusCode() == HttpStatus.UNAUTHORIZED.value()
    }

    def "when user logins with wrong email should get unauthorized"() {
        given:
        def registerRequest = registerUser()
        final def wrongEmail = IdentityTestsHelpers.generateEmailFrom(
                registerRequest.lastName(),
                registerRequest.firstName()
        )
        def loginRequest = new LoginRequest(wrongEmail, registerRequest.password())

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(LOGIN_ENDPOINT)

        then:
        response.statusCode() == HttpStatus.UNAUTHORIZED.value()
    }

    private RegisterRequest registerUser() {
        final def firstName = faker.name().firstName()
        final def lastName = faker.name().lastName()
        def registerRequest = new RegisterRequestsTestsBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .build()

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(REGISTER_ENDPOINT)

        return registerRequest
    }
}
