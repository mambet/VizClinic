package ru.viz.clinic.component.components;

import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import static ru.viz.clinic.help.Translator.LBL_HOSPITAL;

public class HospitalSelect extends Select<Hospital> {
    private HospitalSelect() {
        this.setItemLabelGenerator(hospital -> hospital == null ? Strings.EMPTY : hospital.getName());
    }

    public static HospitalSelect createHospitalSelect(@NotNull final Collection<Hospital> hospitals) {
        final HospitalSelect hospitalSelect = new HospitalSelect();
        hospitalSelect.setLabel(LBL_HOSPITAL);
        hospitalSelect.setItems(hospitals);
        return hospitalSelect;
    }

    public static HospitalSelect createWithAllowEmpty(
            @NotNull final Consumer<Hospital> filterChangeConsumer
    ) {
        final HospitalSelect hospitalSelect = new HospitalSelect();
        Objects.requireNonNull(filterChangeConsumer);
        hospitalSelect.addValueChangeListener(changeEvent -> filterChangeConsumer.accept(
                changeEvent.getValue()));

        hospitalSelect.setEmptySelectionAllowed(true);
        hospitalSelect.setWidthFull();
        return hospitalSelect;
    }
}