package ru.viz.clinic.component.components;

import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Equipment;

import java.util.Objects;
import java.util.function.Consumer;

import static ru.viz.clinic.help.Translator.LBL_EQUIPMENT_NAME;

public class EquipmentSelect extends Select<Equipment> {
    private EquipmentSelect() {
        this.setItemLabelGenerator(equipment -> equipment == null ? Strings.EMPTY : equipment.getName());
    }

    public static EquipmentSelect createDepartmentSelect() {
        final EquipmentSelect departmentSelect = new EquipmentSelect();
        departmentSelect.setLabel(LBL_EQUIPMENT_NAME);
        return departmentSelect;
    }

    public static EquipmentSelect createWithAllowEmpty(@NotNull final Consumer<Equipment> filterChangeConsumer) {
        final EquipmentSelect equipmentSelect = new EquipmentSelect();
        equipmentSelect.setEmptySelectionAllowed(true);
        Objects.requireNonNull(filterChangeConsumer);
        equipmentSelect.addValueChangeListener(changeEvent -> filterChangeConsumer.accept(
                changeEvent.getValue()));
        equipmentSelect.setWidthFull();
        return equipmentSelect;
    }
}
