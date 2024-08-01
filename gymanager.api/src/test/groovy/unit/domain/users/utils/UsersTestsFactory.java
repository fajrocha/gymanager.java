package unit.domain.users.utils;

import com.faroc.gymanager.domain.users.User;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UsersTestsFactory {
    public static final Faker faker = new Faker();

    public static User create() {
        return new User(
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                new BCryptPasswordEncoder().encode(faker.internet().password())
        );
    }

    public static User create(UUID id) {
        return new User(
                id,
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                new BCryptPasswordEncoder().encode(faker.internet().password())
        );
    }

    public static User create(String password) {
        return new User(
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                new BCryptPasswordEncoder().encode(password)
        );
    }
}
