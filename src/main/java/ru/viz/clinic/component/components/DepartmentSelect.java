package ru.viz.clinic.component.components;

import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Department;

import java.util.Objects;
import java.util.function.Consumer;

import static ru.viz.clinic.help.Translator.LBL_DEPARTMENT_NAME;

public class DepartmentSelect extends Select<Department> {
    private DepartmentSelect() {
        this.setItemLabelGenerator(department -> department == null ? Strings.EMPTY : department.getName());
    }

    public static DepartmentSelect createDepartmentSelect() {
        final DepartmentSelect departmentSelect = new DepartmentSelect();
        departmentSelect.setLabel(LBL_DEPARTMENT_NAME);
        return departmentSelect;
    }

    public static DepartmentSelect createWithAllowEmpty(@NotNull final Consumer<Department> filterChangeConsumer) {
        final DepartmentSelect departmentSelect = new DepartmentSelect();
        departmentSelect.setEmptySelectionAllowed(true);
        Objects.requireNonNull(filterChangeConsumer);
        departmentSelect.addValueChangeListener(changeEvent -> filterChangeConsumer.accept(
                changeEvent.getValue()));
        departmentSelect.setWidthFull();
        return departmentSelect;
    }
}
