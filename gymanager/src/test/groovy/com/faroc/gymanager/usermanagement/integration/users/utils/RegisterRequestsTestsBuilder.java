package com.faroc.gymanager.usermanagement.integration.users.utils;

import com.faroc.gymanager.usermanagement.api.users.contracts.v1.requests.RegisterRequest;
import net.datafaker.Faker;

public class RegisterRequestsTestsBuilder {
    private static final Faker faker = new Faker();

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public RegisterRequestsTestsBuilder withFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public RegisterRequestsTestsBuilder withLastName(String lastName) {
        this.lastName = lastName;

        return this;
    }

    public RegisterRequestsTestsBuilder withEmail(String email) {
        this.email = email;

        return this;
    }

    public RegisterRequestsTestsBuilder withPassword(String password) {
        this.password = password;

        return this;
    }

    public RegisterRequest build() {
        if (firstName == null)
            firstName = faker.name().firstName();

        if (lastName == null)
            lastName = faker.name().lastName();

        if (email == null)
            email = IdentityTestsHelpers.generateEmailFrom(firstName, lastName);

        if (password == null)
            password = IdentitySharedConstants.DEFAULT_PASSWORD;

        return new RegisterRequest(firstName, lastName, email, password);
    }
}
