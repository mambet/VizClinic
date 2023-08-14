package ru.viz.clinic.data;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum Role {
    MEDIC, ENGINEER, ADMIN, TEMP;

    public String getAuthority() {
        return "ROLE_" + this;
    }

    public static Optional<Role> authorityToRole(final String authority) {
        if (StringUtils.equals(authority, Role.MEDIC.getAuthority())) {
            return Optional.of(Role.MEDIC);
        }
        if (StringUtils.equals(authority, Role.ENGINEER.getAuthority())) {
            return Optional.of(Role.ENGINEER);
        }
        if (StringUtils.equals(authority, Role.TEMP.getAuthority())) {
            return Optional.of(Role.TEMP);
        }
        if (StringUtils.equals(authority, Role.ADMIN.getAuthority())) {
            return Optional.of(Role.ADMIN);
        }
        return Optional.empty();
    }
}
