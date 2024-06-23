package unit.domain.trainers.utils;

import com.faroc.gymanager.domain.trainers.Trainer;

import java.util.UUID;

public class TrainersFactory {
    public static Trainer create() {
        return new Trainer(UUID.randomUUID());
    }
}
