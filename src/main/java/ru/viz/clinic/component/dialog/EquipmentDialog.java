package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ErrorLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.DepartmentSelect;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.service.DepartmentService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class EquipmentDialog extends VizConfirmDialog<Equipment> {
    private final DepartmentSelect departmentSelect = new DepartmentSelect();
    private final DepartmentService departmentService;

    public EquipmentDialog(
            @NotNull final Equipment equipment,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        super(DLH_CREATE_EQUIPMENT, Objects.requireNonNull(equipment));

        this.departmentService = Objects.requireNonNull(departmentService);
        Objects.requireNonNull(hospitals);

        final HospitalSelect hospitalSelect = new HospitalSelect(hospitals);
        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);

        final TextField name = new TextField(LBL_EQUIPMENT_NAME);
        final TextField number = new TextField(LBL_EQUIPMENT_NUMBER_1);
        final TextField numberNext = new TextField(LBL_EQUIPMENT_NUMBER_2);
        final DatePicker createDate = new DatePicker(LBL_EQUIPMENT_CREATE_DATE);
        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);

        binder.forField(departmentSelect).asRequired().bind(Equipment::getDepartment, Equipment::setDepartment);
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
        this.add(new FormLayout(hospitalSelect, departmentSelect, name, number, numberNext, createDate, description));
    }

    private void hospitalSelectListener(AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final Long hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        departmentSelect.setItems(departmentService.getByHospital(hospitalId));
    }

    public EquipmentDialog(
            @NotNull final List<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        this(new Equipment(), Objects.requireNonNull(hospitals), Objects.requireNonNull(departmentService));
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
