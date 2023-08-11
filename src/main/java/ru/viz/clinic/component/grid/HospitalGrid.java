package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class HospitalGrid extends Grid<Hospital> {
    public HospitalGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Hospital> setItems(Collection<Hospital> items) {
        Objects.requireNonNull(items);
        return super.setItems(items);
    }

    private void createTable() {
        this.setSelectionMode(SelectionMode.SINGLE);
        this.addColumn(Hospital::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);;
        this.addColumn(Hospital::getName).setHeader(HDR_HOSPITAL);
        this.addColumn(hospital -> hospital.getAddress() != null
                        ? hospital.getAddress().toString() : Strings.EMPTY)
                .setHeader(HDR_ADDRESS);

        this.setAllRowsVisible(true);
        this.addClassNames("select", "primary");
    }
}
