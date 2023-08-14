package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.HospitalGridFilterUpdater;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class DepartmentGrid extends RUDGrid<Department> implements HospitalGridFilterUpdater {
    private DepartmentFilter departmentFilter;

    public DepartmentGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Department> setItems(@NotNull final Collection<Department> items) {
        final GridListDataView<Department> departmentGridListDataView = super.setItems(Objects.requireNonNull(items));
        departmentFilter = new DepartmentFilter(this.getListDataView());
        return departmentGridListDataView;
    }

    @Override
    public void setHospitalFilterParameter(final Hospital hospital) {
        departmentFilter.setHospital(hospital);
        this.getListDataView().refreshAll();
    }

    private void createTable() {
        this.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.addColumn(Department::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(department -> department.getHospital() != null
                ? department.getHospital().getName() : Strings.EMPTY).setHeader(HDR_HOSPITAL).setAutoWidth(true);
        this.addColumn(Department::getName).setHeader(HDR_DEPARTMENT).setAutoWidth(true);
        super.addRUDButtons();
        this.setAllRowsVisible(true);
        this.addClassNames("select", "primary");
    }

    public static class DepartmentFilter {
        public final GridListDataView<Department> dataView;
        private Hospital hospital;

        public DepartmentFilter(@NotNull final GridListDataView<Department> dataView) {
            this.dataView = Objects.requireNonNull(dataView);
            this.dataView.addFilter(this::test);
        }

        public void setHospital(final Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        private boolean test(final Department department) {
            return department.getHospital() == null
                    || hospital == null
                    || Objects.equals(department.getHospital().getId(), hospital.getId());
        }

        private boolean matches(
                final String value,
                final String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

    @Override
    protected void updateEntity(final Department department) {
        fireEvent(new DepartmentGrid.UpdateDepartmentGridEvent(this, department));
    }

    @Override
    protected void deleteEntity(final Department department) {
        fireEvent(new DepartmentGrid.DeleteDepartmentGridEvent(this, department));
    }


    public static class DeleteDepartmentGridEvent extends AbstractGridEvent<DepartmentGrid, Department> {
        protected DeleteDepartmentGridEvent(
                @NotNull final DepartmentGrid source,
                @NotNull final Department entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateDepartmentGridEvent extends AbstractGridEvent<DepartmentGrid, Department> {
        protected UpdateDepartmentGridEvent(
                @NotNull final DepartmentGrid source,
                @NotNull final Department entity
        ) {
            super(source, entity);
        }
    }
}
