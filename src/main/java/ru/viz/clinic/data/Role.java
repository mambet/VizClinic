package ru.viz.clinic.data;

public enum Role {
    MEDIC, ENGINEER, ADMIN, TEMP;

    public String getAuthority() {
        return "ROLE_" + this;
    }
}
