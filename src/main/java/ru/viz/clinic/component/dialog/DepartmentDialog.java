package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.help.Helper;

import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class DepartmentDialog extends VizConfirmDialog<Department> {
    public DepartmentDialog(
            @NotNull final Department department,
            @NotNull final List<Hospital> hospitals
    ) {
        super(DLH_CREATE_DEPARTMENT, department);

        Objects.requireNonNull(hospitals);

        final HospitalSelect hospitalSelect = new HospitalSelect(hospitals);
        final TextField departmentName = new TextField(LBL_DEPARTMENT_NAME);

        binder.forField(hospitalSelect).asRequired().bind(Department::getHospital, Department::setHospital);
        binder.forField(departmentName).asRequired().bind(Department::getName, Department::setName);
        binder.readBean(department);

        this.add(new FormLayout(hospitalSelect, departmentName));
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateDepartmentEvent(this, Objects.requireNonNull(item)));
    }

    public DepartmentDialog(@NotNull final List<Hospital> hospitals) {
        this(new Department(), hospitals);
    }

    @Getter
    public static class UpdateDepartmentEvent extends AbstractDialogEvent<DepartmentDialog, Department> {
        public UpdateDepartmentEvent(
                @NotNull final DepartmentDialog source,
                @NotNull final Department department
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(department));
        }
    }
}