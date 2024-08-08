package unit.domain.trainers.utils;

import com.faroc.gymanager.domain.trainers.Trainer;

import java.util.UUID;

public class TrainersTestsFactory {
    public static Trainer create() {
        return new Trainer(UUID.randomUUID());
    }

    public static Trainer create(UUID id) {

        return new Trainer(id, UUID.randomUUID());
    }
}