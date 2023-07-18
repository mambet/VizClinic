package ru.viz.clinic.request.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.request.data.entity.User;
import ru.viz.clinic.request.data.service.UserService;

@Component
public class AuthenticatedUser {

    private final UserService userService;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserService userService) {
        this.userService = userService;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userService.getByName(userDetails.getUsername()));
    }
    @Transactional
    public Optional<UserDetails> getUserDetails() {
        SecurityContextHolder
                .getContextHolderStrategy();
        return authenticationContext.getAuthenticatedUser(UserDetails.class);

    }


    public void logout() {
        authenticationContext.logout();
    }

}
