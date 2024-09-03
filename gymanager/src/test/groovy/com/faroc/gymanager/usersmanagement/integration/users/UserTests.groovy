package com.faroc.gymanager.usersmanagement.integration.users

import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.sessionmanagement.application.participants.gateways.ParticipantsGateway
import com.faroc.gymanager.sessionmanagement.application.trainers.gateways.TrainersGateway
import com.faroc.gymanager.usermanagement.application.users.gateways.UsersGateway
import com.faroc.gymanager.utils.integration.ContainersSpecification
import com.faroc.gymanager.usermanagement.api.users.responses.v1.AdminCreatedResponse
import com.faroc.gymanager.usermanagement.api.users.responses.v1.AuthResponse
import com.faroc.gymanager.usermanagement.api.users.responses.v1.ParticipantCreatedResponse
import com.faroc.gymanager.usermanagement.api.users.responses.v1.TrainerCreatedResponse
import com.faroc.gymanager.usersmanagement.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.usersmanagement.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.usersmanagement.integration.users.utils.UsersHttpEndpoints
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
    @Autowired
    AdminsGateway adminsGateway
    @Autowired
    TrainersGateway trainersGateway
    @Autowired
    ParticipantsGateway participantsGateway

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
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(userId, profile)

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
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(UUID.randomUUID(), profile)

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
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(userId, profile)
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
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(userId, "admin")

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
        def adminId = responseBody.adminId()
        def admin = adminsGateway.findById(adminId).orElseThrow()

        user.getAdminId() == adminId
        admin.getId() == adminId
        admin.getUserId() == userId
        response.statusCode() == HttpStatus.OK.value()
    }

    def "when adding trainer profile and user exists should add trainer profile"() {
        given:
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(userId, "trainer")

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
        def trainerId = responseBody.trainerId()
        def trainer = trainersGateway.findById(trainerId).orElseThrow()

        user.getTrainerId() == trainerId
        trainer.getId() == trainerId
        trainer.getUserId() == userId
        response.statusCode() == HttpStatus.OK.value()
    }

    def "when adding participant profile and user exists should add participant profile"() {
        given:
        def profileEndpoint = UsersHttpEndpoints.getProfileEndpoint(userId, "participant")

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
        def participantId = responseBody.participantId()
        def participant = participantsGateway.findById(participantId).orElseThrow()

        user.getParticipantId() == participantId
        participant.getId() == participantId
        participant.getUserId() == userId
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
                .post(IdentityHttpEndpoints.REGISTRATION_ENDPOINT)

        return response.as(AuthResponse)
    }
}
