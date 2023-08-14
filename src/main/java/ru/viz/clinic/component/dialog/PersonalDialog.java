package ru.viz.clinic.component.dialog;

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
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.Gender;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.repository.CommonRepository;
import ru.viz.clinic.service.AbstractService;
import ru.viz.clinic.validator.PhoneValidator;

import java.time.LocalDate;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class PersonalDialog<P extends Personal, R extends CommonRepository<P>> extends VizConfirmDialog<P> {
    public static final int WORD_LENGTH = 4;
    protected final FormLayout formLayout = new FormLayout();
    protected boolean createAuthority;

    public PersonalDialog(
            @NotNull final P personal,
            @NotNull final String title
    ) {
        super(Objects.requireNonNull(title), personal);

        final RadioButtonGroup<Gender> genderField = new RadioButtonGroup<>(LBL_GENDER);
        final TextField firstNameField = new TextField(LBL_FIRST_NAME);
        final TextField lastNameField = new TextField(LBL_LAST_NAME);
        final DatePicker birthDateField = new DatePicker(LBL_BIRTHDAY);
        final TextField phoneField = new TextField(LBL_PHONE);
        final TextField emailField = new TextField(LBL_EMAIL);

        emailField.setValueChangeMode(ValueChangeMode.EAGER);
        phoneField.setValueChangeMode(ValueChangeMode.EAGER);

        genderField.setItems(Gender.values());
        genderField.setItemLabelGenerator(value -> {
            if (value != null) {
                return value.getGenderAsString();
            }
            return StringUtils.EMPTY;
        });

        binder.forField(firstNameField).asRequired().bind(Personal::getFirstName, Personal::setFirstName);
        binder.forField(lastNameField).asRequired().bind(Personal::getLastName, Personal::setLastName);
        binder.forField(genderField).bind(Personal::getGender, Personal::setGender);
        binder.forField(birthDateField)
                .withValidator(localDate -> localDate == null || localDate.isBefore(LocalDate.now()),
                        ERR_MSG_BIRTHDAY_IS_AFTER_NOW,
                        ErrorLevel.ERROR)
                .bind(Personal::getBirthDate,
                        Personal::setBirthDate);
        binder.forField(phoneField).withValidator(new PhoneValidator(ERR_MSG_PHONE_IS_INVALID, true))
                .bind(Personal::getPhone, Personal::setPhone);
        binder.forField(emailField).asRequired().withValidator(new EmailValidator(ERR_MSG_EMAIL_IS_INVALID, true))
                .bind(Personal::getEmail, Personal::setEmail);

        formLayout.add(firstNameField, lastNameField, birthDateField, genderField, emailField, phoneField);
        this.add(formLayout);
    }

    protected void addAuthorisationFields(final AbstractService<P, R> abstractService) {
        this.createAuthority = true;

        final TextField usernameField = new TextField(LBL_USER);
        final PasswordField passwordField = new PasswordField(LBL_PASSWORD);

        formLayout.addComponentAtIndex(0, usernameField);
        formLayout.addComponentAtIndex(1, passwordField);

        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        usernameField.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(usernameField)
                .asRequired()
                .withValidator(s -> abstractService.findByUsername(s).isEmpty(), ERR_MSG_USER_NAME_BUSY,
                        ErrorLevel.ERROR)
                .withValidator(s -> s.length() >= WORD_LENGTH, ERR_MSG_USER_NAME_IS_SHORT, ErrorLevel.WARNING)
                .bind(p -> Strings.EMPTY, Personal::setUsername);

        binder.forField(passwordField)
                .asRequired()
                .withValidator(s -> s.length() >= WORD_LENGTH, ERR_MSG_PASS_NAME_IS_SHORT, ErrorLevel.WARNING)
                .bind(p -> Strings.EMPTY, Personal::setTempPass);
    }
}