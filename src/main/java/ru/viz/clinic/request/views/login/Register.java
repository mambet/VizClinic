package ru.viz.clinic.request.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.viz.clinic.request.data.Role;
import ru.viz.clinic.request.data.entity.User;
import ru.viz.clinic.request.data.service.UserService;
import ru.viz.clinic.request.views.MainLayout;


@RolesAllowed("ADMIN")
@PageTitle("Register")
@Route(value = "Register", layout = MainLayout.class)
public class Register extends VerticalLayout {


//    private final AuthenticationManager authenticationProvider;
//    private final UserRepository repository;
//    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;

    private TextField user;
    private TextField password;

    final UserService userService;

    public Register(PasswordEncoder passwordEncoder,
                    UserDetailsManager userDetailsManager, UserService userService) {
//        this.authenticationProvider = authenticationProvider;
//        this.repository = repository;
//        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;

        Button confirm = new Button("conf");

        confirm.addClickListener(this::attemptToRegister);

        user = new TextField();
        password = new TextField();

        this.add(user, password, confirm);
    }

    private void attemptToRegister(ClickEvent<Button> buttonClickEvent) {
        User user1 = new User();
        user1.setUsername(user.getValue());
        user1.setPassword(passwordEncoder.encode(password.getValue()));
        user1.setEmail("test_email");
        user1.setEnabled(true);

        userService.createUser(user1, Role.TEMP);
    }

    private void attemptToRegister(AttachEvent attachEvent) {

    }
}
