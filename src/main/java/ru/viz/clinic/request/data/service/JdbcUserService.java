package ru.viz.clinic.request.data.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class JdbcUserService extends JdbcUserDetailsManager {

    public JdbcUserService(DataSource dataSource) {
        super(dataSource);


    }

    @Override
    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        return super.createNewAuthentication(currentAuth, Strings.EMPTY);
    }
}
