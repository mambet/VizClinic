package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.model.DepartmentGridFilterUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;
import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class MedicGrid extends PersonGrid<Medic> implements DepartmentGridFilterUpdater {
    public MedicPersonFilter medicPersonFilter;

    public MedicGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Medic> setItems(Collection<Medic> items) {
        GridListDataView<Medic> medicPersonalGridListDataView = super.setItems(items);
        medicPersonFilter = new MedicPersonFilter(this.getListDataView());
        return medicPersonalGridListDataView;
    }

    @Override
    public void setDepartmentFilterParameter(Department department) {
        medicPersonFilter.setDepartment(department);
    }

    @Override
    public void setHospitalFilterParameter(Hospital hospital) {
        medicPersonFilter.setHospital(hospital);
    }

    private void createTable() {
        List<Column<Medic>> columns = new ArrayList<>(this.getColumns());
        Grid.Column<Medic> departmentColumn = this.addColumn(
                        medicPersonal1 -> medicPersonal1.getDepartment().getName())
                .setHeader(HDR_DEPARTMENT);

        Grid.Column<Medic> hospitalColumn = this.addColumn(
                        medicPersonal -> medicPersonal.getDepartment().getHospital().getName())
                .setHeader(HDR_HOSPITAL);

        columns.add(0, hospitalColumn);
        columns.add(1, departmentColumn);
        this.setColumnOrder(columns);
    }

    public static class MedicPersonFilter extends PersonFilter<Medic> {
        private Department department;
        private Hospital hospital;

        public MedicPersonFilter(GridListDataView<Medic> dataView) {
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
        public boolean test(Medic person) {
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
