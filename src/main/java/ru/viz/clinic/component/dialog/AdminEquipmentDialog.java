package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.select.Select;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.components.DepartmentSelect;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.service.DepartmentService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AdminEquipmentDialog extends EquipmentDialog {
    private final DepartmentService departmentService;
    private final DepartmentSelect departmentSelect = DepartmentSelect.createDepartmentSelect();

    private AdminEquipmentDialog(
            @NotNull final Equipment equipment,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        super(equipment);

        this.departmentService = Objects.requireNonNull(departmentService);
        final HospitalSelect hospitalSelect = HospitalSelect.createHospitalSelect(Objects.requireNonNull(hospitals));

        hospitalSelect.addValueChangeListener(this::hospitalSelectListener);

        binder.forField(hospitalSelect)
                .asRequired()
                .bind(m -> {
                    if (m.getDepartment() != null) {
                        return m.getDepartment().getHospital();
                    }
                    return null;
                }, (m, d) -> {
                });

        binder.forField(departmentSelect).asRequired().bind(Equipment::getDepartment, Equipment::setDepartment);
        binder.readBean(equipment);

        formLayout.addComponentAtIndex(0, hospitalSelect);
        formLayout.addComponentAtIndex(1, departmentSelect);
    }

    public static AdminEquipmentDialog getUpdateDialog(
            @NotNull final Equipment equipment,
            @NotNull final Collection<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        final AdminEquipmentDialog adminEquipmentDialog = new AdminEquipmentDialog(equipment,
                Objects.requireNonNull(hospitals),
                Objects.requireNonNull(departmentService));
        adminEquipmentDialog.initUpdate();
        return adminEquipmentDialog;
    }

    public static AdminEquipmentDialog getCreateDialog(
            @NotNull final List<Hospital> hospitals,
            @NotNull final DepartmentService departmentService
    ) {
        final AdminEquipmentDialog adminEquipmentDialog = new AdminEquipmentDialog(new Equipment(),
                Objects.requireNonNull(hospitals),
                Objects.requireNonNull(departmentService));
        adminEquipmentDialog.initCreate();
        return adminEquipmentDialog;
    }

    private void hospitalSelectListener(final AbstractField.ComponentValueChangeEvent<Select<Hospital>, Hospital> selectHospitalComponentValueChangeEvent) {
        final String hospitalId = selectHospitalComponentValueChangeEvent.getValue().getId();
        departmentSelect.setItems(departmentService.getActiveByHospitalId(hospitalId));
    }
}
