package com.faroc.gymanager.gymmanagement.integration.gyms

import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionRequestMappers
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway
import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.requests.AddGymRequest
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.responses.GymResponse
import com.faroc.gymanager.gymmanagement.integration.gyms.utils.GymsEndpoints
import com.faroc.gymanager.utils.integration.ContainersSpecification
import com.faroc.gymanager.gymmanagement.integration.subscriptions.utils.SubscriptionsEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.IdentitySharedConstants
import com.faroc.gymanager.usermanagement.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.usermanagement.integration.users.utils.UsersHttpEndpoints
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests.SubscribeRequest
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.responses.SubscriptionResponse
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.LoginRequest
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AdminCreatedResponse
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

import static com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi.Free
import static com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi.Pro
import static com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi.Starter

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
    String gymName
    AdminCreatedResponse addAdminResponse
    AuthResponse registerResponse

    def setup() {
        gymName = faker.marketing().buzzwords()
        RestAssured.baseURI = restTemplate.getRootUri()
        registerResponse = registerUser()
        registerToken = registerResponse.token()
        addAdminResponse = addAdminProfile(registerResponse.id(), registerToken)
    }

    def "when gym is added but token doesn't have the right permissions should get forbidden"() {
        given:
        def endpoint = GymsEndpoints.getAddGymEndpointV1(UUID.randomUUID())
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
        def loginResponse = loginUser(registerResponse.email())
        def loginToken = loginResponse.token()
        def endpoint = GymsEndpoints.getAddGymEndpointV1(UUID.randomUUID())
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

    def "when gym is added should add gym to subscription and create gym"(String subscriptionType) {
        given:
        def subscribeResponse = subscribe(
                subscriptionType,
                addAdminResponse.adminId(),
                registerToken)
        def loginResponse = loginUser(registerResponse.email())
        def loginToken = loginResponse.token()
        def subscriptionId = subscribeResponse.id()
        def endpoint = GymsEndpoints.getAddGymEndpointV1(subscriptionId)
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

        where:
        subscriptionType << ["Free", "Starter", "Pro"]
    }

    def "when gym is added and subscription allows no more gyms should get internal server error"(
            String subscriptionType
    ) {
        given:
        def subscribeResponse = subscribe(
                subscriptionType,
                addAdminResponse.adminId(),
                registerToken)
        def loginResponse = loginUser(registerResponse.email())
        def loginToken = loginResponse.token()
        def subscriptionId = subscribeResponse.id()
        def endpoint = GymsEndpoints.getAddGymEndpointV1(subscriptionId)

        addMaxGymsPerSubscriptionType(endpoint, loginToken, subscriptionType)

        def addGymRequest = new AddGymRequest(faker.marketing().buzzwords())

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + loginToken)
                .contentType(ContentType.JSON)
                .body(addGymRequest)
                .when()
                .post(endpoint)

        then:
        response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.body().jsonPath().getString("detail") == SubscriptionErrors.MAX_GYMS_REACHED

        where:
        subscriptionType << [Free, Starter]
    }

    def addMaxGymsPerSubscriptionType(String endpoint, String loginToken, String subscriptionTypeRequest) {
        def subscriptionType = SubscriptionRequestMappers.toDomain(subscriptionTypeRequest)
        def gymsToAdd = Subscription.getMaxGyms(subscriptionType)

        for (int gym = 0; gym < gymsToAdd; gym++) {
            def addGymRequest = new AddGymRequest(faker.marketing().buzzwords())
            RestAssured.given()
                    .header("Authorization", "Bearer " + loginToken)
                    .contentType(ContentType.JSON)
                    .body(addGymRequest)
                    .when()
                    .post(endpoint)
        }
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

    private static SubscriptionResponse subscribe(String subscriptionType, UUID adminId, String token) {
        def request = new SubscribeRequest(subscriptionType, adminId)

        def response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(SubscriptionsEndpoints.SUBSCRIBE_ENDPOINT_V1)

        return response.as(SubscriptionResponse)
    }
}
