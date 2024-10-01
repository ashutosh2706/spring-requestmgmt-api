package com.wizardform.api;

public final class Constants {
    public enum StatusCode {
        STATUS_PENDING,
        STATUS_APPROVED,
        STATUS_REJECTED
    }

    public enum PriorityCode {
        PRIORITY_HIGH,
        PRIORITY_NORM,
        PRIORITY_LOW
    }

    public enum Roles {
        ROLE_USER,
        ROLE_ADMIN
    }
}
