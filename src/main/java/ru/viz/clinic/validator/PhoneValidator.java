package ru.viz.clinic.validator;

import com.vaadin.flow.data.validator.RegexpValidator;

public class PhoneValidator extends RegexpValidator {

    private static final String PATTERN = "^(((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10})?$";
    private final boolean allowEmptyValue;

    public PhoneValidator(final String errorMessage) {
        this(errorMessage, false);
    }

    public PhoneValidator(
            final String errorMessage,
            final boolean allowEmpty
    ) {
        super(errorMessage, "^(((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10})?$", true);
        this.allowEmptyValue = allowEmpty;
    }

    protected boolean isValid(final String value) {
        return this.allowEmptyValue && value != null && value.isEmpty() || super.isValid(value);
    }
}
