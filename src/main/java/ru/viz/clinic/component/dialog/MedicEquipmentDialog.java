package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;

import java.util.Objects;

public class MedicEquipmentDialog extends EquipmentDialog {
    private MedicEquipmentDialog(@NotNull final Equipment equipment) {
        super(Objects.requireNonNull(equipment));
        binder.readBean(equipment);
    }

    public static MedicEquipmentDialog getUpdateDialog(@NotNull final Equipment equipment) {
        final MedicEquipmentDialog medicEquipmentDialog = new MedicEquipmentDialog(equipment);
        medicEquipmentDialog.initUpdate();
        return medicEquipmentDialog;
    }

    public static MedicEquipmentDialog getCreateDialog(@NotNull final Medic currentMedic) {
        final MedicEquipmentDialog medicEquipmentDialog = new MedicEquipmentDialog(
                new Equipment(Objects.requireNonNull(currentMedic),
                        Objects.requireNonNull(currentMedic.getDepartment())));
        medicEquipmentDialog.initCreate();
        return medicEquipmentDialog;
    }
}
