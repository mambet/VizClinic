package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class HospitalGrid extends RUDGrid<Hospital> {
    private HospitalGrid() {
        createTable();
    }

    public static HospitalGrid createHospitalGrid() {
        return new HospitalGrid();
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
                ? hospital.getAddress().getEntityName() : Strings.EMPTY).setHeader(HDR_ADDRESS);
        super.addActionColumn();
        this.setAllRowsVisible(true);
    }

    @Override
    protected void updateEntity(@NotNull final  Hospital hospital) {
        fireEvent(new UpdateGridEvent(this, hospital));
    }

    @Override
    protected void deleteEntity(@NotNull final  Hospital hospital) {
        fireEvent(new DeleteGridEvent(this, hospital));
    }

    @Override
    protected void setEntityActive(@NotNull final Hospital entity,
                                   final boolean active
    ) {
        fireEvent(new SetActiveGridEvent(this, entity, active));
    }

    public static class DeleteGridEvent extends AbstractGridEvent<HospitalGrid, Hospital> {
        protected DeleteGridEvent(
                @NotNull final HospitalGrid source,
                @NotNull final Hospital entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateGridEvent extends AbstractGridEvent<HospitalGrid, Hospital> {
        protected UpdateGridEvent(
                @NotNull final HospitalGrid source,
                @NotNull final Hospital entity
        ) {
            super(source, entity);
        }
    }

    @Getter
    public static class SetActiveGridEvent extends AbstractGridEvent<HospitalGrid, Hospital> {
        private final boolean active;

        protected SetActiveGridEvent(
                @NotNull final HospitalGrid source,
                @NotNull final Hospital entity,
                final boolean active
        ) {
            super(source, entity);
            this.active = active;
        }
    }
}
