package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.help.Helper;

import java.util.List;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class DepartmentDialog extends VizConfirmDialog {
    private final Binder<Department> binder = new BeanValidationBinder<>(Department.class);
    private final Department department;

    public DepartmentDialog(
            @NotNull final Department department,
            @NotNull final List<Hospital> hospitals
    ) {
        super(DLH_CREATE_DEPARTMENT);

        this.department = Objects.requireNonNull(department);
        Objects.requireNonNull(hospitals);

        setConfirmText(BTN_CONFIRM_CREATE);
        setCancelText(BTN_CANCEL);

        Select<Hospital> hospitalSelect = new Select<>();
        hospitalSelect.setItems(hospitals);
        hospitalSelect.setItemLabelGenerator(Hospital::getName);

        final TextField hospitalName = new TextField(LBL_FIRST_NAME);

        hospitalName.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(hospitalName).asRequired().bind(Department::getName, Department::setName);
        binder.forField(hospitalSelect).asRequired()
                .bind(Department::getHospital, Department::setHospital);
        binder.setBean(department);

        setBinder(binder);
        setBtnConfirmEnable(binder.isValid());

        this.add(new FormLayout(hospitalSelect, hospitalName));
    }

    public DepartmentDialog(@NotNull final List<Hospital> hospitals) {
        this(new Department(), hospitals);
    }

    @Override
    void confirmListener(ConfirmEvent confirmEvent) {
        if (binder.isValid()) {
            binder.writeBeanIfValid(department);
            fireEvent(new UpdateHospitalDepartmentEvent(this, binder.getBean()));
            this.close();
        } else {
            Helper.showErrorNotification(ERR_MSG_INVALID_DATA);
        }
    }

    @Getter
    public static class UpdateHospitalDepartmentEvent extends ComponentEvent<DepartmentDialog> {
        private final Department department;

        public UpdateHospitalDepartmentEvent(
                @NotNull final DepartmentDialog source,
                @NotNull final Department department
        ) {
            super(Objects.requireNonNull(source), true);
            this.department = department;
        }
    }
}
