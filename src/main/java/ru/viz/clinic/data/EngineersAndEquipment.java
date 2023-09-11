package ru.viz.clinic.data;

import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Equipment;

import java.util.Collection;

public record EngineersAndEquipment(Collection<Engineer> engineers, @NotNull Collection<Equipment> equipment) {
}
