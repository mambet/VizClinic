package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.help.Helper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class EquipmentDialog extends VizConfirmDialog<Equipment> {
    final Select<Department> departmentSelect = new Select<>();

    public EquipmentDialog(
            @NotNull final Equipment equipment,
            @NotNull final Collection<Hospital> hospitals
    ) {
        super(DLH_CREATE_DEPARTMENT, equipment);

        Objects.requireNonNull(hospitals);

        final Select<Hospital> hospitalSelect = new Select<>();

        hospitalSelect.setItems(hospitals);
        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        departmentSelect.setLabel(LBL_EQUIPMENT_NAME);
        departmentSelect.setItemLabelGenerator(Department::getName);

        final TextField name = new TextField(LBL_EQUIPMENT_NAME);
        final TextField number = new TextField(LBL_EQUIPMENT_NUMBER);
        final TextField numberNext = new TextField(LBL_EQUIPMENT_NUMBER_NEXT);
        final DatePicker createDate = new DatePicker(LBL_EQUIPMENT_CREATE_DATE);
        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);

        binder.forField(departmentSelect).asRequired().bind(Equipment::getDepartment, Equipment::setDepartment);
        binder.forField(name).asRequired().bind(Equipment::getName, Equipment::setName);
        binder.forField(number).asRequired().bind(Equipment::getNumber, Equipment::setNumber);
        binder.forField(numberNext).bind(Equipment::getNumberNext, Equipment::setNumberNext);
        binder.forField(createDate).bind(Equipment::getCreateDate, Equipment::setCreateDate);
        binder.forField(description).bind(Equipment::getDescription, Equipment::setDescription);

        setBtnConfirmEnable(binder.isValid());
        binder.readBean(equipment);
        this.add(new FormLayout(hospitalSelect, departmentSelect, name, number, numberNext, description));
    }

    private void hospitalSelectListener(AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        departmentSelect.setItems(selectHospitalComponentValueChangeEvent.getValue().getDepartments());
    }

    public EquipmentDialog(@NotNull final List<Hospital> hospitals) {
        this(new Equipment(), hospitals);
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateEquipmentEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class UpdateEquipmentEvent extends ComponentEvent<EquipmentDialog> {
        private final Equipment equipment;

        public UpdateEquipmentEvent(
                @NotNull final EquipmentDialog source,
                @NotNull final Equipment equipment
        ) {
            super(Objects.requireNonNull(source), true);
            this.equipment = equipment;
        }
    }
}
