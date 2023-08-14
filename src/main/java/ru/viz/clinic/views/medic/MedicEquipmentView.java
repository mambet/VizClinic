package ru.viz.clinic.views.medic;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
import ru.viz.clinic.data.Role;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.service.*;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;

import static ru.viz.clinic.help.Helper.showErrorNotification;
import static ru.viz.clinic.help.Translator.*;

@RolesAllowed("MEDIC")
@PageTitle(PTT_EQUIPMENT)
@Route(value = "Equipment", layout = MainLayout.class)
@Slf4j
public class MedicEquipmentView extends VerticalLayout {

    private final EquipmentService equipmentService;
    private  EquipmentGrid equipmentGrid;
    private Medic currentMedic;

    public MedicEquipmentView(
            @NotNull final HospitalService hospitalService,
            @NotNull final DepartmentService departmentService,
            @NotNull final EquipmentService equipmentService,
            @NotNull final MedicService medicService
    ) {
        this.equipmentService = Objects.requireNonNull(equipmentService);

        Objects.requireNonNull(medicService).getLoggedMedic().ifPresent(medic -> {
            this.currentMedic = medic;
            this.equipmentGrid = new EquipmentGrid(this::haveToShow);
            updateGrid();
            add(getEquipmentTopicBox());
        });
    }

    private boolean haveToShow(final Equipment equipment) {
        return equipment.getMedic()!=null;
    }

    private TopicBox getEquipmentTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_EQUIPMENT, getCreateEquipmentButton(), equipmentGrid);
    }

    private Button getCreateEquipmentButton() {
        final Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEquipment);
        return confirm;
    }

    private void handleCreateEquipment(final ClickEvent<Button> buttonClickEvent) {
        final EquipmentDialog equipmentDialog = new MedicEquipmentDialog(currentMedic);
        equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::saveEquipment);
        equipmentDialog.open();
    }

    @Transactional
    private void saveEquipment(final EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        equipmentService.save(updateEquipmentEvent.getEntity());
        updateGrid();
        equipmentGrid.getListDataView().refreshAll();
    }

    private void updateGrid() {
        if (currentMedic != null) {
            this.equipmentGrid.setItems(equipmentService.getByDepartment(currentMedic.getDepartment()));
        } else {
            showErrorNotification("Ошибка");
        }
    }
}
