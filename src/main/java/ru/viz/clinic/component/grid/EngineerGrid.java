package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.model.HospitalGridFilterUpdater;

import java.util.*;

import static ru.viz.clinic.help.Translator.HDR_HOSPITAL;

public class EngineerGrid extends PersonGrid<Engineer> implements HospitalGridFilterUpdater {
    public EngineerPersonFilter engineerPersonFilter;

    public EngineerGrid() {
        createTable();
    }

    @Override
    public GridListDataView<Engineer> setItems(Collection<Engineer> items) {
        GridListDataView<Engineer> engineerPersonalGridListDataView = super.setItems(items);
        engineerPersonFilter = new EngineerPersonFilter(this.getListDataView());
        return engineerPersonalGridListDataView;
    }

    @Override
    public void setHospitalFilterParameter(Hospital hospital) {
        engineerPersonFilter.setHospital(hospital);
    }

    private void createTable() {
        List<Column<Engineer>> columns = new ArrayList<>(this.getColumns());
        Column<Engineer> hospitalColumn = this.addColumn(
                        engineerPersonal -> engineerPersonal.getHospital().getName())
                .setHeader(HDR_HOSPITAL);
        columns.add(0, hospitalColumn);
        this.setColumnOrder(columns);
    }

    public static class EngineerPersonFilter extends PersonFilter<Engineer> {
        private Hospital hospital;

        public EngineerPersonFilter(GridListDataView<Engineer> dataView) {
            super(dataView);
        }

        public void setHospital(Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        @Override
        public boolean test(Engineer person) {
            boolean superTest = super.test(person);
            boolean hospitalNull = person.getHospital() == null || hospital == null;
            boolean hospitalMatches = hospitalNull || Objects.equals(person.getHospital().getId(),
                    hospital.getId());
            return hospitalMatches && superTest;
        }
    }
}
