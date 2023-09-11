package ru.viz.clinic.views.medic;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
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
    private EquipmentGrid equipmentGrid;
    private Medic currentMedic;

    public MedicEquipmentView(
            @NotNull final EquipmentService equipmentService,
            @NotNull final MedicService medicService
    ) {
        this.equipmentService = Objects.requireNonNull(equipmentService);

        Objects.requireNonNull(medicService).getLoggedPersonal().ifPresent(medic -> {
            this.currentMedic = medic;
            this.equipmentGrid = EquipmentGrid.getMedicGrid();
            this.equipmentGrid.addListener(EquipmentGrid.UpdateGridEvent.class, this::handleUpdateEquipment);
            this.equipmentGrid.addListener(EquipmentGrid.DeleteGridEvent.class, this::handleDeleteEquipment);
            updateGrid();
            add(getEquipmentTopicBox());
        });
    }

    private void handleDeleteEquipment(final EquipmentGrid.DeleteGridEvent event) {
        deleteEquipment(event.getEntity());
    }


    private void handleCreateEquipment(final ClickEvent<Button> buttonClickEvent) {
        final EquipmentDialog equipmentDialog = MedicEquipmentDialog.getCreateDialog(currentMedic);
        equipmentDialog.addListener(EquipmentDialog.CreateEquipmentEvent.class, this::createEquipment);
        equipmentDialog.open();
    }

    private void handleUpdateEquipment(final EquipmentGrid.UpdateGridEvent event) {
        final EquipmentDialog equipmentDialog = MedicEquipmentDialog.getUpdateDialog(event.getEntity());
        equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::updateEquipment);
        equipmentDialog.open();
    }

    private TopicBox getEquipmentTopicBox() {
        return TopicBox.getInstance(DLH_EQUIPMENT, getCreateEquipmentButton(), equipmentGrid);
    }

    private Button getCreateEquipmentButton() {
        final Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEquipment);
        return confirm;
    }

    private void createEquipment(final EquipmentDialog.CreateEquipmentEvent event) {
        equipmentService.create(event.getEntity());
        updateGrid();
        equipmentGrid.getListDataView().refreshAll();
    }

    private void updateEquipment(final EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        equipmentService.create(updateEquipmentEvent.getEntity());
        updateGrid();
        equipmentGrid.getListDataView().refreshAll();
    }

    private void deleteEquipment(final Equipment equipment) {
        equipmentService.delete(equipment);
        updateGrid();
        equipmentGrid.getListDataView().refreshAll();
    }

    private void updateGrid() {
        if (currentMedic != null) {
            this.equipmentGrid.setItems(equipmentService.getActiveByDepartmentId(currentMedic.getDepartment().getId()));
        } else {
            showErrorNotification("Ошибка");
        }
    }
}