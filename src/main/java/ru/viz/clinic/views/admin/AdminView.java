package ru.viz.clinic.views.admin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.data.entity.*;
import ru.viz.clinic.service.*;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static ru.viz.clinic.help.Translator.*;

@RolesAllowed("ADMIN")
@PageTitle(PTT_ADMIN)
@Route(value = "Personal", layout = MainLayout.class)
@Slf4j
public class AdminView extends VerticalLayout {
    private final AuthenticationService authenticationService;
    private final HospitalService hospitalService;
    private final DepartmentService departmentService;
    private final EngineerService engineerService;
    private final MedicService medicService;
    private final EquipmentService equipmentService;
    private final HospitalGrid hospitalGrid;
    private final MedicGrid medicGrid;
    private final EngineerGrid engineerGrid;
    private final DepartmentGrid departmentGrid;
    private final EquipmentGrid equipmentGrid;
    private final AtomicReference<Hospital> hospitalSelected = new AtomicReference<>();
    private final AtomicReference<Department> departmentSelected = new AtomicReference<>();
    private final Button btnCreateHospital = new Button(VaadinIcon.PLUS.create());
    private final Button btnCreateDepartment = new Button(VaadinIcon.PLUS.create());
    private final Button btnCreateEngineer = new Button(VaadinIcon.PLUS.create());
    private final Button btnCreateMedic = new Button(VaadinIcon.PLUS.create());
    private final Button btnCreateEquipment = new Button(VaadinIcon.PLUS.create());
    private boolean activeState;

    public AdminView(
            @NotNull final AuthenticationService authenticationService,
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService,
            @NotNull final HospitalService hospitalService,
            @NotNull final DepartmentService departmentService,
            @NotNull final EquipmentService equipmentService
    ) {
        log.debug("AdminView in");
        this.authenticationService = Objects.requireNonNull(authenticationService);
        this.medicService = Objects.requireNonNull(medicService);
        this.engineerService = engineerService;
        this.hospitalService = Objects.requireNonNull(hospitalService);
        this.departmentService = Objects.requireNonNull(departmentService);
        this.equipmentService = Objects.requireNonNull(equipmentService);

        this.hospitalGrid = HospitalGrid.createHospitalGrid();
        this.engineerGrid = EngineerGrid.createEngineerGrid();
        this.departmentGrid = DepartmentGrid.createDepartmentGrid();
        this.medicGrid = MedicGrid.createMedicGrid();
        this.equipmentGrid = EquipmentGrid.getAdminGrid();

        this.hospitalGrid.addListener(HospitalGrid.UpdateGridEvent.class, this::handleUpdateHospital);
        this.hospitalGrid.addListener(HospitalGrid.DeleteGridEvent.class, this::handleDeleteHospital);
        this.hospitalGrid.addListener(HospitalGrid.SetActiveGridEvent.class, this::handleSetActiveHospital);

        this.engineerGrid.addListener(EngineerGrid.UpdateGridEvent.class, this::handleUpdateEngineer);
        this.engineerGrid.addListener(EngineerGrid.DeleteGridEvent.class, this::handleDeleteEngineer);
        this.engineerGrid.addListener(EngineerGrid.SetActiveGridEvent.class, this::handleSetActiveEngineer);

        this.departmentGrid.addListener(DepartmentGrid.UpdateGridEvent.class, this::handleUpdateDepartment);
        this.departmentGrid.addListener(DepartmentGrid.DeleteGridEvent.class, this::handleDeleteDepartment);
        this.departmentGrid.addListener(DepartmentGrid.SetActiveGridEvent.class, this::handleSetActiveDepartment);

        this.medicGrid.addListener(MedicGrid.UpdateGridEvent.class, this::handleUpdateMedic);
        this.medicGrid.addListener(MedicGrid.DeletGridEvent.class, this::handleDeleteMedic);
        this.medicGrid.addListener(MedicGrid.SetActiveGridEvent.class, this::handleSetActiveMedic);

        this.equipmentGrid.addListener(EquipmentGrid.UpdateGridEvent.class, this::handleUpdateEquipment);
        this.equipmentGrid.addListener(EquipmentGrid.DeleteGridEvent.class, this::handleDeleteEquipment);
        this.equipmentGrid.addListener(EquipmentGrid.SetActiveGridEvent.class, this::handleSetActiveEquipment);

        this.hospitalGrid.setItems(hospitalService.getAllActive());
        this.medicGrid.setItems(medicService.getAllActive());
        this.engineerGrid.setItems(engineerService.getAllActive());
        this.departmentGrid.setItems(departmentService.getAllActive());
        this.equipmentGrid.setItems(equipmentService.getAllActive());

        this.btnCreateDepartment.setText(BTN_CONFIRM_CREATE_PLUS);
        this.btnCreateHospital.setText(BTN_CONFIRM_CREATE_PLUS);
        this.btnCreateEngineer.setText(BTN_CONFIRM_CREATE_PLUS);
        this.btnCreateMedic.setText(BTN_CONFIRM_CREATE_PLUS);
        this.btnCreateEquipment.setText(BTN_CONFIRM_CREATE_PLUS);

        final Select<Boolean> activeStateSelect = new Select<>();
        activeStateSelect.setItems(true, false);

        activeStateSelect.addValueChangeListener(event -> {
            activeState = Boolean.TRUE.equals(event.getValue());
            updateHospitalGrid();
            updateDepartmentGrid();
            updateEngineerGrid();
            updateMedicGrid();
            updateEquipmentGrid();
            btnCreateHospital.setVisible(event.getValue());
            btnCreateDepartment.setVisible(event.getValue());
            btnCreateEngineer.setVisible(event.getValue());
            btnCreateMedic.setVisible(event.getValue());
            btnCreateEquipment.setVisible(event.getValue());
        });

        activeStateSelect.setValue(true);
        activeStateSelect.setItemLabelGenerator(aBoolean -> {
            if (Boolean.TRUE.equals(aBoolean)) {
                return "активные";
            } else if (Boolean.FALSE.equals(aBoolean)) {
                return " не активные";
            }
            return Strings.EMPTY;
        });
        this.setSizeFull();

        final VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setPadding(false);
        mainContainer.add(getHospitalTopicBox(),
                getDepartmentTopicBox(),
                getEngineerTopicBox(),
                getMedicTopicBox(),
                getEquipmentTopicBox());

        final Scroller scroller = new Scroller(mainContainer);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.setSizeFull();

        add(new HorizontalLayout(activeStateSelect), scroller);

        hospitalGrid.addSelectionListener(this::hospitalSelect);
        departmentGrid.addSelectionListener(this::departmentSelect);
    }

    private void hospitalSelect(final SelectionEvent<Grid<Hospital>, Hospital> gridHospitalSelectionEvent) {
        if (gridHospitalSelectionEvent.isFromClient()) {
            gridHospitalSelectionEvent.getFirstSelectedItem()
                    .ifPresentOrElse(hospitalSelected::set, () -> hospitalSelected.set(null));
        }
        departmentGrid.deselectAll();
        departmentGrid.setHospitalFilterParameter(hospitalSelected.get());
        medicGrid.setHospitalFilterParameter(hospitalSelected.get());
        engineerGrid.setHospitalFilterParameter(hospitalSelected.get());
        equipmentGrid.setHospitalFilterParameter(hospitalSelected.get());
    }

    private void departmentSelect(final SelectionEvent<Grid<Department>, Department> gridDepartmentSelectionEvent) {
        if (gridDepartmentSelectionEvent.isFromClient()) {
            gridDepartmentSelectionEvent.getFirstSelectedItem()
                    .ifPresentOrElse(departmentSelected::set, () -> departmentSelected.set(null));
        }
        medicGrid.setDepartmentFilterParameter(departmentSelected.get());
        equipmentGrid.setDepartmentFilterParameter(departmentSelected.get());
    }

    private void restoreSelection() {
        final Hospital hospital = hospitalSelected.get();
        final Department department = departmentSelected.get();
        hospitalGrid.deselectAll();
        departmentGrid.deselectAll();
        hospitalGrid.select(hospital);
        departmentGrid.select(department);
    }

    private TopicBox getHospitalTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_HOSPITAL, createHospitalButton(), hospitalGrid);
    }

    private TopicBox getDepartmentTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_DEPARTMENT, createDepartmentButton(), departmentGrid);
    }

    private TopicBox getEngineerTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_ENGINEER, getCreateEngineerButton(), engineerGrid);
    }

    private TopicBox getMedicTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_MEDIC, getCreateMedicButton(), medicGrid);
    }

    private TopicBox getEquipmentTopicBox() {
        return TopicBox.getInstanceWithEye(DLH_EQUIPMENT, getCreateEquipmentButton(), equipmentGrid);
    }

    private Button createHospitalButton() {
        btnCreateHospital.addClickListener(this::handleCreateHospital);
        return btnCreateHospital;
    }

    private Button createDepartmentButton() {
        btnCreateDepartment.addClickListener(this::handleCreateDepartment);
        return btnCreateDepartment;
    }

    private Button getCreateEngineerButton() {
        btnCreateEngineer.addClickListener(this::handleCreateEngineer);
        return btnCreateEngineer;
    }

    private Button getCreateMedicButton() {
        btnCreateMedic.addClickListener(this::handleCreateMedic);
        return btnCreateMedic;
    }

    private Button getCreateEquipmentButton() {
        btnCreateEquipment.addClickListener(this::handleCreateEquipment);
        return btnCreateEquipment;
    }

    //create from button
    private void handleCreateHospital(final ClickEvent<Button> buttonClickEvent) {
        final HospitalDialog hospitalDialog = HospitalDialog.getCreateDialog();
        hospitalDialog.addListener(HospitalDialog.CreateHospitalDialogEvent.class, this::createHospital);
        hospitalDialog.open();
    }

    private void handleCreateEngineer(final ClickEvent<Button> buttonClickEvent) {
        final EngineerDialog engineerDialog = EngineerDialog.getCreateDialog(authenticationService,
                hospitalService.getAllActive());
        engineerDialog.addListener(EngineerDialog.CreateEngineerPersonalEvent.class, this::createEngineer);
        engineerDialog.open();
    }

    private void handleCreateDepartment(final ClickEvent<Button> buttonClickEvent) {
        final DepartmentDialog departmentDialog = DepartmentDialog.getCreateDialog(
                hospitalService.getAllActive());
        departmentDialog.addListener(DepartmentDialog.CreateDepartmentEvent.class, this::createDepartment);
        departmentDialog.open();
    }

    private void handleCreateMedic(final ClickEvent<Button> buttonClickEvent) {
        final MedicDialog medicDialog = MedicDialog.getCreateDialog(
                hospitalService.getAllActive(),
                departmentService,
                authenticationService);
        medicDialog.addListener(MedicDialog.CreateMedicPersonalEvent.class, this::createMedic);
        medicDialog.open();
    }

    private void handleCreateEquipment(final ClickEvent<Button> buttonClickEvent) {
        final EquipmentDialog equipmentDialog = AdminEquipmentDialog.getCreateDialog(
                hospitalService.getAllActive(),
                departmentService);
        equipmentDialog.addListener(EquipmentDialog.CreateEquipmentEvent.class, this::createEquipment);
        equipmentDialog.open();
    }

    //update from grid
    private void handleUpdateHospital(final HospitalGrid.UpdateGridEvent event) {
        final HospitalDialog hospitalDialog = HospitalDialog.getUpdateDialog(event.getEntity());
        hospitalDialog.addListener(HospitalDialog.UpdateHospitalDialogEvent.class, this::updateHospital);
        hospitalDialog.open();
    }

    private void handleUpdateEngineer(final EngineerGrid.UpdateGridEvent event) {
        final EngineerDialog engineerDialog = EngineerDialog.getUpdateDialog(event.getEntity(),
                hospitalService.getAllActive());
        engineerDialog.addListener(EngineerDialog.UpdateEngineerDialogEvent.class, this::updateEngineer);
        engineerDialog.open();
    }

    private void handleUpdateMedic(final MedicGrid.UpdateGridEvent event) {
        final MedicDialog medicDialog = MedicDialog.getUpdateDialog(event.getEntity(),
                hospitalService.getAllActive(),
                departmentService);
        medicDialog.addListener(MedicDialog.UpdateMedicEvent.class, this::updateMedic);
        medicDialog.open();
    }

    private void handleUpdateEquipment(final EquipmentGrid.UpdateGridEvent event) {
        final EquipmentDialog equipmentDialog = AdminEquipmentDialog.getUpdateDialog(event.getEntity(),
                hospitalService.getAllActive(),
                departmentService);
        equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::updateEquipment);
        equipmentDialog.open();
    }

    private void handleUpdateDepartment(final DepartmentGrid.UpdateGridEvent event) {
        final DepartmentDialog departmentDialog = DepartmentDialog.getUpdateDialog(event.getEntity(),
                hospitalService.getAllActive());
        departmentDialog.addListener(DepartmentDialog.UpdateDepartmentEvent.class, this::updateDepartment);
        departmentDialog.open();
    }

    private void createHospital(final HospitalDialog.CreateHospitalDialogEvent createHospitalDialogEvent) {
        hospitalService.create(createHospitalDialogEvent.getEntity());
        updateHospitalGrid();
        restoreSelection();
    }

    private void createEngineer(final EngineerDialog.CreateEngineerPersonalEvent event) {
        engineerService.create(event.getEntity());
        updateEngineerGrid();
        restoreSelection();
    }

    private void createDepartment(final DepartmentDialog.CreateDepartmentEvent createDepartmentEvent) {
        departmentService.create(createDepartmentEvent.getEntity());
        updateDepartmentGrid();
        restoreSelection();
    }

    private void createMedic(final MedicDialog.CreateMedicPersonalEvent event) {
        medicService.create(event.getEntity());
        updateMedicGrid();
        restoreSelection();
    }

    private void createEquipment(final EquipmentDialog.CreateEquipmentEvent createEquipmentEvent) {
        equipmentService.create(createEquipmentEvent.getEntity());
        updateEquipmentGrid();
        restoreSelection();
    }

    private void updateHospital(final HospitalDialog.UpdateHospitalDialogEvent updateHospitalDialogEvent) {
        hospitalService.update(updateHospitalDialogEvent.getEntity()).ifPresent(hospital -> {
            updateHospitalGrid();
            updateEngineerGrid();
            updateDepartmentGrid();
            updateMedicGrid();
            updateEquipmentGrid();
            restoreSelection();
        });
    }

    private void updateEngineer(final EngineerDialog.UpdateEngineerDialogEvent event) {
        engineerService.update(event.getEntity()).ifPresent(engineer -> {
            updateEngineerGrid();
            restoreSelection();
        });
    }

    private void updateDepartment(final DepartmentDialog.UpdateDepartmentEvent updateHospitalEvent) {
        departmentService.update(updateHospitalEvent.getEntity()).ifPresent(department -> {
            updateDepartmentGrid();
            updateMedicGrid();
            updateEquipmentGrid();
            restoreSelection();
        });
    }

    private void updateMedic(final MedicDialog.UpdateMedicEvent updateMedicEvent) {
        medicService.update(updateMedicEvent.getEntity()).ifPresent(medic -> {
            updateMedicGrid();
            restoreSelection();
        });
    }

    private void updateEquipment(final EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        equipmentService.update(updateEquipmentEvent.getEntity()).ifPresent(equipment -> {
            updateEquipmentGrid();
            restoreSelection();
        });
    }

    //delete from grid
    private void handleDeleteHospital(final HospitalGrid.DeleteGridEvent event) {
        hospitalService.delete(event.getEntity());
        if (Objects.equals(hospitalGrid.asSingleSelect().getValue(), event.getEntity())) {
            hospitalSelected.set(null);
        }
        updateHospitalGrid();
        updateDepartmentGrid();
        updateEngineerGrid();
        updateMedicGrid();
        updateEquipmentGrid();
        restoreSelection();
    }

    private void handleDeleteEngineer(final EngineerGrid.DeleteGridEvent event) {
        engineerService.delete(event.getEntity());
        updateEngineerGrid();
        restoreSelection();
    }

    private void handleDeleteDepartment(final DepartmentGrid.DeleteGridEvent event) {
        departmentService.delete(event.getEntity());
        if (Objects.equals(departmentGrid.asSingleSelect().getValue(), event.getEntity())) {
            departmentSelected.set(null);
        }
        updateDepartmentGrid();
        updateMedicGrid();
        updateEquipmentGrid();
        restoreSelection();
    }

    private void handleDeleteMedic(final MedicGrid.DeletGridEvent event) {
        medicService.delete(event.getEntity());
        updateMedicGrid();
        restoreSelection();
    }

    private void handleDeleteEquipment(final EquipmentGrid.DeleteGridEvent event) {
        equipmentService.delete(event.getEntity());
        updateEquipmentGrid();
        restoreSelection();
    }

    private void handleSetActiveHospital(@NotNull final HospitalGrid.SetActiveGridEvent event) {
        hospitalService.setActive(event.getEntity(), event.isActive());
        if (Objects.equals(hospitalGrid.asSingleSelect().getValue(), event.getEntity())) {
            hospitalSelected.set(null);
        }

        updateHospitalGrid();
        updateDepartmentGrid();
        updateEngineerGrid();
        updateMedicGrid();
        updateEquipmentGrid();
        restoreSelection();
    }

    private void handleSetActiveDepartment(@NotNull final DepartmentGrid.SetActiveGridEvent event) {
        departmentService.setActive(event.getEntity(), event.isActive());
        if (Objects.equals(departmentGrid.asSingleSelect().getValue(), event.getEntity())) {
            departmentSelected.set(null);
        }
        updateDepartmentGrid();
        updateMedicGrid();
        updateEquipmentGrid();
        restoreSelection();
    }

    private void handleSetActiveEngineer(final EngineerGrid.SetActiveGridEvent event) {
        if (event.isActive()) {
            if (engineerService.isReadyToActivate(event.getEntity())) {
                final EngineerDialog updateAuthorityDialog = EngineerDialog.getUpdateAuthorityDialog(event.getEntity(),
                        authenticationService);
                updateAuthorityDialog.addListener(EngineerDialog.UpdateEngineerDialogEvent.class,
                        updateEvent -> {
                            engineerService.setActive(updateEvent.getEntity(), true);
                            restoreSelection();
                            updateEngineerGrid();
                        });
                updateAuthorityDialog.open();
            }
        } else {
            engineerService.setActive(event.getEntity(), false);
            restoreSelection();
            updateEngineerGrid();
        }
    }

    private void handleSetActiveMedic(@NotNull final MedicGrid.SetActiveGridEvent event) {
        if (event.isActive()) {
            if (medicService.isReadyToActivate(event.getEntity())) {
                final MedicDialog updateAuthorityDialog = MedicDialog.getUpdateAuthorityDialog(event.getEntity(),
                        authenticationService);
                updateAuthorityDialog.addListener(MedicDialog.UpdateMedicEvent.class,
                        updateEvent -> {
                            medicService.setActive(updateEvent.getEntity(), true);
                            restoreSelection();
                            updateMedicGrid();
                        });
                updateAuthorityDialog.open();
            }
        } else {
            medicService.setActive(event.getEntity(), false);
            restoreSelection();
            updateMedicGrid();
        }
    }

    private void handleSetActiveEquipment(@NotNull final EquipmentGrid.SetActiveGridEvent event) {
        equipmentService.setActive(event.getEntity(), event.isActive());
        updateEquipmentGrid();
        restoreSelection();
    }

    private void updateHospitalGrid() {
        if (activeState) {
            hospitalGrid.setItems(hospitalService.getAllActive());
        } else {
            hospitalGrid.setItems(hospitalService.getAllInactive());
        }
        hospitalGrid.getListDataView().refreshAll();
    }

    private void updateDepartmentGrid() {
        if (activeState) {
            departmentGrid.setItems(departmentService.getAllActive());
        } else {
            departmentGrid.setItems(departmentService.getAllInactive());
        }
        departmentGrid.getListDataView().refreshAll();
    }

    private void updateEngineerGrid() {
        if (activeState) {
            this.engineerGrid.setItems(engineerService.getAllActive());
        } else {
            this.engineerGrid.setItems(engineerService.getAllInactive());
        }
        engineerGrid.getListDataView().refreshAll();
    }

    private void updateMedicGrid() {
        if (activeState) {
            medicGrid.setItems(medicService.getAllActive());
        } else {
            medicGrid.setItems(medicService.getAllInactive());
        }
        medicGrid.getListDataView().refreshAll();
    }

    private void updateEquipmentGrid() {
        if (activeState) {
            equipmentGrid.setItems(equipmentService.getAllActive());
        } else {
            equipmentGrid.setItems(equipmentService.getAllInactive());
        }
        equipmentGrid.getListDataView().refreshAll();
    }
}