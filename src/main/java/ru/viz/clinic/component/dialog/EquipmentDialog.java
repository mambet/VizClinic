package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Equipment;

import java.time.LocalDate;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class EquipmentDialog extends VizConfirmDialog<Equipment> {
    protected final FormLayout formLayout;

    public EquipmentDialog(@NotNull final Equipment equipment) {
        super(DLH_CREATE_EQUIPMENT, Objects.requireNonNull(equipment));

        final TextField name = new TextField(LBL_EQUIPMENT_NAME);
        final TextField inventoryNumber = new TextField(LBL_EQUIPMENT_INVENTORY_NUMBER);
        final TextField factoryNumber = new TextField(LBL_EQUIPMENT_FACTORY_NUMBER);
        final DatePicker createDate = new DatePicker(LBL_EQUIPMENT_CREATE_DATE);
        final DatePicker commissioningDate = new DatePicker(LBL_EQUIPMENT_COMMISSIONING_DATE);
        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);

        name.setValueChangeMode(ValueChangeMode.EAGER);
        inventoryNumber.setValueChangeMode(ValueChangeMode.EAGER);
        factoryNumber.setValueChangeMode(ValueChangeMode.EAGER);
        description.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(name).asRequired().bind(Equipment::getName, Equipment::setName);
        binder.forField(inventoryNumber).asRequired()
                .bind(Equipment::getInventoryNumber, Equipment::setInventoryNumber);
        binder.forField(factoryNumber).asRequired().bind(Equipment::getFactoryNumber, Equipment::setFactoryNumber);
        binder.forField(createDate)
                .withValidator(localDate -> localDate == null || localDate.isBefore(LocalDate.now()),
                        ERR_MSG_CREATION_DATE_IS_AFTER_NOW,
                        ErrorLevel.ERROR)
                .bind(Equipment::getCreateDate, Equipment::setCreateDate);

        binder.forField(commissioningDate)
                .withValidator(localDate -> localDate == null || localDate.isBefore(LocalDate.now()),
                        ERR_MSG_CREATION_DATE_IS_AFTER_NOW,
                        ErrorLevel.ERROR)
                .bind(Equipment::getCommissioningDate, Equipment::setCommissioningDate);

        binder.forField(description).bind(Equipment::getDescription, Equipment::setDescription);
        setBtnConfirmEnable(binder.isValid());
        formLayout = new FormLayout();
        formLayout.add(name, factoryNumber, inventoryNumber, createDate, commissioningDate, description);
        this.add(formLayout);
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateEquipmentEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateEquipmentEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class CreateEquipmentEvent extends AbstractDialogEvent<EquipmentDialog, Equipment> {
        public CreateEquipmentEvent(
                @NotNull final EquipmentDialog source,
                @NotNull final Equipment equipment
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(equipment));
        }
    }

    @Getter
    public static class UpdateEquipmentEvent extends AbstractDialogEvent<EquipmentDialog, Equipment> {
        public UpdateEquipmentEvent(
                @NotNull final EquipmentDialog source,
                @NotNull final Equipment equipment
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(equipment));
        }
    }
}
