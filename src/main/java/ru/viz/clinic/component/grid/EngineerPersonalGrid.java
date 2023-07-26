package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import ru.viz.clinic.data.entity.EngineerPersonal;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.HospitalGridUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class EngineerPersonalGrid extends PersonGrid<EngineerPersonal> implements HospitalGridUpdater {
    public EngineerPersonFilter engineerPersonFilter;

    public EngineerPersonalGrid() {
        createTable();
    }

    @Override
    public GridListDataView<EngineerPersonal> setItems(Collection<EngineerPersonal> items) {
        GridListDataView<EngineerPersonal> engineerPersonalGridListDataView = super.setItems(items);
        engineerPersonFilter = new EngineerPersonFilter(this.getListDataView());
        return engineerPersonalGridListDataView;
    }

    @Override
    public void updateHospital(Optional<Hospital> optionalHospital) {
        optionalHospital.ifPresentOrElse(engineerPersonFilter::setHospital,
                () -> engineerPersonFilter.setHospital(null));
    }

    private void createTable() {
        this.setSelectionMode(Grid.SelectionMode.NONE);
        List<Column<EngineerPersonal>> columns = new ArrayList<>(this.getColumns());
        Column<EngineerPersonal> hospitalColumn = this.addColumn(
                        engineerPersonal -> engineerPersonal.getHospital().getName())
                .setHeader(HDR_HOSPITAL);
        columns.add(0, hospitalColumn);
        this.setColumnOrder(columns);
    }

    public static class EngineerPersonFilter extends PersonFilter<EngineerPersonal> {
        private Hospital hospital;
        public EngineerPersonFilter(GridListDataView<EngineerPersonal> dataView) {
            super(dataView);
        }

        public void setHospital(Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        @Override
        public boolean test(EngineerPersonal person) {
            boolean superTest = super.test(person);
            boolean hospitalNull = person.getHospital() == null || hospital == null;
            boolean hospitalMatches = hospitalNull || Objects.equals(person.getHospital().getId(),
                    hospital.getId());
            return hospitalMatches && superTest;
        }
    }
}
