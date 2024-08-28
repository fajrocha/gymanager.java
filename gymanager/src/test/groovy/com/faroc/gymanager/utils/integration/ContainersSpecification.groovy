package com.faroc.gymanager.utils.integration

import org.flywaydb.core.Flyway
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@Testcontainers
abstract class ContainersSpecification extends Specification {
    static final String DATABASE_NAME = "gymanager"
    static final String DATABASE_USERNAME = "user"
    static final String DATABASE_PWD = "password"

    @ServiceConnection
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USERNAME)
            .withPassword(DATABASE_PWD)

    static {
        postgresContainer.start()
    }

    def setupSpec() {

        Flyway flyway = Flyway.configure()
                .dataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword())
                .load()
        flyway.migrate()
    }
}
