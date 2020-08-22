package com.kcymerys.java.todowithcognito.security.model;

public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private String value;

    Role(String role) {
        this.value = role;
    }

    public String value() {
        return this.value;
    }

}
