package ru.viz.clinic.service;

import com.vaadin.flow.component.UI;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.viz.clinic.help.Helper.*;

@Service
@Slf4j
public class OrderService extends CommonEntityService<Order, OrderRepository> {
    private static final String UPDATE_SUCCESS_MESSAGE = "Заявка '%s' успешно обновлена";
    private static final String UPDATE_ERROR_MESSAGE = "ошибка при обновлении заявки '%s'";
    private static final String CREATE_SUCCESS_MESSAGE = "Заявка '%s' успешно создана";
    private static final String CREATE_ERROR_MESSAGE = "ошибка при создании заявки '%s'";
    private static final String DELETE_SUCCESS_MESSAGE = "Заявка '%s' успешно удалена";
    private static final String DELETE_ERROR_MESSAGE = "ошибка при удалении заявки '%s'";
    private static final String ACTIVATE_SUCCESS_MESSAGE = "Заявка '%s' успешно активирована";
    private static final String ACTIVATE_ERROR_MESSAGE = "ошибка при активации заявки '%s'";
    private static final String DEACTIVATE_SUCCESS_MESSAGE = "Заявка '%s' успешно деактивирована";
    private static final String DEACTIVATE_ERROR_MESSAGE = "ошибка при деактивации заявки '%s'";
    private static final String ADOPT_SUCCESS_MESSAGE = "Заявка '%s' успешно передана в работу";
    private static final String ADOPT_ERROR_MESSAGE = "ошибка при передачи заявки '%s' в работу";
    private static final String LEAVE_SUCCESS_MESSAGE = "Заявка '%s' успешно возвращена";
    private static final String LEAVE_ERROR_MESSAGE = "ошибка при возврате заявки '%s' в работу";
    private static final String CLOSE_SUCCESS_MESSAGE = "Заявка '%s' успешно закрыта";
    private static final String CLOSE_ERROR_MESSAGE = "ошибка при закрытии заявки '%s' в работу";
    private static final String EQUIPMENT_NOT_ACTIVE_MESSAGE = "Оборудование '%s' не активно";
    private static final String ORDER_PLANNED_MESSAGE = "Заявка '%s' запланирована";
    private static final String ORDER_IN_WORK_MESSAGE = "Заявка '%s' в работе";
    private static final String CANNOT_UPDATE_MESSAGE = "Заявка %s не может быть изменена";
    private static final String CANNOT_ADOPT_MESSAGE = "Заявка %s не может быть взята в работу";
    private static final String CANNOT_LEAVE_MESSAGE = "Заявка %s не может быть отменена";
    private static final String CANNOT_CLOSE_MESSAGE = "Заявка %s не может быть закрыта";
    private final EmailServiceImpl emailService;

    public OrderService(
            @NotNull final OrderRepository orderRepository,
            @NotNull final EmailServiceImpl emailService
    ) {
        super(orderRepository);
        this.emailService = Objects.requireNonNull(emailService);
    }

    // need for extern services
    public List<Order> getActiveByHospitalId(@NotNull final String hospitalId) {
        return repository.getByHospitalIdAndActiveIs(hospitalId, true);
    }

    public List<Order> getInactiveByHospitalId(@NotNull final String hospitalId) {
        return repository.getByHospitalIdAndActiveIs(hospitalId, false);
    }

    public Set<Order> getActiveByEquipmentId(@NotNull final String equipmentId) {
        return repository.getByEquipmentIdAndActiveIs(Objects.requireNonNull(equipmentId), true);
    }

    public Set<Order> getInactiveByEquipmentId(@NotNull final String equipmentId) {
        return repository.getByEquipmentIdAndActiveIs(Objects.requireNonNull(equipmentId), false);
    }

    public List<Order> getActiveByHospitalIdAndByOrderState(
            @NotNull final String hospitalId,
            @NotNull final OrderState orderState
    ) {
        return repository.getByHospitalIdAndActiveIsAndOrderStateIs(hospitalId, true, orderState);
    }

    public List<Order> getActiveByDepartmentId(@NotNull final String departmentId) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, true);
    }

    public List<Order> getInactiveByDepartmentId(@NotNull final String departmentId) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, false);
    }

    //common for entities
    @Override
    public boolean isReadyToActivate(final Order order) {
        final boolean isDepartmentActive = order.getEquipment().isActive();
        if (!isDepartmentActive) {
            formatAndShowErrorMessage(EQUIPMENT_NOT_ACTIVE_MESSAGE, order.getEquipment());
            return false;
        }
        return true;
    }

    @Override
    public boolean isReadyToDeactivate(final Order order) {
        if (order.getOrderState() == OrderState.READY) {
            formatAndShowErrorMessage(ORDER_PLANNED_MESSAGE, order);
            return false;
        }
        if (order.getOrderState() == OrderState.WORKING) {
            formatAndShowErrorMessage(ORDER_IN_WORK_MESSAGE, order);
            return false;
        }
        return true;
    }

    @Override
    public boolean isReadyToDelete(final Order order) {
        if (order.getOrderState() == OrderState.WORKING) {
            formatAndShowErrorMessage(ORDER_IN_WORK_MESSAGE, order);
            return false;
        }
        return true;
    }

    @Override
    public void showUpdateSuccessMessage(final Order order) {
        formatAndShowSuccessMessage(UPDATE_SUCCESS_MESSAGE, order);
    }

    @Override
    public void showUpdateErrMessage(final Order order) {
        formatAndShowErrorMessage(UPDATE_ERROR_MESSAGE, order);
    }

    @Override
    public void showCreateSuccessMessage(final Order order) {
        formatAndShowSuccessMessage(CREATE_SUCCESS_MESSAGE, order);
    }

    @Override
    public void showCreateErrMessage(final Order order) {
        formatAndShowErrorMessage(CREATE_ERROR_MESSAGE, order);
    }

    @Override
    public void showDeleteSuccessMessage(final Order order) {
        formatAndShowSuccessMessage(DELETE_SUCCESS_MESSAGE, order);
    }

    @Override
    public void showDeleteErrMessage(final Order order) {
        formatAndShowErrorMessage(DELETE_ERROR_MESSAGE, order);
    }

    @Override
    public void showActivateSuccessMessage(final Order order) {
        formatAndShowSuccessMessage(ACTIVATE_SUCCESS_MESSAGE, order);
    }

    @Override
    public void showActivateErrMessage(final Order order) {
        formatAndShowErrorMessage(ACTIVATE_ERROR_MESSAGE, order);
    }

    @Override
    public void showDeactivateSuccessMessage(final Order order) {
        formatAndShowSuccessMessage(DEACTIVATE_SUCCESS_MESSAGE, order);
    }

    @Override
    public void showDeactivateErrMessage(final Order order) {
        formatAndShowErrorMessage(DEACTIVATE_ERROR_MESSAGE, order);
    }

    // only for order
    public Optional<Order> createOrder(
            @NotNull final Order order
    ) {
        Objects.requireNonNull(order);
        order.setOwnerEngineer(null);
        order.setOrderState(OrderState.READY);
        final Optional<Order> optionalOrder = create(order);
        optionalOrder.ifPresent(this::sendOrderCreatedMessages);
        return optionalOrder;
    }

    private void sendOrderCreatedMessages(final Order order) {
        emailService.sendOrderCreatedMessages(order.getDestinationEngineers(), UI.getCurrent());
    }

    public Optional<Order> updateOrder(@NotNull final Order order) {
        final Optional<Order> optionalOrder = get(order.getId());
        if (optionalOrder.isEmpty()) {
            showEntityNotFoundErrorMessage(order);
            return Optional.empty();
        }
        if (canUpdate(order)) {
            return update(order);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Order> adoptOrder(
            @NotNull final Order order,
            @NotNull final Engineer engineer
    ) {
        Objects.requireNonNull(order);
        final Optional<Order> optionalOrder = get(order.getId());
        if (optionalOrder.isEmpty()) {
            showEntityNotFoundErrorMessage(order);
            return Optional.empty();
        }
        if (canAdopt(optionalOrder.get())) {
            order.setOwnerEngineer(engineer);
            order.setOrderState(OrderState.WORKING);
            return save(order, this::showAdoptSuccessMessage, this::showAdoptErrorMessage);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Order> leaveOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Optional<Order> optionalOrder = get(order.getId());
        if (optionalOrder.isEmpty()) {
            showEntityNotFoundErrorMessage(order);
            return Optional.empty();
        }
        if (canLeave(optionalOrder.get())) {
            order.setOwnerEngineer(null);
            order.setOrderState(OrderState.READY);
            return save(order, this::showLeaveSuccessMessage, this::showLeaveErrorMessage);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Order> closeOrder(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Optional<Order> optionalOrder = get(order.getId());
        if (optionalOrder.isEmpty()) {
            showEntityNotFoundErrorMessage(order);
            return Optional.empty();
        }
        if (canClose(optionalOrder.get())) {
            order.setOrderState(OrderState.DONE);
            order.setFinishEngineer(order.getOwnerEngineer());
            order.setEndTime(LocalDateTime.now());
            return save(order, this::showCloseSuccessMessage, this::showCloseErrorMessage);
        } else {
            return Optional.empty();
        }
    }

    private boolean canUpdate(final Order order) {
        final boolean canUpdate = order.getOrderState() == OrderState.READY;
        if (!canUpdate) {
            formatAndShowSuccessMessage(CANNOT_UPDATE_MESSAGE, order);
        }
        return canUpdate;
    }

    private boolean canAdopt(final Order order) {
        final boolean canAdopt = order.getOrderState() == OrderState.READY;
        if (!canAdopt) {
            formatAndShowSuccessMessage(CANNOT_ADOPT_MESSAGE, order);
        }
        return canAdopt;
    }

    private boolean canLeave(@NotNull final Order order) {
        final boolean canLeave = order.getOrderState() == OrderState.WORKING;
        if (!canLeave) {
            formatAndShowSuccessMessage(CANNOT_LEAVE_MESSAGE, order);
        }
        return canLeave;
    }

    private boolean canClose(@NotNull final Order order) {
        final boolean canClose = order.getOrderState() == OrderState.WORKING;
        if (!canClose) {
            formatAndShowSuccessMessage(CANNOT_CLOSE_MESSAGE, order);
        }
        return canClose;
    }

    private void showAdoptSuccessMessage(@NotNull final Order order) {
        formatAndShowSuccessMessage(ADOPT_SUCCESS_MESSAGE, order);
    }

    private void showAdoptErrorMessage(@NotNull final Order order) {
        formatAndShowErrorMessage(ADOPT_ERROR_MESSAGE, order);
    }

    private void showLeaveSuccessMessage(@NotNull final Order order) {
        formatAndShowSuccessMessage(LEAVE_SUCCESS_MESSAGE, order);
    }

    private void showLeaveErrorMessage(@NotNull final Order order) {
        formatAndShowErrorMessage(LEAVE_ERROR_MESSAGE, order);
    }

    private void showCloseSuccessMessage(@NotNull final Order order) {
        formatAndShowSuccessMessage(CLOSE_SUCCESS_MESSAGE, order);
    }

    private void showCloseErrorMessage(@NotNull final Order order) {
        formatAndShowErrorMessage(CLOSE_ERROR_MESSAGE, order);
    }
}