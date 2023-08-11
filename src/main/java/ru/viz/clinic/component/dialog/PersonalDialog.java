package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import ru.viz.clinic.data.Gender;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.model.PersonalDTO;
import ru.viz.clinic.data.repository.CommonRepository;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.service.AbstractService;
import ru.viz.clinic.validator.PhoneValidator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class PersonalDialog<T extends PersonalDTO, P extends Personal, R extends CommonRepository<P>> extends VizConfirmDialog<T> {
    public static final int WORD_LENGTH = 4;
    private final FormLayout formLayout;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final DatePicker birthDateField;
    private final TextField userField;
    private final PasswordField passwordField;
    private final TextField phoneField;
    private final TextField emailField;
    private final RadioButtonGroup<Gender> genderField;

    public PersonalDialog(
            @NotNull final T t,
            @NotNull final AbstractService<P, R, T> abstractService,
            @NotNull final String title
    ) {
        super(Objects.requireNonNull(title), Objects.requireNonNull(t));

        Objects.requireNonNull(abstractService);

        genderField = new RadioButtonGroup<>(LBL_GENDER);
        firstNameField = new TextField(LBL_FIRST_NAME);
        lastNameField = new TextField(LBL_LAST_NAME);
        birthDateField = new DatePicker(LBL_BIRTHDAY);
        userField = new TextField(LBL_USER);
        passwordField = new PasswordField(LBL_PASSWORD);
        phoneField = new TextField(LBL_PHONE);
        emailField = new TextField(LBL_EMAIL);

        emailField.setValueChangeMode(ValueChangeMode.EAGER);
        phoneField.setValueChangeMode(ValueChangeMode.EAGER);
        userField.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField.setValueChangeMode(ValueChangeMode.EAGER);

        genderField.setItems(Gender.values());
        genderField.setItemLabelGenerator(value -> {
            if (value != null) {
                return value.getGenderAsString();
            }
            return StringUtils.EMPTY;
        });

        binder.forField(userField)
                .asRequired()
                .withValidator(s -> abstractService.findByUsername(s).isEmpty(), ERR_MSG_USER_NAME_BUSY,
                        ErrorLevel.ERROR)
                .withValidator(s -> s.length() >= WORD_LENGTH, ERR_MSG_USER_NAME_IS_SHORT, ErrorLevel.WARNING)
                .bind(PersonalDTO::getUsername, PersonalDTO::setUsername);
        binder.forField(passwordField)
                .asRequired()
                .withValidator(s -> s.length() >= WORD_LENGTH, ERR_MSG_PASS_NAME_IS_SHORT, ErrorLevel.WARNING)
                .bind(PersonalDTO::getPassword, PersonalDTO::setPassword);
        binder.forField(firstNameField).asRequired().bind(PersonalDTO::getFirstName, PersonalDTO::setFirstName);
        binder.forField(lastNameField).asRequired().bind(PersonalDTO::getLastName, PersonalDTO::setLastName);
        binder.forField(genderField).bind(PersonalDTO::getGender, PersonalDTO::setGender);
        binder.forField(birthDateField)
                .withValidator(localDate -> localDate == null || localDate.isBefore(LocalDate.now()),
                        ERR_MSG_BIRTHDAY_IS_AFTER_NOW,
                        ErrorLevel.ERROR)
                .bind(PersonalDTO::getBirthDate,
                        PersonalDTO::setBirthDate);
        binder.forField(phoneField).withValidator(new PhoneValidator(ERR_MSG_PHONE_IS_INVALID, true))
                .bind(PersonalDTO::getPhone, PersonalDTO::setPhone);
        binder.forField(emailField).asRequired().withValidator(new EmailValidator(ERR_MSG_EMAIL_IS_INVALID, true))
                .bind(PersonalDTO::getEmail, PersonalDTO::setEmail);

        binder.readBean(t);

        formLayout = new FormLayout();
        this.add(formLayout);
    }

    private void addFields() {
        formLayout.add(userField, passwordField, firstNameField,
                lastNameField, birthDateField, genderField, emailField, phoneField);
    }

    protected void addComponents(@NotNull final Component... components) {
        Arrays.stream(components).forEach(formLayout::add);
        addFields();
    }
}