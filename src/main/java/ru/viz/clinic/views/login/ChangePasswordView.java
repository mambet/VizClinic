package ru.viz.clinic.views.login;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.dialog.PassChangeLayout;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.ChangePassService;
import ru.viz.clinic.views.engineer.EngineerOrderView;
import ru.viz.clinic.views.medic.MedicOrderView;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.PTT_CHANGE_PASS;

@PageTitle(PTT_CHANGE_PASS)
@Route(value = "RegisterTemp")
@RolesAllowed("TEMP")
public class ChangePasswordView extends HorizontalLayout {
    private final ChangePassService changePassService;

    public ChangePasswordView(@NotNull final ChangePassService changePassService) {
        this.changePassService = Objects.requireNonNull(changePassService);
        final PassChangeLayout passChangeLayout = new PassChangeLayout(this::attemptToRegister);
        this.add(passChangeLayout);
        this.setHeightFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setAlignSelf(Alignment.CENTER, passChangeLayout);
    }

    private void attemptToRegister(final PassChangeLayout.PassDTO passDTO) {
        changePassService.updatePassAndRole(passDTO).ifPresentOrElse(role -> {
            if (role == Role.ENGINEER) {
                this.getUI().ifPresent(ui -> ui.navigate(EngineerOrderView.class));
            }
            if (role == Role.MEDIC) {
                this.getUI().ifPresent(ui -> ui.navigate(MedicOrderView.class));
            }
        },()->Helper.showErrorNotification(Translator.ERR_AUTHENTICATION));
    }
}