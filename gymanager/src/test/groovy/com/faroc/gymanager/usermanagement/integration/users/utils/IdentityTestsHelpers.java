package com.faroc.gymanager.usermanagement.integration.users.utils;

public class IdentityTestsHelpers {
    public static String generateEmailFrom(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
    }
}
