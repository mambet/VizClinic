package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
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

        Select<Hospital> hospitalSelect = new Select<>();
        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        final TextField departmentName = new TextField(LBL_DEPARTMENT_NAME);

        binder.forField(departmentName).asRequired().bind(Department::getName, Department::setName);
        binder.forField(hospitalSelect).asRequired().bind(Department::getHospital, Department::setHospital);

        this.add(new FormLayout(hospitalSelect, departmentName));
    }
    @Override
    protected void firePersonalEvent() {
        fireEvent(new UpdateDepartmentEvent(this, Objects.requireNonNull(item)));
    }
    public DepartmentDialog(@NotNull final List<Hospital> hospitals) {
        this(new Department(), hospitals);
    }

    @Getter
    public static class UpdateDepartmentEvent extends ComponentEvent<DepartmentDialog> {
        private final Department department;

        public UpdateDepartmentEvent(
                @NotNull final DepartmentDialog source,
                @NotNull final Department department
        ) {
            super(Objects.requireNonNull(source), true);
            this.department = department;
        }
    }
}