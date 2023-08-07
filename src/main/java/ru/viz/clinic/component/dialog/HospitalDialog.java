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

public class HospitalDialog extends VizConfirmDialog<Hospital> {
    public HospitalDialog(@NotNull final Hospital hospital) {
        super(DLH_CREATE_HOSPITAL, hospital);

        final TextField hospitalName = new TextField(LBL_HOSPITAL_NAME);
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
    }

    public HospitalDialog() {
        this(new Hospital());
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateHospitalEvent(this, Objects.requireNonNull(item)));
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