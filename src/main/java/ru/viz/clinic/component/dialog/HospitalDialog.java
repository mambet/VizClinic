package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Address;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.help.Helper;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class HospitalDialog extends VizConfirmDialog {
    private final Binder<Hospital> binder = new BeanValidationBinder<>(Hospital.class);
    private final Hospital hospital;

    public HospitalDialog(@NotNull final Hospital hospital) {
        super(DLH_CREATE_HOSPITAL);
        this.hospital = hospital;

        Objects.requireNonNull(hospital);

        setConfirmText(BTN_CONFIRM_CREATE);
        setCancelText(BTN_CANCEL);

        final TextField hospitalName = new TextField(LBL_FIRST_NAME);
        final TextField streetField = new TextField(LBL_STREET);
        final TextField cityField = new TextField(LBL_CITY);
        final IntegerField postalCodeField = new IntegerField(LBL_POSTAL_CODE);
        final TextField regionField = new TextField(LBL_REGION);

        hospitalName.setValueChangeMode(ValueChangeMode.EAGER);
        streetField.setValueChangeMode(ValueChangeMode.EAGER);
        cityField.setValueChangeMode(ValueChangeMode.EAGER);
        postalCodeField.setValueChangeMode(ValueChangeMode.EAGER);
        regionField.setValueChangeMode(ValueChangeMode.EAGER);

        this.add(new FormLayout(hospitalName, regionField, postalCodeField, cityField, streetField));

        binder.forField(hospitalName).asRequired().bind(Hospital::getName, Hospital::setName);
        binder.forField(streetField)
                .bind(item -> item.getAddress() != null ? item.getAddress().getStreet() : Strings.EMPTY,
                        (item, value) -> {
                            if (item.getAddress() == null) {
                                item.setAddress(new Address());
                            }
                            item.getAddress().setStreet(value);
                        });

        binder.forField(cityField)
                .bind(item -> item.getAddress() != null ? item.getAddress().getCity() : Strings.EMPTY,
                        (item, value) -> {
                            if (item.getAddress() == null) {
                                item.setAddress(new Address());
                            }
                            item.getAddress().setCity(value);
                        });
        binder.forField(postalCodeField)
                .bind(item -> item.getAddress() != null ? item.getAddress().getPostalCode() : null,
                        (item, value) -> {
                            if (item.getAddress() == null) {
                                item.setAddress(new Address());
                            }
                            item.getAddress().setPostalCode(value);
                        });
        binder.forField(regionField)
                .bind(item -> item.getAddress() != null ? item.getAddress().getRegion() : Strings.EMPTY,
                        (item, value) -> {
                            if (item.getAddress() == null) {
                                item.setAddress(new Address());
                            }
                            item.getAddress().setRegion(value);
                        });

        binder.readBean(hospital);
        this.setBinder(binder);
    }

    public HospitalDialog() {
        this(new Hospital());
    }



    @Override
    void confirmListener(ConfirmEvent confirmEvent) {
        if (binder.isValid()) {
            binder.writeBeanIfValid(hospital);
            fireEvent(new UpdateHospitalEvent(this, hospital));
            this.close();
        } else {
            Helper.showErrorNotification(ERR_MSG_INVALID_DATA);
        }
    }

    @Getter
    public static class UpdateHospitalEvent extends ComponentEvent<HospitalDialog> {
        private final Hospital hospital;

        public UpdateHospitalEvent(
                @NotNull final HospitalDialog source,
                @NotNull final Hospital hospital
        ) {
            super(Objects.requireNonNull(source), true);
            this.hospital = hospital;
        }
    }
}
