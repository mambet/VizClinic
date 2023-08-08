package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;

import static ru.viz.clinic.help.Translator.HDR_DEPARTMENT;

public class HospitalGrid extends Grid<Hospital> {
    public HospitalGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Hospital> setItems(Collection<Hospital> items) {
        return super.setItems(items);
    }

    private void createTable() {
        this.setSelectionMode(SelectionMode.SINGLE);
        this.addColumn(Hospital::getId);
        this.addColumn(Hospital::getName);
        this.addColumn(
                        hospital -> hospital.getAddress() != null ? hospital.getAddress().toString() : Strings.EMPTY)
                .setHeader(HDR_DEPARTMENT);
        this.setAllRowsVisible(true);
               this.addClassName("select");
        this.addClassName("primary");
    }
}
