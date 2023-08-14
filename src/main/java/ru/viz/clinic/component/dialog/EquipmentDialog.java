package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ErrorLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Equipment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public abstract class EquipmentDialog extends VizConfirmDialog<Equipment> {
    protected final FormLayout formLayout;

    public EquipmentDialog(@NotNull final Equipment equipment) {
        super(DLH_CREATE_EQUIPMENT, Objects.requireNonNull(equipment));

        final TextField name = new TextField(LBL_EQUIPMENT_NAME);
        final TextField number = new TextField(LBL_EQUIPMENT_NUMBER_1);
        final TextField numberNext = new TextField(LBL_EQUIPMENT_NUMBER_2);
        final DatePicker createDate = new DatePicker(LBL_EQUIPMENT_CREATE_DATE);
        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);

        binder.forField(name).asRequired().bind(Equipment::getName, Equipment::setName);
        binder.forField(number).asRequired().bind(Equipment::getNumber, Equipment::setNumber);
        binder.forField(numberNext).bind(Equipment::getNumberNext, Equipment::setNumberNext);
        binder.forField(createDate).withValidator(localDate -> localDate == null || localDate.isBefore(LocalDate.now()),
                        ERR_MSG_CREATION_DATE_IS_AFTER_NOW,
                        ErrorLevel.ERROR)
                .bind(Equipment::getCreateDate, Equipment::setCreateDate);
        binder.forField(description).bind(Equipment::getDescription, Equipment::setDescription);
        setBtnConfirmEnable(binder.isValid());
        binder.readBean(equipment);
        formLayout = new FormLayout();
        formLayout.add(name, number, numberNext, createDate, description);
        this.add(formLayout);
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateEquipmentEvent(this, Objects.requireNonNull(item)));
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
