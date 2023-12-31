package ru.viz.clinic.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.admin.AdminView;
import ru.viz.clinic.views.engineer.EngineerOrderView;
import ru.viz.clinic.views.medic.MedicOrderView;

import java.util.Objects;

@PageTitle("main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@AnonymousAllowed
public class MainView extends Div implements BeforeEnterObserver {
    private final AuthenticationService authenticationService;

    public MainView(@NotNull final AuthenticationService authenticationService) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
    }

    @Override
    public void beforeEnter(final BeforeEnterEvent event) {
        //if user has role ENGINEER reroute to ChangePasswordView
        authenticationService.getUserDetails()
                .flatMap(userDetails -> userDetails.getAuthorities().stream().filter(grantedAuthority ->
                                StringUtils.equals(grantedAuthority.getAuthority(), Role.ENGINEER.getAuthority()))
                        .findFirst()).ifPresent(grantedAuthority -> event.forwardTo(EngineerOrderView.class));

        //if user has role MEDIC reroute to ChangePasswordView
        authenticationService.getUserDetails()
                .flatMap(userDetails -> userDetails.getAuthorities().stream().filter(grantedAuthority ->
                                StringUtils.equals(grantedAuthority.getAuthority(), Role.MEDIC.getAuthority()))
                        .findFirst()).ifPresent(grantedAuthority -> event.forwardTo(MedicOrderView.class));

        //if user has role ADMIN reroute to ChangePasswordView
        authenticationService.getUserDetails()
                .flatMap(userDetails -> userDetails.getAuthorities().stream().filter(grantedAuthority ->
                                StringUtils.equals(grantedAuthority.getAuthority(), Role.ADMIN.getAuthority()))
                        .findFirst()).ifPresent(grantedAuthority -> event.forwardTo(AdminView.class));
    }
}
