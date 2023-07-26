package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.DepartmentGridUpdater;

import java.util.Collection;
import java.util.Optional;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;

public class EquipmentGrid extends Grid<Equipment>  implements DepartmentGridUpdater {
    public EquipmentGrid() {
        createTable();
    }
    @Override
    public GridListDataView<Equipment> setItems(Collection<Equipment> items) {
        return super.setItems(items);
    }

    @Override
    public void updateDepartment(Optional<Department> optionalDepartment) {

    }

    @Override
    public void updateHospital(Optional<Hospital> optionalDepartment) {

    }

    private void createTable() {
        this.setSelectionMode(SelectionMode.SINGLE);
        this.addColumn(Equipment::getId);
        this.addColumn(Equipment::getNumber);
        this.addColumn(Equipment::getNumberNext);
        this.addColumn(Equipment::getCreateDate);
        this.setAllRowsVisible(true);
    }
}
