package ru.viz.clinic.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.order.AdminOrderView;
import ru.viz.clinic.views.order.EngineerOrderView;
import ru.viz.clinic.views.login.ChangePassword;
import ru.viz.clinic.views.login.LoginView;
import ru.viz.clinic.views.order.MedicOrderView;

import java.util.Objects;

@Component
public class VaadinServiceInitListenerImpl implements VaadinServiceInitListener {
    private final AuthenticationService authenticationService;

    public VaadinServiceInitListenerImpl(@NotNull final AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    @Override
    public void serviceInit(final ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(final BeforeEnterEvent event) {
        //if user has role TEMP reroute to ChangePassword
        authenticationService.getUserDetails()
                .flatMap(userDetails -> userDetails.getAuthorities().stream().filter(grantedAuthority ->
                                StringUtils.equals(grantedAuthority.getAuthority(), Role.TEMP.getAuthority()))
                        .findFirst()).ifPresent(grantedAuthority -> event.forwardTo(ChangePassword.class));

        //if user not login in, reroute to LoginView
        if (!LoginView.class.equals(event.getNavigationTarget()) && !authenticationService.isUserLoggedIn()) {
            event.forwardTo(LoginView.class);
        }
    }
}