package com.faroc.gymanager;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GymanagerApiApplicationTests {

    @Test
    void contextLoads() {
        var result = EmailValidator.getInstance().isValid("fabio.lanzoni@model.com");
    }

}
