package ru.viz.clinic.views.login;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.service.*;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;
import java.util.Optional;

import static ru.viz.clinic.help.Translator.*;

@RolesAllowed("ADMIN")
@PageTitle("Personal")
@Route(value = "Personal", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class PersonalView extends VerticalLayout {
    private final AuthenticationService authenticationService;
    private final HospitalService hospitalService;
    private final EngineerPersonalService engineerPersonalService;
    private final DepartmentService departmentService;
    private final MedicPersonalService medicPersonalService;
    private final EquipmentService equipmentService;
    private final Grid<Hospital> hospitalGrid;
    private final MedicPersonalGrid medicPersonalGrid;
    private final EngineerPersonalGrid engineerPersonalGrid;
    private final DepartmentGrid departmentGrid;
    private final EquipmentGrid equipmentGrid;

    public PersonalView(
            @NotNull final AuthenticationService authenticationService,
            @NotNull final MedicPersonalService medicPersonalService,
            @NotNull final EngineerPersonalService engineerPersonalService,
            @NotNull final HospitalService hospitalService,
            @NotNull final DepartmentService departmentService,
            @NotNull final EquipmentService equipmentService
    ) {
        this.authenticationService = Objects.requireNonNull(authenticationService);
        this.medicPersonalService = Objects.requireNonNull(medicPersonalService);
        this.engineerPersonalService = engineerPersonalService;
        this.hospitalService = Objects.requireNonNull(hospitalService);
        this.departmentService = Objects.requireNonNull(departmentService);
        this.equipmentService = Objects.requireNonNull(equipmentService);

        this.hospitalGrid = new HospitalGrid();
        this.medicPersonalGrid = new MedicPersonalGrid();
        this.engineerPersonalGrid = new EngineerPersonalGrid();
        this.departmentGrid = new DepartmentGrid();
        this.equipmentGrid = new EquipmentGrid();

        this.hospitalGrid.setItems(hospitalService.getAll());
        this.medicPersonalGrid.setItems(medicPersonalService.getAll());
        this.engineerPersonalGrid.setItems(engineerPersonalService.getAll());
        this.departmentGrid.setItems(departmentService.getAll());
        this.equipmentGrid.setItems(equipmentService.getAll());

        add(getHospitalTopicBox(), getEngineerTopicBox(), getDepartmentTopicBox(), getMedicTopicBox(),
                getEquipmentTopicBox());

        hospitalGrid.addSelectionListener(this::hospitalSelect);
        departmentGrid.addSelectionListener(this::departmentSelect);

        this.getElement().getStyle().set("background", "#bababb");
    }

    private void hospitalSelect(SelectionEvent<Grid<Hospital>, Hospital> gridHospitalSelectionEvent) {
        Optional<Hospital> optionalHospital = gridHospitalSelectionEvent.getFirstSelectedItem();
        departmentGrid.select(null);
        departmentGrid.updateHospital(optionalHospital);
        medicPersonalGrid.updateHospital(optionalHospital);
        engineerPersonalGrid.updateHospital(optionalHospital);
        equipmentGrid.updateHospital(optionalHospital);
    }

    private void departmentSelect(SelectionEvent<Grid<Department>, Department> gridDepartmentSelectionEvent) {
        Optional<Department> departmentOptional = gridDepartmentSelectionEvent.getFirstSelectedItem();
        medicPersonalGrid.updateDepartment(departmentOptional);
        equipmentGrid.updateDepartment(departmentOptional);
    }

    private TopicBox getHospitalTopicBox() {
        return new TopicBox(DLH_HOSPITAL, createHospitalButton(), hospitalGrid);
    }

    private TopicBox getDepartmentTopicBox() {
        return new TopicBox(DLH_DEPARTMENT, createDepartmentButton(), departmentGrid);
    }

    private TopicBox getEngineerTopicBox() {
        return new TopicBox(DLH_ENGINEER, getCreateEngineerButton(), engineerPersonalGrid);
    }

    private TopicBox getMedicTopicBox() {
        return new TopicBox(DLH_MEDIC, getCreateMedicButton(), medicPersonalGrid);
    }

    private TopicBox getEquipmentTopicBox() {
        return new TopicBox(DLH_EQUIPMENT, getCreateEquipmentButton(), equipmentGrid);
    }

    private Button createHospitalButton() {
        Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateHospital);
        return button;
    }

    private Button createDepartmentButton() {
        Button button = new Button(BTN_CONFIRM_CREATE_PLUS);
        button.addClickListener(this::handleCreateDepartment);
        return button;
    }

    private Button getCreateEngineerButton() {
        Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEngineer);
        return confirm;
    }

    private Button getCreateMedicButton() {
        Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateMedic);
        return confirm;
    }

    private Button getCreateEquipmentButton() {
        Button confirm = new Button(BTN_CONFIRM_CREATE_PLUS);
        confirm.addClickListener(this::handleCreateEquipment);
        return confirm;
    }

    private void handleCreateHospital(ClickEvent<Button> buttonClickEvent) {
        HospitalDialog hospitalDialog = new HospitalDialog();
        hospitalDialog.addListener(HospitalDialog.UpdateHospitalEvent.class, this::saveHospital);
        hospitalDialog.open();
    }

    private void handleCreateEngineer(ClickEvent<Button> buttonClickEvent) {
        EngineerPersonalDialog engineerPersonalDialog = new EngineerPersonalDialog(engineerPersonalService,
                hospitalService.getAll());
        engineerPersonalDialog.addListener(EngineerPersonalDialog.UpdateEngineerPersonalEvent.class,
                this::saveEngineer);
        engineerPersonalDialog.open();
    }

    private void handleCreateDepartment(ClickEvent<Button> buttonClickEvent) {
        DepartmentDialog departmentDialog = new DepartmentDialog(hospitalService.getAll());
        departmentDialog.addListener(DepartmentDialog.UpdateDepartmentEvent.class, this::handleCreateDepartment);
        departmentDialog.open();
    }

    private void handleCreateMedic(ClickEvent<Button> buttonClickEvent) {
        MedicPersonalDialog medicPersonalDialog = new MedicPersonalDialog(medicPersonalService,
                hospitalService.getAll());
        medicPersonalDialog.addListener(MedicPersonalDialog.UpdateMedicPersonalEvent.class, this::saveMedic);
        medicPersonalDialog.open();
    }

    private void handleCreateEquipment(ClickEvent<Button> buttonClickEvent) {
        EquipmentDialog equipmentDialog = new EquipmentDialog(hospitalService.getAll());
        equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::saveEquipment);
        equipmentDialog.open();
    }

    private void saveHospital(HospitalDialog.UpdateHospitalEvent updateHospitalEvent) {
        try {
            hospitalService.save(updateHospitalEvent.getHospital());
            hospitalGrid.setItems(hospitalService.getAll());
            hospitalGrid.getListDataView().refreshAll();
            Helper.showSuccessNotification(Translator.MSG_HOSPITAL_SUCCESS_SAVED);
            deselectAll();
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
    }

    private void handleCreateDepartment(DepartmentDialog.UpdateDepartmentEvent updateHospitalEvent) {
        try {
            departmentService.save(updateHospitalEvent.getDepartment());
            departmentGrid.setItems(departmentService.getAll());
            departmentGrid.getListDataView().refreshAll();
            Helper.showSuccessNotification(Translator.MSG_DEPARTMENT_SUCCESS_SAVED);
            deselectAll();
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
    }

    private void saveMedic(MedicPersonalDialog.UpdateMedicPersonalEvent updateMedicPersonalEvent) {
        try {
            authenticationService.createTempUserDetails(updateMedicPersonalEvent.getMedicPersonalDTO());
            medicPersonalService.save(updateMedicPersonalEvent.getMedicPersonalDTO());
            Helper.showSuccessNotification(MSG_PERSON_SUCCESS_SAVED);
            medicPersonalGrid.setItems(medicPersonalService.getAll());
            medicPersonalGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
    }

    @Transactional
    private void saveEngineer(EngineerPersonalDialog.UpdateEngineerPersonalEvent updateEngineerPersonalEvent) {
        try {
            authenticationService.createTempUserDetails(updateEngineerPersonalEvent.getEngineerPersonalDTO());
            engineerPersonalService.save(updateEngineerPersonalEvent.getEngineerPersonalDTO());
            Helper.showSuccessNotification(MSG_PERSON_SUCCESS_SAVED);
            engineerPersonalGrid.setItems(engineerPersonalService.getAll());
            engineerPersonalGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
    }

    private void saveEquipment(EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        try {
            equipmentService.save(updateEquipmentEvent.getEquipment());
            Helper.showSuccessNotification(MSG_PERSON_SUCCESS_SAVED);
            equipmentGrid.setItems(equipmentService.getAll());
            equipmentGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            Helper.showErrorNotification(String.format("жопа %s", e.getMessage()));
        }
    }

    private void deselectAll() {
        hospitalGrid.select(null);
        departmentGrid.select(null);
        medicPersonalGrid.select(null);
    }
}
