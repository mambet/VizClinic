package ru.viz.clinic.component.components;

import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;

import static ru.viz.clinic.help.Translator.LBL_HOSPITAL;

public class HospitalSelect extends Select<Hospital> {
    public HospitalSelect(@NotNull final Collection<Hospital> hospitals) {
        this.setItems(hospitals);
        this.setItemLabelGenerator(Hospital::getName);
        this.setLabel(LBL_HOSPITAL);
    }
}
