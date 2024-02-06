package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.HospitalGridFilterUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class EngineerGrid extends PersonalGrid<Engineer> implements HospitalGridFilterUpdater {
    public EngineerPersonFilter engineerPersonFilter;

    private EngineerGrid() {
        createTable();
    }

    public static EngineerGrid createEngineerGrid() {
        return new EngineerGrid();
    }

    @Override
    public GridListDataView<Engineer> setItems(final Collection<Engineer> items) {
        final GridListDataView<Engineer> engineerPersonalGridListDataView = super.setItems(items);
        engineerPersonFilter = new EngineerPersonFilter(this.getListDataView());
        return engineerPersonalGridListDataView;
    }

    @Override
    public void setHospitalFilterParameter(final Hospital hospital) {
        engineerPersonFilter.setHospital(hospital);
    }

    private void createTable() {
        final List<Column<Engineer>> columns = new ArrayList<>(this.getColumns());
        final Column<Engineer> hospitalColumn = this.addColumn(getStyled(Engineer::getHospital))
                .setAutoWidth(true)
                .setResizable(true)
                .setHeader(HDR_HOSPITAL);
        columns.add(1, hospitalColumn);
        this.setColumnOrder(columns);
    }

    @Override
    protected void updateEntity(final Engineer engineer) {
        fireEvent(new UpdateGridEvent(this, engineer));
    }

    @Override
    protected void deleteEntity(final Engineer engineer) {
        fireEvent(new DeleteGridEvent(this, engineer));
    }

    @Override
    protected void setEntityActive(
            @NotNull final Engineer engineer,
            final boolean active
    ) {
        fireEvent(new SetActiveGridEvent(this, engineer, active));
    }

    public static class DeleteGridEvent extends AbstractGridEvent<EngineerGrid, Engineer> {
        protected DeleteGridEvent(
                @NotNull final EngineerGrid source,
                @NotNull final Engineer entity
        ) {
            super(source, entity);
        }
    }

    public static class UpdateGridEvent extends AbstractGridEvent<EngineerGrid, Engineer> {
        protected UpdateGridEvent(
                @NotNull final EngineerGrid source,
                @NotNull final Engineer entity
        ) {
            super(source, entity);
        }
    }

    @Getter
    public static class SetActiveGridEvent extends AbstractGridEvent<EngineerGrid, Engineer> {
        boolean active;

        protected SetActiveGridEvent(
                @NotNull final EngineerGrid source,
                @NotNull final Engineer entity,
                final boolean active
        ) {
            super(source, entity);
            this.active = active;
        }
    }

    public static class EngineerPersonFilter extends PersonFilter<Engineer> {
        private Hospital hospital;

        public EngineerPersonFilter(final GridListDataView<Engineer> dataView) {
            super(dataView);
        }

        public void setHospital(final Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        @Override
        public boolean test(final Engineer person) {
            final boolean superTest = super.test(person);
            final boolean hospitalNull = person.getHospital() == null || hospital == null;
            final boolean hospitalMatches = hospitalNull || Objects.equals(person.getHospital().getId(),
                    hospital.getId());
            return hospitalMatches && superTest;
        }
    }
}
