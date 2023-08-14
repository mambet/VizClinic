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

import static ru.viz.clinic.help.Translator.*;

@AnonymousAllowed
@PageTitle(PTT_LOGIN)
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
    private final AuthenticationService authenticationService;

    public LoginView(@NotNull final AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService);

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.getErrorMessage().setTitle(ERR_MSG_LOGIN_TITLE);
        i18n.getErrorMessage().setMessage(ERR_MSG_LOGIN_FAILED);
        i18n.setHeader(new LoginI18n.Header());
        i18n.getForm().setPassword(LBL_LOGIN_PASS);
        i18n.getForm().setUsername(LBL_LOGIN_USERNAME);
        i18n.getForm().setSubmit(BTN_LOGIN);
        i18n.getForm().setTitle(LBL_LOGIN_TEXT);
        i18n.getHeader().setTitle(DLH_LOGIN_TITLE);
        i18n.getHeader().setDescription(MSG_LOGIN);
        i18n.setAdditionalInformation(null);

        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);

        this.addLoginListener(this::login);
    }

    private void login(final LoginEvent loginEvent) {
        authenticationService.authenticate(loginEvent.getUsername(), loginEvent.getPassword());
    }

    @Override
    public void beforeEnter(final BeforeEnterEvent beforeEnterEvent) {
        if (authenticationService.isUserLoggedIn()) {
            setOpened(false);
            beforeEnterEvent.forwardTo("");
        }
        setError(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
