package ru.viz.clinic.views.admin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Medic;
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
    private final EngineerService engineerService;
    private final DepartmentService departmentService;
    private final MedicService medicService;
    private final EquipmentService equipmentService;
    private final HospitalGrid hospitalGrid;
    private final MedicGrid medicGrid;
    private final EngineerGrid engineerGrid;
    private final DepartmentGrid departmentGrid;
    private final EquipmentGrid equipmentGrid;

    public AdminView(
            @NotNull final AuthenticationService authenticationService,
            @NotNull final MedicService medicService,
            @NotNull final EngineerService engineerService,
            @NotNull final HospitalService hospitalService,
            @NotNull final DepartmentService departmentService,
            @NotNull final EquipmentService equipmentService
    ) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
        this.medicService = Objects.requireNonNull(medicService);
        this.engineerService = engineerService;
        this.hospitalService = Objects.requireNonNull(hospitalService);
        this.departmentService = Objects.requireNonNull(departmentService);
        this.equipmentService = Objects.requireNonNull(equipmentService);

        this.hospitalGrid = new HospitalGrid();
        this.engineerGrid = new EngineerGrid();
        this.departmentGrid = new DepartmentGrid();
        this.medicGrid = new MedicGrid();
        this.equipmentGrid = new EquipmentGrid();

        this.hospitalGrid.addListener(HospitalGrid.UpdateHospitalGridEvent.class, this::handleUpdateHospital);
        this.hospitalGrid.addListener(HospitalGrid.DeleteHospitalGridEvent.class, this::handleDeleteHospital);

        this.engineerGrid.addListener(EngineerGrid.UpdateEngineerGridEvent.class, this::handleUpdateEngineer);
        this.engineerGrid.addListener(EngineerGrid.DeleteEngineerGridEvent.class, this::handleDeleteEngineer);

        this.departmentGrid.addListener(DepartmentGrid.UpdateDepartmentGridEvent.class, this::handleUpdateDepartment);
        this.departmentGrid.addListener(DepartmentGrid.DeleteDepartmentGridEvent.class, this::handleDeleteDepartment);

        this.medicGrid.addListener(MedicGrid.UpdateMedicGridEvent.class, this::handleUpdateMedic);
        this.medicGrid.addListener(MedicGrid.DeleteMedicGridEvent.class, this::handleDeleteMedic);

        this.equipmentGrid.addListener(EquipmentGrid.UpdateEquipmentGridEvent.class, this::handleUpdateEquipment);
        this.equipmentGrid.addListener(EquipmentGrid.DeleteEquipmentGridEvent.class, this::handleDeleteEquipment);

        this.hospitalGrid.setItems(hospitalService.getAll());
        this.medicGrid.setItems(medicService.getAll());
        this.engineerGrid.setItems(engineerService.getAll());
        this.departmentGrid.setItems(departmentService.getAll());
        this.equipmentGrid.setItems(equipmentService.getAll());

        add(getHospitalTopicBox(), getEngineerTopicBox(), getDepartmentTopicBox(), getMedicTopicBox(),
                getEquipmentTopicBox());

        hospitalGrid.addSelectionListener(this::hospitalSelect);
        departmentGrid.addSelectionListener(this::departmentSelect);
    }

    private void hospitalSelect(final SelectionEvent<Grid<Hospital>, Hospital> gridHospitalSelectionEvent) {
        final AtomicReference<Hospital> hospitalAtomic = new AtomicReference<>();
        gridHospitalSelectionEvent.getFirstSelectedItem()
                .ifPresentOrElse(hospitalAtomic::set, () -> hospitalAtomic.set(null));

        departmentGrid.select(null);
        departmentGrid.setHospitalFilterParameter(hospitalAtomic.get());
        medicGrid.setHospitalFilterParameter(hospitalAtomic.get());
        engineerGrid.setHospitalFilterParameter(hospitalAtomic.get());
        equipmentGrid.setHospitalFilterParameter(hospitalAtomic.get());
    }

    private void departmentSelect(final SelectionEvent<Grid<Department>, Department> gridDepartmentSelectionEvent) {
        final AtomicReference<Department> departmentAtomic = new AtomicReference<>();
        gridDepartmentSelectionEvent.getFirstSelectedItem()
                .ifPresentOrElse(departmentAtomic::set, () -> departmentAtomic.set(null));
        medicGrid.setDepartmentFilterParameter(departmentAtomic.get());
        equipmentGrid.setDepartmentFilterParameter(departmentAtomic.get());
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
        final Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateHospital);
        return button;
    }

    private Button createDepartmentButton() {
        final Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateDepartment);
        return button;
    }

    private Button getCreateEngineerButton() {
        final Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEngineer);
        return confirm;
    }

    private Button getCreateMedicButton() {
        final Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateMedic);
        return confirm;
    }

    private Button getCreateEquipmentButton() {
        final Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEquipment);
        return confirm;
    }

    //create from button
    private void handleCreateHospital(final ClickEvent<Button> buttonClickEvent) {
        final HospitalDialog hospitalDialog = new HospitalDialog();
        hospitalDialog.addListener(HospitalDialog.UpdateHospitalDialogEvent.class, this::saveHospital);
        hospitalDialog.open();
    }

    private void handleCreateEngineer(final ClickEvent<Button> buttonClickEvent) {
        final EngineerDialog engineerDialog = new EngineerDialog(engineerService, hospitalService.getAll());
        engineerDialog.addListener(EngineerDialog.CreateEngineerPersonalEvent.class, this::createEngineer);
        engineerDialog.open();
    }

    private void handleCreateDepartment(final ClickEvent<Button> buttonClickEvent) {
        final DepartmentDialog departmentDialog = new DepartmentDialog(hospitalService.getAll());
        departmentDialog.addListener(DepartmentDialog.UpdateDepartmentEvent.class, this::saveDepartment);
        departmentDialog.open();
    }

    private void handleCreateMedic(final ClickEvent<Button> buttonClickEvent) {
        final MedicDialog medicDialog = new MedicDialog(medicService, hospitalService.getAll(), departmentService);
        medicDialog.addListener(MedicDialog.CreateMedicPersonalEvent.class, this::createMedic);
        medicDialog.open();
    }

    private void handleCreateEquipment(final ClickEvent<Button> buttonClickEvent) {
        final EquipmentDialog equipmentDialog = new AdminEquipmentDialog(hospitalService.getAll(), departmentService);
        equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::saveEquipment);
        equipmentDialog.open();
    }

    //update from grid
    private void handleUpdateHospital(final HospitalGrid.UpdateHospitalGridEvent event) {
        final HospitalDialog hospitalDialog = new HospitalDialog(event.getEntity());
        hospitalDialog.addListener(HospitalDialog.UpdateHospitalDialogEvent.class, this::saveHospital);
        hospitalDialog.open();
    }

    private void handleUpdateEngineer(final EngineerGrid.UpdateEngineerGridEvent event) {
        final EngineerDialog engineerDialog = new EngineerDialog(event.getEntity(), hospitalService.getAll());
        engineerDialog.addListener(EngineerDialog.UpdateEngineerPersonalEvent.class, this::updateEngineer);
        engineerDialog.open();
    }

    private void handleUpdateMedic(final MedicGrid.UpdateMedicGridEvent event) {
        final MedicDialog medicDialog = new MedicDialog(event.getEntity(), hospitalService.getAll(), departmentService);
        medicDialog.addListener(MedicDialog.UpdateMedicPersonalEvent.class, this::updateMedic);
        medicDialog.open();
    }

    private void handleUpdateEquipment(final EquipmentGrid.UpdateEquipmentGridEvent event) {
        final EquipmentDialog equipmentDialog = new AdminEquipmentDialog(event.getEntity(), hospitalService.getAll(),
                departmentService);
        equipmentDialog.addListener(HospitalDialog.UpdateHospitalDialogEvent.class, this::saveHospital);
        equipmentDialog.open();
    }

    private void handleUpdateDepartment(final DepartmentGrid.UpdateDepartmentGridEvent event) {
        final DepartmentDialog departmentDialog = new DepartmentDialog(event.getEntity(), hospitalService.getAll());
        departmentDialog.addListener(DepartmentDialog.UpdateDepartmentEvent.class, this::saveDepartment);
        departmentDialog.open();
    }


    //delete from grid
    private void handleDeleteHospital(final HospitalGrid.DeleteHospitalGridEvent event) {
        hospitalService.deleteHospital(event.getEntity().getId());
        updateHospitalGrid();
        updateDepartmentGrid();
        updateEngineerGrid();
        updateMedicGrid();
        updateEquipmentGrid();
        deselectAll();
    }

    private void handleDeleteEngineer(EngineerGrid.DeleteEngineerGridEvent event) {
    }

    private void handleDeleteDepartment(DepartmentGrid.DeleteDepartmentGridEvent event) {
    }

    private void handleDeleteMedic(MedicGrid.DeleteMedicGridEvent event) {

    }

    private void handleDeleteEquipment(EquipmentGrid.DeleteEquipmentGridEvent event) {
    }

    //TODO transactional funktioniert nicht
    private void saveHospital(final HospitalDialog.UpdateHospitalDialogEvent updateHospitalDialogEvent) {
        hospitalService.save(updateHospitalDialogEvent.getEntity());
        updateHospitalGrid();
        deselectAll();
    }

    private void updateEngineer(final EngineerDialog.UpdateEngineerPersonalEvent event) {
        engineerService.save(event.getEntity());
        updateEngineerGrid();
        deselectAll();
    }

    @Transactional
    private void createEngineer(final EngineerDialog.CreateEngineerPersonalEvent event) {
        final Engineer engineer = event.getEntity();
        authenticationService.createTempUserDetails(engineer.getUsername(), engineer.getTempPass());
        engineer.setTempPass(null);
        engineerService.save(engineer);
        updateEngineerGrid();
        deselectAll();
    }

    @Transactional
    private void saveDepartment(final DepartmentDialog.UpdateDepartmentEvent updateHospitalEvent) {
        departmentService.save(updateHospitalEvent.getEntity());
        updateDepartmentGrid();
        deselectAll();
    }

    private void createMedic(final MedicDialog.CreateMedicPersonalEvent event) {
        final Medic medic = event.getEntity();
        authenticationService.createTempUserDetails(medic.getUsername(), medic.getTempPass());
        medic.setTempPass(null);
        medicService.save(medic);
        updateMedicGrid();
        deselectAll();
    }

    @Transactional
    private void updateMedic(final MedicDialog.UpdateMedicPersonalEvent updateMedicPersonalEvent) {
        medicService.save(updateMedicPersonalEvent.getEntity());
        updateMedicGrid();
        deselectAll();
    }

    private void saveEquipment(final EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        equipmentService.save(updateEquipmentEvent.getEntity());
        updateEquipmentGrid();
        deselectAll();
    }

    private void updateHospitalGrid() {
        hospitalGrid.setItems(hospitalService.getAll());
        hospitalGrid.getListDataView().refreshAll();
    }

    private void updateDepartmentGrid() {
        departmentGrid.setItems(departmentService.getAll());
        departmentGrid.getListDataView().refreshAll();
    }

    private void updateEngineerGrid() {
        engineerGrid.setItems(engineerService.getAll());
        engineerGrid.getListDataView().refreshAll();
    }

    private void updateMedicGrid() {
        medicGrid.setItems(medicService.getAll());
        medicGrid.getListDataView().refreshAll();
    }

    private void updateEquipmentGrid() {
        equipmentGrid.setItems(equipmentService.getAll());
        equipmentGrid.getListDataView().refreshAll();
    }

    private void deselectAll() {
        hospitalGrid.select(null);
        departmentGrid.select(null);
        medicGrid.select(null);
    }
}
