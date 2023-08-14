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
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.service.DepartmentService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class AdminEquipmentDialog extends EquipmentDialog {
    private final DepartmentSelect departmentSelect = new DepartmentSelect();
    private final DepartmentService departmentService;

    public AdminEquipmentDialog(
            @NotNull final Equipment equipment,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        super(equipment);

        this.departmentService = Objects.requireNonNull(departmentService);
        final HospitalSelect hospitalSelect = new HospitalSelect(Objects.requireNonNull(hospitals));

        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);
        binder.forField(departmentSelect).asRequired().bind(Equipment::getDepartment, Equipment::setDepartment);

        formLayout.addComponentAtIndex(0, hospitalSelect);
        formLayout.addComponentAtIndex(1, departmentSelect);
    }

    public AdminEquipmentDialog(
            @NotNull final List<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        this(new Equipment(),
                Objects.requireNonNull(hospitals),
                Objects.requireNonNull(departmentService));
    }

    private void hospitalSelectListener(final AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final Long hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        departmentSelect.setItems(departmentService.getByHospital(hospitalId));
    }
}
