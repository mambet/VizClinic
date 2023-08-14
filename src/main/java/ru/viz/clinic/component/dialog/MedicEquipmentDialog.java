package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;

import java.util.Objects;

public class MedicEquipmentDialog extends EquipmentDialog {
    public MedicEquipmentDialog(@NotNull final Equipment equipment) {
        super(Objects.requireNonNull(equipment));
    }

    public MedicEquipmentDialog(@NotNull final Medic currentMedic) {
        this(new Equipment(Objects.requireNonNull(currentMedic),
                Objects.requireNonNull(currentMedic.getDepartment())));
    }
}
