package com.faroc.gymanager.integration.gyms

import com.faroc.gymanager.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors
import com.faroc.gymanager.gyms.requests.AddGymRequest
import com.faroc.gymanager.gyms.responses.GymResponse
import com.faroc.gymanager.integration.gyms.utils.EndpointsGyms
import com.faroc.gymanager.integration.shared.ContainersSpecification
import com.faroc.gymanager.integration.subscriptions.utils.SubscriptionsHttpEndpoints
import com.faroc.gymanager.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.integration.users.utils.IdentitySharedConstants
import com.faroc.gymanager.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.integration.users.utils.UsersHttpEndpoints
import com.faroc.gymanager.subscriptions.requests.SubscribeRequest
import com.faroc.gymanager.subscriptions.responses.SubscriptionResponse
import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi
import com.faroc.gymanager.users.requests.LoginRequest
import com.faroc.gymanager.users.responses.AdminCreatedResponse
import com.faroc.gymanager.users.responses.AuthResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GymTests extends ContainersSpecification {
    @Autowired
    TestRestTemplate restTemplate
    @Autowired
    SubscriptionsGateway subscriptionsGateway
    @Autowired
    GymsGateway gymsGateway

    final Faker faker = new Faker()
    String registerToken
    UUID subscriptionId
    String gymName
    String loginToken

    def setup() {
        gymName = faker.marketing().buzzwords()
        RestAssured.baseURI = restTemplate.getRootUri()
        def registerResponse = registerUser()
        registerToken = registerResponse.token()
        def addAdminResponse = addAdminProfile(registerResponse.id(), registerToken)
        def subscribeResponse = subscribe(
                SubscriptionTypeApi.Free,
                addAdminResponse.adminId(),
                registerToken)
        def loginResponse = loginUser(registerResponse.email())
        loginToken = loginResponse.token()
        subscriptionId = subscribeResponse.id()
    }

    def "when gym is added but token doesn't have the right permissions should get forbidden"() {
        given:
        def endpoint = EndpointsGyms.getAddGymEndpoint(UUID.randomUUID())
        def request = new AddGymRequest(gymName)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + registerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(endpoint)

        then:
        response.statusCode() == HttpStatus.FORBIDDEN.value()
    }

    def "when gym is added but subscription does not exist should get internal server error"() {
        given:
        def endpoint = EndpointsGyms.getAddGymEndpoint(UUID.randomUUID())
        def request = new AddGymRequest(gymName)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(endpoint)

        then:
        response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
    }

    def "when gym is added should add gym to subscription and create gym"() {
        given:
        def endpoint = EndpointsGyms.getAddGymEndpoint(subscriptionId)
        def request = new AddGymRequest(gymName)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(endpoint)

        then:
        response.statusCode() == HttpStatus.CREATED.value()
        def responseBody = response.body().as(GymResponse.class)
        def responseGymId = responseBody.id()
        def subscription = subscriptionsGateway.findById(subscriptionId).orElseThrow()
        def gym = gymsGateway.findById(responseGymId).orElseThrow()

        subscription.hasGym(responseGymId)
        responseGymId == gym.getId()
        responseBody.name() == gymName
    }

    def "when gym is added and subscription allows no more gyms should get internal server error"() {
        given:
        def endpoint = EndpointsGyms.getAddGymEndpoint(subscriptionId)
        def addGymRequest1 = new AddGymRequest(faker.marketing().buzzwords())
        def addGymRequest2 = new AddGymRequest(faker.marketing().buzzwords())

        RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .contentType(ContentType.JSON)
                .body(addGymRequest1)
                .when()
                .post(endpoint)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .contentType(ContentType.JSON)
                .body(addGymRequest2)
                .when()
                .post(endpoint)

        then:
        response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.body().jsonPath().getString("detail") == SubscriptionErrors.MAX_GYMS_REACHED
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

    private static AuthResponse loginUser(String email) {
        def request = new LoginRequest(email, IdentitySharedConstants.DEFAULT_PASSWORD)

        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(IdentityHttpEndpoints.LOGIN_ENDPOINT)

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

    private static SubscriptionResponse subscribe(SubscriptionTypeApi subscriptionType, UUID adminId, String token) {
        def request = new SubscribeRequest(subscriptionType, adminId)

        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(SubscriptionsHttpEndpoints.SUBSCRIBE_ENDPOINT)

        return response.as(SubscriptionResponse)
    }
}
