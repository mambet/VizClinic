package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
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
    public GridListDataView<Medic> setItems(final Collection<Medic> items) {
        final GridListDataView<Medic> medicPersonalGridListDataView = super.setItems(items);
        medicPersonFilter = new MedicPersonFilter(this.getListDataView());
        return medicPersonalGridListDataView;
    }

    @Override
    public void setDepartmentFilterParameter(final Department department) {
        medicPersonFilter.setDepartment(department);
    }

    @Override
    public void setHospitalFilterParameter(final Hospital hospital) {
        medicPersonFilter.setHospital(hospital);
    }

    private void createTable() {
        final List<Column<Medic>> columns = new ArrayList<>(this.getColumns());
        final Grid.Column<Medic> departmentColumn = this.addColumn(medic -> {
                    if (medic.getDepartment() != null) {
                        return medic.getDepartment().getName();
                    } else {
                        return Strings.EMPTY;
                    }
                }).setHeader(HDR_DEPARTMENT)
                .setAutoWidth(true)
                .setResizable(true);

        final Grid.Column<Medic> hospitalColumn =
                this.addColumn(medic -> {
                            if (medic.getDepartment() != null && medic.getDepartment().getHospital() != null) {
                                return medic.getDepartment().getHospital().getName();
                            } else {
                                return Strings.EMPTY;
                            }
                        })
                        .setHeader(HDR_HOSPITAL)
                        .setAutoWidth(true)
                        .setResizable(true);

        columns.add(1, hospitalColumn);
        columns.add(2, departmentColumn);

        this.setColumnOrder(columns);
    }

    public static class MedicPersonFilter extends PersonFilter<Medic> {
        private Department department;
        private Hospital hospital;

        public MedicPersonFilter(final GridListDataView<Medic> dataView) {
            super(dataView);
        }

        public void setDepartment(final Department department) {
            this.department = department;
            this.dataView.refreshAll();
        }

        public void setHospital(final Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        @Override
        public boolean test(final Medic person) {
            final boolean superTest = super.test(person);
            final boolean departmentNull = person.getDepartment() == null || department == null;
            final boolean hospitalNull = person.getDepartment() == null
                    || person.getDepartment().getHospital() == null
                    || hospital == null;
            final boolean departmentMatches = departmentNull || Objects.equals(person.getDepartment().getId(),
                    department.getId());
            final boolean hospitalMatches = hospitalNull || Objects.equals(person.getDepartment().getHospital().getId(),
                    hospital.getId());

            return departmentMatches && hospitalMatches && superTest;
        }
    }

    @Override
    protected void updateEntity(final Medic medic) {
        fireEvent(new UpdateMedicGridEvent(this, medic));
    }

    @Override
    protected void deleteEntity(final Medic medic) {
        fireEvent(new DeleteMedicGridEvent(this, medic));
    }


    public static class DeleteMedicGridEvent extends AbstractGridEvent<MedicGrid, Medic> {
        protected DeleteMedicGridEvent(
                @NotNull final MedicGrid source,
                @NotNull final Medic entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateMedicGridEvent extends AbstractGridEvent<MedicGrid, Medic> {
        protected UpdateMedicGridEvent(
                @NotNull final MedicGrid source,
                @NotNull final Medic entity
        ) {
            super(source, entity);
        }
    }
}
