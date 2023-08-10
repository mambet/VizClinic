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
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.dialog.*;
import ru.viz.clinic.component.grid.*;
import ru.viz.clinic.component.TopicBox;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.service.*;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.MainLayout;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static ru.viz.clinic.help.Translator.*;

@RolesAllowed("ADMIN")
@PageTitle("Personal")
@Route(value = "Personal", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Slf4j
public class PersonalView extends VerticalLayout {
    private final AuthenticationService authenticationService;
    private final HospitalService hospitalService;
    private final EngineerService engineerService;
    private final DepartmentService departmentService;
    private final MedicService medicService;
    private final EquipmentService equipmentService;
    private final Grid<Hospital> hospitalGrid;
    private final MedicGrid medicGrid;
    private final EngineerGrid engineerGrid;
    private final DepartmentGrid departmentGrid;
    private final EquipmentGrid equipmentGrid;

    public PersonalView(
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
        this.medicGrid = new MedicGrid();
        this.engineerGrid = new EngineerGrid();
        this.departmentGrid = new DepartmentGrid();
        this.equipmentGrid = new EquipmentGrid();

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

    private void hospitalSelect(SelectionEvent<Grid<Hospital>, Hospital> gridHospitalSelectionEvent) {
        AtomicReference<Hospital> hospitalAtomic = new AtomicReference<>();
        gridHospitalSelectionEvent.getFirstSelectedItem()
                .ifPresentOrElse(hospitalAtomic::set, () -> hospitalAtomic.set(null));

        departmentGrid.select(null);
        departmentGrid.setHospitalFilterParameter(hospitalAtomic.get());
        medicGrid.setHospitalFilterParameter(hospitalAtomic.get());
        engineerGrid.setHospitalFilterParameter(hospitalAtomic.get());
        equipmentGrid.setHospitalFilterParameter(hospitalAtomic.get());
    }

    private void departmentSelect(SelectionEvent<Grid<Department>, Department> gridDepartmentSelectionEvent) {
        AtomicReference<Department> departmentAtomic = new AtomicReference<>();
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
        try {
            HospitalDialog hospitalDialog = new HospitalDialog();
            hospitalDialog.addListener(HospitalDialog.UpdateHospitalEvent.class, this::saveHospital);
            hospitalDialog.open();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void handleCreateEngineer(ClickEvent<Button> buttonClickEvent) {
        try {
            EngineerDialog engineerDialog = new EngineerDialog(engineerService, hospitalService.getAll());
            engineerDialog.addListener(EngineerDialog.UpdateEngineerPersonalEvent.class,
                    this::saveEngineer);
            engineerDialog.open();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void handleCreateDepartment(ClickEvent<Button> buttonClickEvent) {
        try {
            DepartmentDialog departmentDialog = new DepartmentDialog(hospitalService.getAll());
            departmentDialog.addListener(DepartmentDialog.UpdateDepartmentEvent.class, this::handleCreateDepartment);
            departmentDialog.open();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void handleCreateMedic(ClickEvent<Button> buttonClickEvent) {
        try {
            MedicDialog medicDialog = new MedicDialog(medicService, hospitalService.getAll(), departmentService);
            medicDialog.addListener(MedicDialog.UpdateMedicPersonalEvent.class, this::saveMedic);
            medicDialog.open();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void handleCreateEquipment(ClickEvent<Button> buttonClickEvent) {
        try {
            EquipmentDialog equipmentDialog = new EquipmentDialog(hospitalService.getAll(), departmentService);
            equipmentDialog.addListener(EquipmentDialog.UpdateEquipmentEvent.class, this::saveEquipment);
            equipmentDialog.open();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void saveHospital(HospitalDialog.UpdateHospitalEvent updateHospitalEvent) {
        try {
            hospitalService.save(updateHospitalEvent.getHospital());
            hospitalGrid.setItems(hospitalService.getAll());
            hospitalGrid.getListDataView().refreshAll();
            Helper.showSuccessNotification(Translator.MSG_HOSPITAL_SUCCESS_SAVED);
            deselectAll();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
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
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void saveMedic(MedicDialog.UpdateMedicPersonalEvent updateMedicPersonalEvent) {
        try {
            authenticationService.createTempUserDetails(updateMedicPersonalEvent.getMedicPersonalDTO());
            medicService.save(updateMedicPersonalEvent.getMedicPersonalDTO());
            Helper.showSuccessNotification(MSG_MEDIC_SUCCESS_SAVED);
            medicGrid.setItems(medicService.getAll());
            medicGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    @Transactional
    private void saveEngineer(EngineerDialog.UpdateEngineerPersonalEvent updateEngineerPersonalEvent) {
        try {
            authenticationService.createTempUserDetails(updateEngineerPersonalEvent.getEngineerPersonalDTO());
            engineerService.save(updateEngineerPersonalEvent.getEngineerPersonalDTO());
            Helper.showSuccessNotification(MSG_ENGINEER_SUCCESS_SAVED);
            engineerGrid.setItems(engineerService.getAll());
            engineerGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void saveEquipment(EquipmentDialog.UpdateEquipmentEvent updateEquipmentEvent) {
        try {
            equipmentService.save(updateEquipmentEvent.getEquipment());
            Helper.showSuccessNotification(MSG_EQUIPMENT_SUCCESS_SAVED);
            equipmentGrid.setItems(equipmentService.getAll());
            equipmentGrid.getListDataView().refreshAll();
            deselectAll();
        } catch (Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка");
        }
    }

    private void deselectAll() {
        hospitalGrid.select(null);
        departmentGrid.select(null);
        medicGrid.select(null);
    }
}
