package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.repository.EquipmentRepository;

import java.util.Objects;
import java.util.Set;

import static ru.viz.clinic.help.Helper.formatAndShowErrorMessage;
import static ru.viz.clinic.help.Helper.formatAndShowSuccessMessage;

@Service
@Slf4j
public class EquipmentService extends CommonEntityService<Equipment, EquipmentRepository> {
    private final OrderService orderService;
    private static final String UPDATE_SUCCESS_MSG_FORMAT = "Оборудование '%s' успешно обновлено";
    private static final String UPDATE_ERR_MSG_FORMAT = "Ошибка при обновлении оборудования '%s'";
    private static final String CREATE_SUCCESS_MSG_FORMAT = "Оборудование '%s' успешно создано";
    private static final String CREATE_ERR_MSG_FORMAT = "Ошибка при создании оборудования '%s'";
    private static final String DELETE_SUCCESS_MSG_FORMAT = "Оборудование '%s' успешно удалено";
    private static final String DELETE_ERR_MSG_FORMAT = "Ошибка при удалении оборудования '%s'";
    private static final String ACTIVATE_SUCCESS_MSG_FORMAT = "Оборудование '%s' успешно активировано";
    private static final String ACTIVATE_ERR_MSG_FORMAT = "Ошибка при активации оборудования '%s'";
    private static final String DEACTIVATE_SUCCESS_MSG_FORMAT = "Оборудование '%s' успешно деактивировано";
    private static final String DEACTIVATE_ERR_MSG_FORMAT = "Ошибка при деактивации оборудования '%s'";
    private static final String DEPARTMENT_NOT_ACTIVE_MSG_FORMAT = "Отделение '%s' не активно";
    private static final String ACTIVE_ORDERS_EXIST_MSG_FORMAT = "Еще есть активные заявки '%s'";
    private static final String INACTIVE_ORDERS_EXIST_MSG_FORMAT = "Еще есть не активные заявки '%s'";

    public EquipmentService(
            @NotNull final EquipmentRepository equipmentRepository,
            @NotNull final OrderService orderService
    ) {
        super(equipmentRepository);
        this.orderService = Objects.requireNonNull(orderService);
    }

    public Set<Equipment> getActiveByDepartmentId(@NotNull final String departmentId) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, true);
    }

    public Set<Equipment> getInactiveByDepartmentId(@NotNull final String departmentId) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, false);
    }

    @Override
    public boolean isReadyToActivate(@NotNull final Equipment equipment) {
        return isDepartmentActive(equipment);
    }

    @Override
    public boolean isReadyToDeactivate(@NotNull final Equipment equipment) {
        return isActiveOrderFree(equipment);
    }

    @Override
    public boolean isReadyToDelete(@NotNull final Equipment equipment) {
        return isActiveOrderFree(equipment) && isInactiveOrderFree(equipment);
    }

    @Override
    public void showUpdateSuccessMessage(@NotNull final Equipment equipment) {
        formatAndShowSuccessMessage(UPDATE_SUCCESS_MSG_FORMAT, equipment);
    }

    @Override
    public void showUpdateErrMessage(@NotNull final Equipment equipment) {
        formatAndShowErrorMessage(UPDATE_ERR_MSG_FORMAT, equipment);
    }

    @Override
    public void showCreateSuccessMessage(@NotNull final Equipment equipment) {
        formatAndShowSuccessMessage(CREATE_SUCCESS_MSG_FORMAT, equipment);
    }

    @Override
    public void showCreateErrMessage(@NotNull final Equipment equipment) {
        formatAndShowErrorMessage(CREATE_ERR_MSG_FORMAT, equipment);
    }

    @Override
    public void showDeleteSuccessMessage(@NotNull final Equipment equipment) {
        formatAndShowSuccessMessage(DELETE_SUCCESS_MSG_FORMAT, equipment);
    }

    @Override
    public void showDeleteErrMessage(@NotNull final Equipment equipment) {
        formatAndShowErrorMessage(DELETE_ERR_MSG_FORMAT, equipment);
    }

    @Override
    public void showActivateSuccessMessage(@NotNull final Equipment equipment) {
        formatAndShowSuccessMessage(ACTIVATE_SUCCESS_MSG_FORMAT, equipment);
    }

    @Override
    public void showActivateErrMessage(@NotNull final Equipment equipment) {
        formatAndShowErrorMessage(ACTIVATE_ERR_MSG_FORMAT, equipment);
    }

    @Override
    public void showDeactivateSuccessMessage(@NotNull final Equipment equipment) {
        formatAndShowSuccessMessage(DEACTIVATE_SUCCESS_MSG_FORMAT, equipment);
    }

    @Override
    public void showDeactivateErrMessage(@NotNull final Equipment equipment) {
        formatAndShowErrorMessage(DEACTIVATE_ERR_MSG_FORMAT, equipment);
    }

    private boolean isDepartmentActive(@NotNull final Equipment equipment) {
        final boolean isDepartmentActive = equipment.getDepartment().isActive();
        if (!isDepartmentActive) {
            formatAndShowErrorMessage(DEPARTMENT_NOT_ACTIVE_MSG_FORMAT, equipment.getDepartment());
        }
        return isDepartmentActive;
    }

    private boolean isActiveOrderFree(final Equipment equipment) {
        final Set<Order> orderSet = orderService.getActiveByEquipmentId(Objects.requireNonNull(equipment).getId());
        final boolean isEmpty = orderSet.isEmpty();
        if (!isEmpty) {
            formatAndShowErrorMessage(ACTIVE_ORDERS_EXIST_MSG_FORMAT, orderSet);
        }
        return isEmpty;
    }

    private boolean isInactiveOrderFree(final Equipment equipment) {
        final Set<Order> orderSet = orderService.getInactiveByEquipmentId(
                Objects.requireNonNull(equipment).getId());
        final boolean isEmpty = orderSet.isEmpty();
        if (!isEmpty) {
            formatAndShowErrorMessage(INACTIVE_ORDERS_EXIST_MSG_FORMAT, orderSet);
        }
        return isEmpty;
    }
}