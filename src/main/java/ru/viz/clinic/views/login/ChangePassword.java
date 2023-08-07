package ru.viz.clinic.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.dialog.PassChangeLayout;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.views.order.EngineerOrderView;
import ru.viz.clinic.views.order.MedicOrderView;

import java.util.Objects;

@PageTitle("RegisterTemp")
@Route(value = "RegisterTemp")
@RolesAllowed("TEMP")
public class ChangePassword extends HorizontalLayout {
    private final AuthenticationService authenticationService;

    public ChangePassword(
            @NotNull final AuthenticationService authenticationService
    ) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
        final PassChangeLayout passChangeLayout = new PassChangeLayout(this::attemptToRegister);
        this.add(passChangeLayout);
        this.setHeightFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setAlignSelf(Alignment.CENTER, passChangeLayout);
    }

    private void attemptToRegister(@NotNull final PassChangeLayout.PassDTO passDTO) {
        Objects.requireNonNull(passDTO);
        try {
            authenticationService.updatePassAndRole(passDTO.getOldPass(), passDTO.getNewPass());
            authenticationService.getLoggedUserRole().ifPresent(role ->
            {
                if (role.equals(Role.ENGINEER)) {
                    this.getUI().ifPresent(ui -> ui.navigate(EngineerOrderView.class));
                }
                if (role.equals(Role.MEDIC)) {
                    this.getUI().ifPresent(ui -> ui.navigate(MedicOrderView.class));
                }

                //TODO create empty error view and navigate to it
            });
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("Ну удалось %s", e.getMessage()));
        }
    }
}
