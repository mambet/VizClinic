package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.converter.PersonalToStringConverter;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.DepartmentGridFilterUpdater;
import ru.viz.clinic.help.Helper;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import static ru.viz.clinic.help.Translator.*;

public class EquipmentGrid extends RUDGrid<Equipment> implements DepartmentGridFilterUpdater {
    EquipmentFilter equipmentFilter;

    public EquipmentGrid() {
        createTable();
        super.addRUDButtons();
    }
    public EquipmentGrid(Predicate<Equipment> haveToShowPredicate) {
        createTable();
        super.addRUDButtonsIf(haveToShowPredicate);
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

    private void createTable() {
        this.setSelectionMode(SelectionMode.NONE);
        this.addColumn(Equipment::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(equipment -> {
                    if (equipment.getDepartment() != null && equipment.getDepartment().getHospital() != null) {
                        return equipment.getDepartment().getHospital().getName();
                    } else {
                        return Strings.EMPTY;
                    }
                })
                .setHeader(HDR_HOSPITAL);

        this.addColumn(equipment -> {
            if (equipment.getDepartment() != null) {
                return equipment.getDepartment().getName();
            } else {
                return Strings.EMPTY;
            }
        }).setHeader(HDR_DEPARTMENT);

        this.addColumn(Equipment::getNumber).setHeader(HDR_EQUIPMENT_NUMBER_1);
        this.addColumn(Equipment::getNumberNext).setHeader(HDR_EQUIPMENT_NUMBER_2);
        this.addColumn(Helper.getDateRenderer(Equipment::getCreateDate)).setHeader(HDR_EQUIPMENT_CREATION_TIME);
        this.addColumn(Equipment::getDescription).setHeader(HDR_EQUIPMENT_DESCRIPTION);
        this.addColumn(equipment -> PersonalToStringConverter.convertToPresentation(equipment.getMedic()))
                .setHeader(HDR_EQUIPMENT_DESCRIPTION); //TODO rename header

        this.setAllRowsVisible(true);
        this.addClassNames("primary");
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
        fireEvent(new EquipmentGrid.UpdateEquipmentGridEvent(this, equipment));
    }

    @Override
    protected void deleteEntity(final Equipment equipment) {
        fireEvent(new EquipmentGrid.DeleteEquipmentGridEvent(this, equipment));
    }


    public static class DeleteEquipmentGridEvent extends AbstractGridEvent<EquipmentGrid, Equipment> {
        protected DeleteEquipmentGridEvent(
                @NotNull final EquipmentGrid source,
                @NotNull final Equipment entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateEquipmentGridEvent extends AbstractGridEvent<EquipmentGrid, Equipment> {
        protected UpdateEquipmentGridEvent(
                @NotNull final EquipmentGrid source,
                @NotNull final Equipment entity
        ) {
            super(source, entity);
        }
    }
}
