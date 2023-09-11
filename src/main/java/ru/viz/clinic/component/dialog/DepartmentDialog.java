package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Department;

import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class DepartmentDialog extends VizConfirmDialog<Department> {
    private DepartmentDialog(
            @NotNull final Department department,
            @NotNull final List<Hospital> hospitals
    ) {
        super(DLH_CREATE_DEPARTMENT, department);

        Objects.requireNonNull(hospitals);

        final HospitalSelect hospitalSelect = HospitalSelect.createHospitalSelect(hospitals);
        final TextField departmentName = new TextField(LBL_DEPARTMENT_NAME);

        departmentName.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(hospitalSelect).asRequired().bind(Department::getHospital, Department::setHospital);
        binder.forField(departmentName).asRequired().bind(Department::getName, Department::setName);
        binder.readBean(department);

        this.add(new FormLayout(hospitalSelect, departmentName));

        super.initUpdate();
    }

    public static DepartmentDialog getCreateDialog(@NotNull final List<Hospital> hospitals) {
        Objects.requireNonNull(hospitals);
        final DepartmentDialog departmentCreateDialog = new DepartmentDialog(new Department(), hospitals);
        departmentCreateDialog.initCreate();
        return departmentCreateDialog;
    }

    public static DepartmentDialog getUpdateDialog(
            @NotNull final Department department,
            @NotNull final List<Hospital> hospitals
    ) {
        Objects.requireNonNull(department);
        Objects.requireNonNull(hospitals);
        final DepartmentDialog departmentCreateDialog = new DepartmentDialog(department, hospitals);
        departmentCreateDialog.initUpdate();
        return departmentCreateDialog;
    }

    @Override
    protected void handleCreate() {
        fireEvent(new CreateDepartmentEvent(this, Objects.requireNonNull(item)));
    }

    @Override
    protected void handleUpdate() {
        fireEvent(new UpdateDepartmentEvent(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class CreateDepartmentEvent extends AbstractDialogEvent<DepartmentDialog, Department> {
        public CreateDepartmentEvent(
                @NotNull final DepartmentDialog source,
                @NotNull final Department department
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(department));
        }
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