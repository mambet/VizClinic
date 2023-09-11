package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.data.model.DepartmentGridFilterUpdater;
import ru.viz.clinic.help.Helper;

import java.util.*;

import static ru.viz.clinic.help.Translator.*;

public class EquipmentGrid extends RUDGrid<Equipment> implements DepartmentGridFilterUpdater {
    EquipmentFilter equipmentFilter;

    private EquipmentGrid() {
        this.setSelectionMode(SelectionMode.NONE);
        this.setAllRowsVisible(true);
        this.addClassNames("primary");
    }

    public static EquipmentGrid getAdminGrid() {
        final EquipmentGrid equipmentGrid = new EquipmentGrid();
        equipmentGrid.addCommonColumn();
        equipmentGrid.addActionColumn();

        final List<Column<Equipment>> columns = new ArrayList<>(equipmentGrid.getColumns());

        final Column<Equipment> hospitalColumn = equipmentGrid.addHospitalColumn();
        final Column<Equipment> departmentColumn = equipmentGrid.addDepartmentColumn();

        columns.add(1, hospitalColumn);
        columns.add(2, departmentColumn);

        equipmentGrid.setColumnOrder(columns);
        return equipmentGrid;
    }

    public static EquipmentGrid getMedicGrid() {
        final EquipmentGrid equipmentGrid = new EquipmentGrid();
        equipmentGrid.addCommonColumn();
        equipmentGrid.addActionColumn(equipmentGrid::getMedicActionButton);
        return equipmentGrid;
    }

    public Optional<Button[]> getMedicActionButton(@NotNull final Equipment equipment) {
        if (equipment.getMedic() != null) {
            return Optional.of(new Button[]{this.editButton(equipment), this.deleteButton(equipment)});
        }
        return Optional.empty();
    }

    private Column<Equipment> addHospitalColumn() {
        return this.addColumn(getStyled(equipment -> {
                    if (equipment.getDepartment() == null) {
                        return null;
                    }
                    return equipment.getDepartment().getHospital();
                }
        )).setHeader(HDR_HOSPITAL).setResizable(true);
    }

    private Column<Equipment> addDepartmentColumn() {
        return this.addColumn(getStyled(Equipment::getDepartment))
                .setHeader(HDR_DEPARTMENT).setResizable(true);
    }

    @Override
    public GridListDataView<Equipment> setItems(final Collection<Equipment> items) {
        final GridListDataView<Equipment> equipmentGridListDataView = super.setItems(items);
        equipmentFilter = new EquipmentFilter(this.getListDataView());
        return equipmentGridListDataView;
    }

    @Override
    public void setDepartmentFilterParameter(final Department department) {
        equipmentFilter.setDepartment(department);
    }

    @Override
    public void setHospitalFilterParameter(final Hospital hospital) {
        equipmentFilter.setHospital(hospital);
    }

    private void addCommonColumn() {
        this.addColumn(Equipment::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(Equipment::getEntityName).setHeader(HDR_EQUIPMENT).setResizable(true);
        this.addColumn(Equipment::getInventoryNumber).setHeader(HDR_EQUIPMENT_INVENTORY_NUMBER).setAutoWidth(true).setResizable(true);
        this.addColumn(Equipment::getFactoryNumber).setHeader(HDR_EQUIPMENT_FABRIC_NUMBER).setAutoWidth(true)
                .setResizable(true);
        this.addColumn(Helper.getDateRenderer(Equipment::getCreateDate)).setHeader(HDR_EQUIPMENT_CREATION_TIME)
                .setAutoWidth(true).setResizable(true);
        this.addColumn(Helper.getDateRenderer(Equipment::getCommissioningDate)).setHeader(HDR_EQUIPMENT_COMMISSIONING_TIME)
                .setAutoWidth(true).setResizable(true);
        this.addColumn(Equipment::getDescription).setHeader(HDR_EQUIPMENT_DESCRIPTION).setAutoWidth(true)
                .setResizable(true);
    }

    public static class EquipmentFilter {
        public final GridListDataView<Equipment> dataView;
        private Department department;
        private Hospital hospital;

        public EquipmentFilter(final GridListDataView<Equipment> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setDepartment(final Department department) {
            this.department = department;
            this.dataView.refreshAll();
        }

        public void setHospital(final Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        public boolean test(final Equipment equipment) {
            final boolean departmentNull = equipment.getDepartment() == null || department == null;
            final boolean hospitalNull = equipment.getDepartment() == null
                    || equipment.getDepartment().getHospital() == null
                    || hospital == null;
            final boolean departmentMatches = departmentNull || Objects.equals(equipment.getDepartment().getId(),
                    department.getId());
            final boolean hospitalMatches = hospitalNull || Objects.equals(
                    equipment.getDepartment().getHospital().getId(),
                    hospital.getId());

            return departmentMatches && hospitalMatches;
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
    protected void updateEntity(final Equipment equipment) {
        fireEvent(new UpdateGridEvent(this, equipment));
    }

    @Override
    protected void deleteEntity(final Equipment equipment) {
        fireEvent(new DeleteGridEvent(this, equipment));
    }

    @Override
    protected void setEntityActive(
            @NotNull final Equipment equipment,
            final boolean active
    ) {
        fireEvent(new SetActiveGridEvent(this, equipment, active));
    }

    public static class DeleteGridEvent extends AbstractGridEvent<EquipmentGrid, Equipment> {
        protected DeleteGridEvent(
                @NotNull final EquipmentGrid source,
                @NotNull final Equipment entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateGridEvent extends AbstractGridEvent<EquipmentGrid, Equipment> {
        protected UpdateGridEvent(
                @NotNull final EquipmentGrid source,
                @NotNull final Equipment entity
        ) {
            super(source, entity);
        }
    }

    @Getter
    public static class SetActiveGridEvent extends AbstractGridEvent<EquipmentGrid, Equipment> {
        private final boolean active;

        protected SetActiveGridEvent(
                @NotNull final EquipmentGrid source,
                @NotNull final Equipment entity,
                final boolean active
        ) {
            super(source, entity);
            this.active = active;
        }
    }
}
