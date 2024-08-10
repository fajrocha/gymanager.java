package integration.users.utils;

import com.faroc.gymanager.users.requests.RegisterRequest;
import net.datafaker.Faker;

public class IdentityTestsFactory {
    static final String DEFAULT_PASSWORD = "Pwd12345!";
    public static RegisterRequest createRegisterRequest() {
        var firstName = new Faker().name().firstName();
        var lastName = new Faker().name().lastName();
        var email = firstName + "." + lastName + "@gmail.com";

        return new RegisterRequest(
                firstName,
                lastName,
                email,
                DEFAULT_PASSWORD
        );
    }

    public static RegisterRequest createRegisterRequest(String email) {
        var firstName = new Faker().name().firstName();
        var lastName = new Faker().name().lastName();

        return new RegisterRequest(
                firstName,
                lastName,
                email,
                DEFAULT_PASSWORD
        );
    }
}
