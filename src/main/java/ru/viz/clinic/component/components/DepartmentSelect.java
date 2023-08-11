package ru.viz.clinic.component.components;

import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;

import static ru.viz.clinic.help.Translator.LBL_DEPARTMENT_NAME;
import static ru.viz.clinic.help.Translator.LBL_HOSPITAL;

public class DepartmentSelect extends Select<Department> {
    public DepartmentSelect() {
        this.setLabel(LBL_DEPARTMENT_NAME);
        this.setItemLabelGenerator(Department::getName);
    }
}
