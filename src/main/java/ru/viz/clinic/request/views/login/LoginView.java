package ru.viz.clinic.request.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticationManager authenticationManager;
//    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
//        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("VizClinik");
        i18n.getHeader().setDescription("Login using user/user or admin/admin");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);


        setForgotPasswordButtonVisible(false);
        setOpened(true);

        this.addLoginListener(this::login);
    }

    private void login(LoginEvent loginEvent) {
        attemptLogin(loginEvent.getUsername(), loginEvent.getPassword());
    }


    private void attemptLogin(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getName());

    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
