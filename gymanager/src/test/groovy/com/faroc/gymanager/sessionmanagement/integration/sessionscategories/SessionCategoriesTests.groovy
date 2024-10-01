package com.faroc.gymanager.sessionmanagement.integration.sessionscategories

import com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.requests.AddSessionCategoriesRequest
import com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.responses.AddSessionCategoriesResponse
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory
import com.faroc.gymanager.sessionmanagement.integration.sessionscategories.utils.SessionCategoriesEndpoints
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse
import com.faroc.gymanager.usermanagement.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.RegisterRequestsTestsBuilder
import com.faroc.gymanager.utils.integration.ContainersSpecification
import io.restassured.RestAssured
import io.restassured.http.ContentType
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionCategoriesTests extends ContainersSpecification {
    final FIRST_SESSION_CATEGORY = "Pilates"
    final SECOND_SESSION_CATEGORY = "Functional"
    final THIRD_SESSION_CATEGORY = "Bike"
    final Faker faker = new Faker()
    AuthResponse registerResponse
    String registerToken

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    SessionCategoriesGateway sessionCategoriesGateway

    void setup() {
        RestAssured.baseURI = restTemplate.getRootUri()

        registerResponse = registerUser()
        registerToken = registerResponse.token()
    }

    def "when session categories are added should return categories created"() {
        given:
        def endpoint = SessionCategoriesEndpoints.getSessionCategoriesEndpointV1()
        def sessionCategoriesToAdd = List.of(
                FIRST_SESSION_CATEGORY,
                SECOND_SESSION_CATEGORY
        )

        def request = new AddSessionCategoriesRequest(sessionCategoriesToAdd)

        when:
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + registerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(endpoint)

        def responseBody = response.body().as(AddSessionCategoriesResponse.class)
        def sessionCategoriesAdded = responseBody.sessionCategories()
        def sessionCategoriesNamesAdded = getSessionCategoriesNamesAdded(sessionCategoriesAdded)
        def sessionCategoriesIdsAdded = getSessionCategoriesIdsAdded(sessionCategoriesAdded)

        then:

        response.statusCode() == HttpStatus.CREATED.value()
        sessionCategoriesNamesAdded == sessionCategoriesToAdd
        sessionCategoriesGateway.existsAll(sessionCategoriesIdsAdded)

        // Tear Down:
        sessionCategoriesGateway.deleteAll(sessionCategoriesIdsAdded)
    }

    def "when session categories already exist should only add new ones"() {
        given:
        def endpoint = SessionCategoriesEndpoints.getSessionCategoriesEndpointV1()

        def sessionCategoriesToAddFirst = List.of(
                FIRST_SESSION_CATEGORY,
                SECOND_SESSION_CATEGORY
        )

        def sessionCategoriesToAddSecond = List.of(
                sessionCategoriesToAddFirst.first(),
                sessionCategoriesToAddFirst.last(),
                THIRD_SESSION_CATEGORY)

        def requestFirst = new AddSessionCategoriesRequest(sessionCategoriesToAddFirst)
        def requestSecond = new AddSessionCategoriesRequest(sessionCategoriesToAddSecond)

        when:
        RestAssured.given()
                .header("Authorization", "Bearer " + registerToken)
                .contentType(ContentType.JSON)
                .body(requestFirst)
                .when()
                .post(endpoint)

        def response = RestAssured.given()
                .header("Authorization", "Bearer " + registerToken)
                .contentType(ContentType.JSON)
                .body(requestSecond)
                .when()
                .post(endpoint)

        def responseBody = response.body().as(AddSessionCategoriesResponse.class)
        def sessionCategoriesAdded = responseBody.sessionCategories()
        def sessionCategoriesNamesAdded = getSessionCategoriesNamesAdded(sessionCategoriesAdded)
        def sessionCategoriesIdsAdded = getSessionCategoriesIdsAdded(sessionCategoriesAdded)

        then:
        response.statusCode() == HttpStatus.CREATED.value()
        sessionCategoriesNamesAdded == List.of(THIRD_SESSION_CATEGORY)
        sessionCategoriesGateway.exists(sessionCategoriesAdded.first().getId())

        // Tear Down:
        sessionCategoriesGateway.deleteAll(sessionCategoriesIdsAdded)
    }

    AuthResponse registerUser() {
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

    List<String> getSessionCategoriesNamesAdded(List<SessionCategory> sessionCategories) {
        return sessionCategories.stream()
                .map {sc -> sc.getName()}
                .toList()
    }

    List<UUID> getSessionCategoriesIdsAdded(List<SessionCategory> sessionCategories) {
        return sessionCategories.stream()
                .map {sc -> sc.getId()}
                .toList()
    }
}
