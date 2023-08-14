package ru.viz.clinic.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.dialog.PassChangeLayout;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.service.PersonalService;
import ru.viz.clinic.views.order.EngineerOrderView;
import ru.viz.clinic.views.order.MedicOrderView;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.PTT_CHANGE_PASS;

@PageTitle(PTT_CHANGE_PASS)
@Route(value = "RegisterTemp")
@RolesAllowed("TEMP")
public class ChangePassword extends HorizontalLayout {
    private final PersonalService personalService;


    public ChangePassword( @NotNull final PersonalService personalService) {
        this.personalService = Objects.requireNonNull(personalService);
        final PassChangeLayout passChangeLayout = new PassChangeLayout(this::attemptToRegister);
        this.add(passChangeLayout);
        this.setHeightFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setAlignSelf(Alignment.CENTER, passChangeLayout);
    }

    private void attemptToRegister(final PassChangeLayout.PassDTO passDTO) {
        personalService.updatePassAndRole(passDTO).ifPresentOrElse(role -> {
            if (role == Role.ENGINEER) {
                this.getUI().ifPresent(ui -> ui.navigate(EngineerOrderView.class));
            }
            if (role == Role.MEDIC) {
                this.getUI().ifPresent(ui -> ui.navigate(MedicOrderView.class));
            }
        },()->Helper.showErrorNotification(Translator.ERR_AUTHENTICATION));
    }
}