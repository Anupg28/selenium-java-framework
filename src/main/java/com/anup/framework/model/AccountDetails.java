package com.anup.framework.model;

public record AccountDetails(
        String name,
        String email,
        String password,
        String birthDay,
        String birthMonth,
        String birthYear,
        String firstName,
        String lastName,
        String address,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber
) {

    public static AccountDetails randomTestUser() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String name = "QA Automation " + uniqueSuffix;
        String email = "qa.automation." + uniqueSuffix + "@mailinator.com";

        return new AccountDetails(
                name,
                email,
                "Test@1234",
                "15",
                "6",
                "1995",
                "QA",
                "Automation",
                "221B Baker Street",
                "India",
                "Maharashtra",
                "Pune",
                "411001",
                "9876543210"
        );
    }
}
