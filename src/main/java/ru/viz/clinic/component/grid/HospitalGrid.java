package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class HospitalGrid extends RUDGrid<Hospital> {
    public HospitalGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Hospital> setItems(final Collection<Hospital> items) {
        Objects.requireNonNull(items);
        return super.setItems(items);
    }

    private void createTable() {
        this.setSelectionMode(SelectionMode.SINGLE);
        this.addColumn(Hospital::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(Hospital::getName).setHeader(HDR_HOSPITAL);
        this.addColumn(hospital -> hospital.getAddress() != null
                ? hospital.getAddress().toString() : Strings.EMPTY).setHeader(HDR_ADDRESS);
        super.addRUDButtons();
        this.setAllRowsVisible(true);
        this.addClassNames("select", "primary");
    }

    @Override
    protected void updateEntity(final Hospital hospital) {
        fireEvent(new UpdateHospitalGridEvent(this, hospital));
    }

    @Override
    protected void deleteEntity(final Hospital hospital) {
        fireEvent(new DeleteHospitalGridEvent(this, hospital));
    }

    public static class DeleteHospitalGridEvent extends AbstractGridEvent<HospitalGrid, Hospital> {
        protected DeleteHospitalGridEvent(
                @NotNull final HospitalGrid source,
                @NotNull final Hospital entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateHospitalGridEvent extends AbstractGridEvent<HospitalGrid, Hospital> {
        protected UpdateHospitalGridEvent(
                @NotNull final HospitalGrid source,
                @NotNull final Hospital entity
        ) {
            super(source, entity);
        }
    }
}
