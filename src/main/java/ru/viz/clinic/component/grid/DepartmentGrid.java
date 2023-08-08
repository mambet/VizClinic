package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.HospitalGridFilterUpdater;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;
import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class DepartmentGrid extends Grid<Department> implements HospitalGridFilterUpdater {
    private DepartmentFilter departmentFilter;

    public DepartmentGrid() {
        createTable();
    }

    @Override
    public void setHospitalFilterParameter(Hospital hospital) {
        departmentFilter.setHospital(hospital);
        this.getListDataView().refreshAll();
    }

    private void createTable() {
        this.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.addColumn(Department::getId).setHeader(HDR_DEPARTMENT);
        this.addColumn(Department::getName).setHeader(HDR_DEPARTMENT);
        this.addColumn(department -> department.getHospital() != null ? department.getHospital().getName() :
                Strings.EMPTY).setHeader(HDR_HOSPITAL);
        this.setAllRowsVisible(true);
        this.addClassName("select");
        this.addClassName("primary");
    }

    @Override
    public GridListDataView<Department> setItems(Collection<Department> items) {
        GridListDataView<Department> departmentGridListDataView = super.setItems(items);
        departmentFilter = new DepartmentFilter(this.getListDataView());
        return departmentGridListDataView;
    }

    public static class DepartmentFilter {
        public final GridListDataView<Department> dataView;
        private Hospital hospital;

        public DepartmentFilter(GridListDataView<Department> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setHospital(Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        private boolean test(Department department) {
            return department.getHospital() == null
                    || hospital == null
                    || Objects.equals(department.getHospital().getId(), hospital.getId());
        }

        private boolean matches(
                String value,
                String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
