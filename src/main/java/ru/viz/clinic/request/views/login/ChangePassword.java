package ru.viz.clinic.request.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.viz.clinic.request.data.service.AuthorityRepository;
import ru.viz.clinic.request.data.service.UserService;
import ru.viz.clinic.request.security.AuthenticatedUser;


@PageTitle("RegisterTemp")
@Route(value = "RegisterTemp")
@RolesAllowed("TEMP")
public class ChangePassword extends VerticalLayout {


    //    private final AuthenticationManager authenticationProvider;
//    private final UserRepository repository;
//    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;

    private final AuthorityRepository authorityRepository;
    private final AuthenticatedUser authenticatedUser;

    private TextField password_1;
    private TextField password_2;

    final UserService userService;


    public ChangePassword(PasswordEncoder passwordEncoder,
                          UserDetailsManager userDetailsManager, AuthorityRepository authorityRepository, AuthenticatedUser authenticatedUser, UserService userService) {
//        this.authenticationProvider = authenticationProvider;
//        this.repository = repository;
//        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.authorityRepository = authorityRepository;
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;


        Button confirm = new Button("conf");

        confirm.addClickListener(this::attemptToRegister);

        password_1 = new TextField();
        password_2 = new TextField();

        this.add(password_1, password_2, confirm);
    }

    private void attemptToRegister(ClickEvent<Button> buttonClickEvent) {
        authenticatedUser.getUserDetails().ifPresent(user -> {

            userService.updateUserDetails(user, User.builder()
                    .username(user.getUsername())
                    .authorities("ROLE_ADMIN")
                    .password(passwordEncoder.encode(password_2.getValue()))
                    .build(),password_1.getValue());
        });
    }

    private void attemptToRegister(AttachEvent attachEvent) {

    }
}
