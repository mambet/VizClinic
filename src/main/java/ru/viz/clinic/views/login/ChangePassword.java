package ru.viz.clinic.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.about.AboutView;

import java.util.Objects;

@PageTitle("RegisterTemp")
@Route(value = "RegisterTemp")
@RolesAllowed("TEMP")
public class ChangePassword extends VerticalLayout {
    private final AuthenticationService authenticationService;
    private final TextField oldPasswordField;
    private final TextField newPasswordField;

    public ChangePassword(
            @NotNull final AuthenticationService authenticationService
    ) {
        this.authenticationService = Objects.requireNonNull(authenticationService);

        Button confirm = new Button("Подтвердить");

        confirm.addClickListener(this::attemptToRegister);

        oldPasswordField = new TextField("Актуальный пороль");
        newPasswordField = new TextField("Новый пароль");

        this.add(oldPasswordField, newPasswordField, confirm);
    }

    private void attemptToRegister(ClickEvent<Button> buttonClickEvent) {

        try {
            authenticationService.updatePassAndRole(oldPasswordField.getValue(), newPasswordField.getValue());
            this.getUI().ifPresent(ui -> ui.navigate(AboutView.class));
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("Ну удалось %s", e.getMessage()));
        }
    }
}
