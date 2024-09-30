package com.faroc.gymanager.sessionmanagement.integration.sessionscategories

import com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.requests.AddSessionCategoriesRequest
import com.faroc.gymanager.sessionmanagement.integration.sessionscategories.utils.SessionCategoriesEndpoints
import com.faroc.gymanager.usermanagement.api.users.contracts.v1.responses.AuthResponse
import com.faroc.gymanager.usermanagement.integration.users.utils.IdentityHttpEndpoints
import com.faroc.gymanager.usermanagement.integration.users.utils.RegisterRequestsTestsBuilder
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
class SessionCategoriesTests extends Specification {

    final TestRestTemplate restTemplate
    final Faker faker = new Faker()
    AuthResponse registerResponse
    String registerToken

    @Autowired
    SessionCategoriesTests(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    void setup() {
        RestAssured.baseURI = restTemplate.getRootUri()

        registerResponse = registerUser()
        registerToken = registerResponse.token()
    }

    def "when session category is added should return category created"() {
        given:
        def endpoint = SessionCategoriesEndpoints.getSessionCategoriesEndpointV1()
        def sessionCategories = List.of(
                createSessionCategory()
        )

        def request = new AddSessionCategoriesRequest(sessionCategories)

        when:
        def response = RestAssured.given()
            .header("Authorization", "Bearer " + registerToken)
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post(endpoint)

        then:
        response.statusCode() == HttpStatus.CREATED.value()
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

    String createSessionCategory() {
        return faker.marketing().buzzwords()
    }
}
