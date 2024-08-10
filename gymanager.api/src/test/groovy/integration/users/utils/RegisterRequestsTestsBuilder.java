package integration.users.utils;

import com.faroc.gymanager.users.requests.RegisterRequest;
import net.datafaker.Faker;

public class RegisterRequestsTestsBuilder {
    private static final Faker faker = new Faker();
    private static final String DEFAULT_PASSWORD = "Pwd12345!";

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
            email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";

        if (password == null)
            password = DEFAULT_PASSWORD;

        return new RegisterRequest(firstName, lastName, email, password);
    }
}
