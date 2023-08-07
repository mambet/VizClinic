package ru.viz.clinic.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
    private final AuthenticationService authenticationService;

    public LoginView(
            @NotNull final AuthenticationService authenticationService
    ) {
        this.authenticationService = Objects.requireNonNull(authenticationService);

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("VizClinic");
        i18n.getHeader().setDescription("Login using user/user or admin/admin");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);

        this.addLoginListener(this::login);
    }

    private void login(LoginEvent loginEvent) {
        authenticationService.authenticate(loginEvent.getUsername(), loginEvent.getPassword());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //TODO
    }
}
