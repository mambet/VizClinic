package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.MedicPersonal;
import ru.viz.clinic.data.model.DepartmentGridUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;
import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class MedicPersonalGrid extends PersonGrid<MedicPersonal> implements DepartmentGridUpdater {
    public MedicPersonFilter medicPersonFilter;

    public MedicPersonalGrid() {
        createTable();

    }
    @Override
    public GridListDataView<MedicPersonal> setItems(Collection<MedicPersonal> items) {
        GridListDataView<MedicPersonal> medicPersonalGridListDataView = super.setItems(items);
        medicPersonFilter = new MedicPersonFilter(this.getListDataView());
        return medicPersonalGridListDataView;
    }

    @Override
    public void updateDepartment(Optional<Department> optionalDepartment) {
        optionalDepartment.ifPresentOrElse(medicPersonFilter::setDepartment,
                () -> medicPersonFilter.setDepartment(null));
    }

    @Override
    public void updateHospital(Optional<Hospital> optionalHospital) {
        optionalHospital.ifPresentOrElse(medicPersonFilter::setHospital,
                () -> medicPersonFilter.setHospital(null));
    }

    private void createTable() {
        this.setSelectionMode(Grid.SelectionMode.NONE);
        List<Column<MedicPersonal>> columns = new ArrayList<>(this.getColumns());
        Grid.Column<MedicPersonal> departmentColumn = this.addColumn(
                        medicPersonal1 -> medicPersonal1.getDepartment().getName())
                .setHeader(HDR_DEPARTMENT);

        Grid.Column<MedicPersonal> hospitalColumn = this.addColumn(
                        medicPersonal -> medicPersonal.getDepartment().getHospital().getName())
                .setHeader(HDR_HOSPITAL);

        columns.add(0, hospitalColumn);
        columns.add(1, departmentColumn);
        this.setColumnOrder(columns);
    }

    public static class MedicPersonFilter extends PersonFilter<MedicPersonal> {
        private Department department;
        private Hospital hospital;

        public MedicPersonFilter(GridListDataView<MedicPersonal> dataView) {
            super(dataView);
        }

        public void setDepartment(Department department) {
            this.department = department;
            this.dataView.refreshAll();
        }

        public void setHospital(Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        @Override
        public boolean test(MedicPersonal person) {
            boolean superTest = super.test(person);
            boolean departmentNull = person.getDepartment() == null || department == null;
            boolean hospitalNull = person.getDepartment() == null
                    || person.getDepartment().getHospital() == null
                    || hospital == null;

            boolean departmentMatches = departmentNull || Objects.equals(person.getDepartment().getId(),
                    department.getId());
            boolean hospitalMatches = hospitalNull || Objects.equals(person.getDepartment().getHospital().getId(),
                    hospital.getId());

            return departmentMatches && hospitalMatches && superTest;
        }
    }
}
