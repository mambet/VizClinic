package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Address;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class HospitalDialog extends VizConfirmDialog<Hospital> {
    private HospitalDialog(
            @NotNull final Hospital hospital,
            @NotNull final String header
    ) {
        super(header, hospital);

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

        hospitalName.setValueChangeMode(ValueChangeMode.EAGER);

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

    public static HospitalDialog getUpdateDialog(@NotNull final Hospital hospital) {
        final HospitalDialog hospitalDialog = new HospitalDialog(hospital, DLH_UPDATE_HOSPITAL);
        hospitalDialog.initUpdate();
        return hospitalDialog;
    }

    public static HospitalDialog getCreateDialog() {
        final HospitalDialog hospitalDialog = new HospitalDialog(new Hospital(), DLH_CREATE_HOSPITAL);
        hospitalDialog.initCreate();
        return hospitalDialog;
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateHospitalDialogEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateHospitalDialogEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class CreateHospitalDialogEvent extends AbstractDialogEvent<HospitalDialog, Hospital> {
        public CreateHospitalDialogEvent(
                @NotNull final HospitalDialog source,
                @NotNull final Hospital hospital
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(hospital));
        }
    }

    @Getter
    public static class UpdateHospitalDialogEvent extends AbstractDialogEvent<HospitalDialog, Hospital> {
        public UpdateHospitalDialogEvent(
                @NotNull final HospitalDialog source,
                @NotNull final Hospital hospital
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(hospital));
        }
    }
}