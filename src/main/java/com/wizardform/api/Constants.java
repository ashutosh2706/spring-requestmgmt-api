package com.wizardform.api;

public final class Constants {

    public static final String UPLOAD_DIR = "Attachments";
    public static final String ROOT_PATH = System.getProperty("user.dir");
    public static final String BEARER_TOKEN = "Bearer";

    public static final class StatusCode {
        public static final int STATUS_PENDING = 1;
        public static final int STATUS_APPROVED = 2;
        public static final int STATUS_REJECTED = 3;
    }

    public static final class PriorityCode {
        public static final int PRIORITY_HIGH = 1;
        public static final int PRIORITY_NORM = 2;
        public static final int PRIORITY_LOW = 3;
    }

    public static final class Roles {
        public static final int ROLE_USER = 1;
        public static final int ROLE_ADMIN = 2;
    }
}
