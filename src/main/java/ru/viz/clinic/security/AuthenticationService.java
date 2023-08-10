package ru.viz.clinic.security;

import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.model.PersonalDTO;
import ru.viz.clinic.service.PersonalService;

@Component
public class AuthenticationService {
    private final AuthenticationContext authenticationContext;
    private final AuthenticationManager authenticationManager;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            @NotNull final AuthenticationContext authenticationContext,
            @NotNull final AuthenticationManager authenticationManager,
            @NotNull final JdbcUserDetailsManager jdbcUserDetailsManager,
            @NotNull final PasswordEncoder passwordEncoder

    ) {
        this.authenticationContext = Objects.requireNonNull(authenticationContext);
        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.jdbcUserDetailsManager = Objects.requireNonNull(jdbcUserDetailsManager);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }

    @Transactional
    public Optional<UserDetails> getUserDetails() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class);
    }

    public void logout() {
        authenticationContext.logout();
    }

    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    @Transactional
    public void createTempUserDetails(PersonalDTO personalDTO) {
        UserDetails userDetails = User.builder()
                .disabled(false)
                .password(passwordEncoder.encode(personalDTO.getPassword()))
                .username(personalDTO.getUsername())
                .authorities(new SimpleGrantedAuthority(Role.TEMP.getAuthority()))
                .build();
        jdbcUserDetailsManager.createUser(userDetails);
    }

    public void authenticate(
            @NotNull final String username,
            @NotNull final String password
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void unauthenticated(
            @NotNull final String username,
            @NotNull final String password
    ) {
        this.authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, password));
    }

    public List<GrantedAuthority> getAuthorities(@NotNull final Set<Role> roles) {
        Objects.requireNonNull(roles);
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }



    public Optional<Role> getLoggedUserRole() {
        return getLoggedUserAuthority()
                .stream()
                .map(Role::authorityToRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public Set<String> getLoggedUserAuthority() {
        AtomicReference<Set<String>> atomicReference = new AtomicReference<>(new HashSet<>());
        getUserDetails().ifPresent(userDetails ->
                atomicReference.set(userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
        );
        return atomicReference.get();
    }



    @Transactional
    public void updatePassAndRole(
            @NotNull final String username,
            @NotNull final Set<Role> roles,
            @NotNull final String currentPass,
            @NotNull final String newPass
    ) {
        Objects.requireNonNull(newPass);
        Objects.requireNonNull(currentPass);
        Objects.requireNonNull(roles);
        Objects.requireNonNull(username);

        getUserDetails().ifPresentOrElse(userDetails -> {
            unauthenticated(username, currentPass);

            UserDetails newUserDetails = User.builder()
                    .username(username)
                    .authorities(getAuthorities(roles))
                    .password(passwordEncoder.encode(newPass))
                    .build();

            jdbcUserDetailsManager.updateUser(newUserDetails);

            authenticate(username, newPass);
        }, () -> {
            throw new RuntimeException("No person");
        });
    }
}
