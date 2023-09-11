package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.model.DepartmentGridFilterUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;
import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class MedicGrid extends PersonGrid<Medic> implements DepartmentGridFilterUpdater {
    public MedicPersonFilter medicPersonFilter;

    private MedicGrid() {
        createTable();
    }

    public static MedicGrid createMedicGrid() {
        return new MedicGrid();
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
        final Grid.Column<Medic> departmentColumn = addDepartmentColumn();
        final Grid.Column<Medic> hospitalColumn = addHospitalColumn();
        columns.add(1, hospitalColumn);
        columns.add(2, departmentColumn);
        this.setColumnOrder(columns);
    }

    private Column<Medic> addHospitalColumn() {
        return this.addColumn(getStyled(equipment -> {
                    if (equipment.getDepartment() == null) {
                        return null;
                    }
                    return equipment.getDepartment().getHospital();
                }
        )).setHeader(HDR_HOSPITAL).setAutoWidth(true).setResizable(true);
    }

    private Column<Medic> addDepartmentColumn() {
        return this.addColumn(getStyled(Medic::getDepartment))
                .setHeader(HDR_DEPARTMENT).setAutoWidth(true).setResizable(true);
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
    protected void updateEntity(@NotNull final Medic medic) {
        fireEvent(new UpdateGridEvent(this, medic));
    }

    @Override
    protected void deleteEntity(@NotNull final Medic medic) {
        fireEvent(new DeletGridEvent(this, medic));
    }

    @Override
    protected void setEntityActive(
            @NotNull final Medic medic,
            final boolean active
    ) {
        fireEvent(new SetActiveGridEvent(this, medic, active));
    }

    public static class DeletGridEvent extends AbstractGridEvent<MedicGrid, Medic> {
        protected DeletGridEvent(
                @NotNull final MedicGrid source,
                @NotNull final Medic entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateGridEvent extends AbstractGridEvent<MedicGrid, Medic> {
        protected UpdateGridEvent(
                @NotNull final MedicGrid source,
                @NotNull final Medic entity
        ) {
            super(source, entity);
        }
    }

    @Getter
    public static class SetActiveGridEvent extends AbstractGridEvent<MedicGrid, Medic> {
        private final boolean active;

        protected SetActiveGridEvent(
                @NotNull final MedicGrid source,
                @NotNull final Medic entity,
                final boolean active
        ) {
            super(source, entity);
            this.active = active;
        }
    }
}
