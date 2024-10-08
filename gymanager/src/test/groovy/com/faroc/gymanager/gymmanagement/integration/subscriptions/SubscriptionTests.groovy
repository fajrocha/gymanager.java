package com.faroc.gymanager.gymmanagement.integration.subscriptions

import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionRequestMappers
import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.admins.errors.AdminErrors
import com.faroc.gymanager.utils.integration.ContainersSpecification

import com.faroc.gymanager.gymmanagement.integration.subscriptions.utils.SubscriptionsEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.usermanagement.integration.users.utils.UsersHttpEndpoints
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests.SubscribeRequest
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.responses.SubscriptionResponse
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AdminCreatedResponse
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

import static com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriptionTests extends ContainersSpecification {
    @Autowired
    TestRestTemplate restTemplate
    @Autowired
    AdminsGateway adminsGateway
    @Autowired
    SubscriptionsGateway subscriptionsGateway

    final Faker faker = new Faker()
    String token
    UUID userId

    def setup() {
        RestAssured.baseURI = restTemplate.getRootUri()

        def registerResponse = registerUser()
        token = registerResponse.token()
        userId = registerResponse.id()
    }

    def "when subscribing and admin does not exist should return not found"(String subscriptionType) {
        given:
        def subscribeRequest = new SubscribeRequest(subscriptionType, UUID.randomUUID())

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(subscribeRequest)
                .when()
                .post(SubscriptionsEndpoints.SUBSCRIBE_ENDPOINT_V1)

        then:
        response.statusCode() == HttpStatus.NOT_FOUND.value()
        response.body().jsonPath().getString("detail") == AdminErrors.NOT_FOUND

        where:
        subscriptionType << ["Free", "Starter", "Pro"]
    }

    def "when subscribing should add subscription to admin and create subscription"(String subscriptionType) {
        given:
        def addAdminResponse = addAdminProfile(userId, token)
        def adminId = addAdminResponse.adminId()
        def subscribeRequest = new SubscribeRequest(subscriptionType, adminId)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(subscribeRequest)
                .when()
                .post(SubscriptionsEndpoints.SUBSCRIBE_ENDPOINT_V1)

        then:
        def responseBody = response.body().as(SubscriptionResponse.class)
        def admin = adminsGateway.findById(adminId).orElseThrow()
        def subscriptionId = responseBody.id()
        def subscription = subscriptionsGateway.findById(subscriptionId).orElseThrow()

        admin.getSubscriptionId() == subscriptionId
        subscription.getId() == subscriptionId
        subscription.getAdminId() == adminId
        subscription.getSubscriptionType() == SubscriptionRequestMappers.toDomain(subscriptionType)
        subscriptionType == responseBody.subscriptionType().name()
        response.statusCode() == HttpStatus.CREATED.value()

        where:
        subscriptionType << ["Free", "Starter", "Pro"]
    }

    private AuthResponse registerUser() {
        final def firstName = faker.name().firstName()
        final def lastName = faker.name().lastName()
        def registerRequest = new RegisterRequestsTestsBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .build()

        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(IdentityHttpEndpoints.REGISTRATION_ENDPOINT)

        return response.as(AuthResponse)
    }

    private static AdminCreatedResponse addAdminProfile(UUID userId, String token) {
        def endpoint = UsersHttpEndpoints.getAdminProfileEndpoint(userId)

        var response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body()
                .when()
                .post(endpoint)

        return response.as(AdminCreatedResponse)
    }
}
