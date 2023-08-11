package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

import static ru.viz.clinic.help.Translator.*;

public class PassChangeLayout extends VerticalLayout {
    public static final int WORD_LENGTH = 4;
    private final Consumer<PassDTO> passDTOConsumer;
    private final Binder<PassDTO> binder = new Binder<>();
    private final PasswordField oldPasswordField;
    private final PasswordField newPasswordField;
    private final PasswordField newPasswordFieldRepeat;
    private final Binder.Binding<PassDTO, String> bindNew;
    private final Binder.Binding<PassDTO, String> bindRepeat;

    public PassChangeLayout(
            @NotNull final Consumer<PassDTO> passDTOConsumer
    ) {
        this.passDTOConsumer = Objects.requireNonNull(passDTOConsumer);

        oldPasswordField = new PasswordField("Актуальный пороль");
        newPasswordField = new PasswordField("Новый пароль");
        newPasswordFieldRepeat = new PasswordField("Повторить пароль");
        final Button confirm = new Button("Подтвердить", this::handleConfirm);

        binder.forField(oldPasswordField)
                .asRequired()
                .bind(PassDTO::getOldPass, PassDTO::setOldPass);
        bindNew = binder.forField(newPasswordField)
                .asRequired()
                .withValidator(this::validateNewPassEquals)
                .bind(PassDTO::getNewPass, PassDTO::setNewPass);
        bindRepeat = binder.forField(newPasswordFieldRepeat)
                .asRequired()
                .withValidator(this::validateNewRepeatPassEquals)
                .bind(PassDTO::getNewPassRepeat, PassDTO::setNewPassRepeat);
        binder.setBean(new PassDTO());

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        //        formLayout.setWidth(300, Unit.PIXELS);
        //        formLayout.setHeight(300, Unit.PIXELS);

        formLayout.add(oldPasswordField, newPasswordField, newPasswordFieldRepeat);

        this.setWidth(300, Unit.PIXELS);
        H3 label = new H3(DLH_CHANGE_PASS);
        this.setHorizontalComponentAlignment(Alignment.END, confirm);
        this.setHorizontalComponentAlignment(Alignment.CENTER, label);

        this.add(label, formLayout, confirm);

        newPasswordField.addValueChangeListener(this::validateRepeat);
        newPasswordFieldRepeat.addValueChangeListener(this::validateNew);
    }

    private void validateNew(AbstractField.ComponentValueChangeEvent<PasswordField, String> passwordFieldStringComponentValueChangeEvent) {
        bindNew.validate();
    }

    private void validateRepeat(AbstractField.ComponentValueChangeEvent<PasswordField, String> passwordFieldStringComponentValueChangeEvent) {
        bindRepeat.validate();
    }

    private ValidationResult validateNewPassEquals(
            @NotNull final String newPass,
            @NotNull final ValueContext valueContext
    ) {

        if (newPass.length() < WORD_LENGTH) {
            return ValidationResult.error(ERR_MSG_PASS_NAME_IS_SHORT);
        }
        if (StringUtils.equals(newPasswordFieldRepeat.getValue(), newPass)) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error(ERR_PASSWORDS_NOT_EQUALS_ERROR);
        }
    }

    private ValidationResult validateNewRepeatPassEquals(
            @NotNull final String repeatPass,
            @NotNull final ValueContext valueContext
    ) {
        if (repeatPass.length() < WORD_LENGTH) {
            return ValidationResult.error(ERR_MSG_PASS_NAME_IS_SHORT);
        }
        if (StringUtils.equals(newPasswordField.getValue(), repeatPass)) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error(ERR_PASSWORDS_NOT_EQUALS_ERROR);
        }
    }

    private void handleConfirm(ClickEvent<Button> buttonClickEvent) {
        if (binder.isValid()) {
            passDTOConsumer.accept(binder.getBean());
        }
    }

    @Getter
    @Setter
    public static class PassDTO {
        String oldPass;
        String newPass;
        String newPassRepeat;
    }
}
