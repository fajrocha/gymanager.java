package com.faroc.gymanager.integration.users

import com.faroc.gymanager.application.users.gateways.UsersGateway
import com.faroc.gymanager.integration.shared.ContainersSpecification
import com.faroc.gymanager.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.users.responses.AdminCreatedResponse
import com.faroc.gymanager.users.responses.AuthResponse
import com.faroc.gymanager.users.responses.ParticipantCreatedResponse
import com.faroc.gymanager.users.responses.TrainerCreatedResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTests extends ContainersSpecification {
    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    UsersGateway usersGateway

    final String REGISTER_ENDPOINT = "/authentication/register"
    final Faker faker = new Faker()
    String token
    UUID userId

    def setup() {
        RestAssured.baseURI = restTemplate.getRootUri()
        def response = registerUser()
        token = response.token()
        userId = response.id()
    }

    def "when adding profile but token not provided should get unauthorized"(String profile) {
        given:
        def profileEndpoint = getProfileEndpoint(userId, profile)

        when:
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        response.statusCode() == HttpStatus.UNAUTHORIZED.value()

        where:
        profile << ["admin", "participant", "trainer"]
    }

    def "when adding profile but user does not match token user should get unauthorized"(String profile) {
        given:
        def profileEndpoint = getProfileEndpoint(UUID.randomUUID(), profile)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        response.statusCode() == HttpStatus.UNAUTHORIZED.value()

        where:
        profile << ["admin", "participant", "trainer"]
    }

    def "when adding profile but user does not exist should get not found"(String profile) {
        given:
        def profileEndpoint = getProfileEndpoint(userId, profile)
        usersGateway.delete(userId)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        response.statusCode() == HttpStatus.NOT_FOUND.value()

        where:
        profile << ["admin", "participant", "trainer"]
    }

    def "when adding admin profile and user exists should add admin profile"() {
        given:
        def profileEndpoint = getProfileEndpoint(userId, "admin")

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        def responseBody = response.body().as(AdminCreatedResponse.class)
        def user = usersGateway.findById(userId).orElseThrow()

        user.getAdminId() == responseBody.adminId()
        response.statusCode() == HttpStatus.OK.value()
    }

    def "when adding trainer profile and user exists should add admin profile"() {
        given:
        def profileEndpoint = getProfileEndpoint(userId, "trainer")

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        def responseBody = response.body().as(TrainerCreatedResponse.class)
        def user = usersGateway.findById(userId).orElseThrow()

        user.getTrainerId() == responseBody.trainerId()
        response.statusCode() == HttpStatus.OK.value()
    }

    def "when adding participant profile and user exists should add admin profile"() {
        given:
        def profileEndpoint = getProfileEndpoint(userId, "participant")

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(profileEndpoint)

        then:
        def responseBody = response.body().as(ParticipantCreatedResponse.class)
        def user = usersGateway.findById(userId).orElseThrow()

        user.getParticipantId() == responseBody.participantId()
        response.statusCode() == HttpStatus.OK.value()
    }

    private AuthResponse registerUser() {
        final def firstName = faker.name().firstName()
        final def lastName = faker.name().lastName()
        def registerRequest = new RegisterRequestsTestsBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .build()

        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(REGISTER_ENDPOINT)

        return response.as(AuthResponse)
    }

    private static String getProfileEndpoint(UUID userId, String profile) {
        return "users/" + userId + "/profiles/" + profile
    }
}
