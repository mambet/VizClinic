package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.repository.EngineerRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static ru.viz.clinic.converter.EntityToStringConverter.convertToPresentation;
import static ru.viz.clinic.help.Helper.*;

@Service
@Slf4j
public class EngineerService extends CommonPersonalService<Engineer, EngineerRepository> {
    private static final String UPDATE_SUCCESS_MSG_FORMAT = "Инженер '%s' успешно обновлен";
    private static final String UPDATE_ERR_MSG_FORMAT = "Ошибка при обновлении инженера '%s'";
    private static final String CREATE_SUCCESS_MSG_FORMAT = "Инженер '%s' успешно создан";
    private static final String CREATE_ERR_MSG_FORMAT = "Ошибка при создании инженера '%s'";
    private static final String DELETE_SUCCESS_MSG_FORMAT = "Инженер '%s' успешно удален";
    private static final String DELETE_ERR_MSG_FORMAT = "Ошибка при удалении инженера '%s'";
    private static final String ACTIVATE_SUCCESS_MSG_FORMAT = "Инженер '%s' успешно активирован";
    private static final String ACTIVATE_ERR_MSG_FORMAT = "Ошибка при активации инженера '%s'";
    private static final String DEACTIVATE_SUCCESS_MSG_FORMAT = "Инженер '%s' успешно деактивирован";
    private static final String DEACTIVATE_ERR_MSG_FORMAT = "Ошибка при деактивации инженера '%s'";
    private static final String ACTIVE_READY_ORDERS_AS_DESTINATION = "Инженер еще в активных заявках в состоянии 'ожидание' '%s' как запланированный";
    private static final String ACTIVE_WORKING_ORDERS_AS_WORKER = "Инженер еще в активных заявках в состоянии 'в работе' '%s' как выполняющий";
    private static final String ACTIVE_ORDERS_AS_DESTINATION = "Инженер еще в активных заявках '%s' как запланированный";
    private static final String INACTIVE_ORDERS_AS_DESTINATION = "Инженер еще в неактивных заявках '%s' как запланированный";
    private static final String ACTIVE_ORDERS_AS_WORKER = "Инженер еще в активных заявках '%s' как выполняющий";
    private static final String INACTIVE_ORDERS_AS_WORKER = "Инженер еще в неактивных заявках '%s' как выполняющий";
    private static final String ACTIVE_ORDERS_AS_FINISHER = "Инженер еще в активных заявках '%s' как закончивший";
    private static final String INACTIVE_ORDERS_AS_FINISHER = "Инженер еще в неактивных заявках '%s' как закончивший";
    private static final String HOSPITAL_NOT_ACTIVE_MESSAGE = "Клиника '%s' не активна";

    public EngineerService(
            @NotNull final EngineerRepository engineerPersonalRepository,
            @NotNull final CustomTransactionalPersonalService<Engineer, EngineerRepository> customTransactionalPersonalService,
            @NotNull final OrderService orderService,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Objects.requireNonNull(engineerPersonalRepository),
                Objects.requireNonNull(customTransactionalPersonalService),
                Objects.requireNonNull(orderService),
                Objects.requireNonNull(authenticationService));
    }

    public Set<Engineer> getActiveByHospitalId(@NotNull final Long hospitalId) {
        return repository.getByHospitalIdAndActiveIs(Objects.requireNonNull(hospitalId), true);
    }

    public Set<Engineer> getInactiveByHospitalId(@NotNull final Long hospitalId) {
        return repository.getByHospitalIdAndActiveIs(Objects.requireNonNull(hospitalId), false);
    }

    @Override
    public boolean isReadyToActivate(final Engineer engineer) {
        return isHospitalActive(engineer);
    }

    @Override
    public boolean isReadyToDeactivate(final Engineer engineer) {
        return !isInActiveReadyOrderAsDestination(engineer) && !isInActiveWorkingOrderAsOwner(engineer);
    }

    @Override
    public boolean isReadyToDelete(final Engineer engineer) {
        return !isInActiveOrders(engineer) && !isInInactiveOrders(engineer);
    }

    @Override
    public void showUpdateSuccessMessage(final Engineer engineer) {
        formatAndShowSuccessMessage(UPDATE_SUCCESS_MSG_FORMAT, engineer);
    }

    @Override
    public void showUpdateErrMessage(final Engineer engineer) {
        formatAndShowErrorMessage(UPDATE_ERR_MSG_FORMAT, engineer);
    }

    @Override
    public void showCreateSuccessMessage(final Engineer engineer) {
        formatAndShowSuccessMessage(CREATE_SUCCESS_MSG_FORMAT, engineer);
    }

    @Override
    public void showCreateErrMessage(final Engineer engineer) {
        formatAndShowErrorMessage(CREATE_ERR_MSG_FORMAT, engineer);
    }

    @Override
    public void showDeleteSuccessMessage(final Engineer engineer) {
        formatAndShowSuccessMessage(DELETE_SUCCESS_MSG_FORMAT, engineer);
    }

    @Override
    public void showDeleteErrMessage(final Engineer engineer) {
        formatAndShowErrorMessage(DELETE_ERR_MSG_FORMAT, engineer);
    }

    @Override
    public void showActivateSuccessMessage(final Engineer engineer) {
        formatAndShowSuccessMessage(ACTIVATE_SUCCESS_MSG_FORMAT, engineer);
    }

    @Override
    public void showActivateErrMessage(final Engineer engineer) {
        formatAndShowErrorMessage(ACTIVATE_ERR_MSG_FORMAT, engineer);
    }

    @Override
    public void showDeactivateSuccessMessage(final Engineer engineer) {
        formatAndShowSuccessMessage(DEACTIVATE_SUCCESS_MSG_FORMAT, engineer);
    }

    @Override
    public void showDeactivateErrMessage(final Engineer engineer) {
        formatAndShowErrorMessage(DEACTIVATE_ERR_MSG_FORMAT, engineer);
    }

    private boolean isInActiveReadyOrderAsDestination(final Engineer engineer) {
        return isInOrdersAsDestination(engineer,
                getOrderService().getActiveByHospitalIdAndByOrderState(engineer.getHospital().getId(),
                        OrderState.READY), ACTIVE_READY_ORDERS_AS_DESTINATION);
    }

    private boolean isInActiveWorkingOrderAsOwner(final Engineer engineer) {
        return isInOrdersAsOwner(engineer,
                getOrderService().getActiveByHospitalIdAndByOrderState(engineer.getHospital().getId(),
                        OrderState.WORKING), ACTIVE_WORKING_ORDERS_AS_WORKER);
    }

    private boolean isInActiveOrders(final Engineer engineer) {
        final List<Order> inactiveByHospitalId = getOrderService().getInactiveByHospitalId(engineer.getHospital().getId());
        return isInOrdersAsDestination(engineer, inactiveByHospitalId, ACTIVE_ORDERS_AS_DESTINATION) ||
                isInOrdersAsOwner(engineer, inactiveByHospitalId, ACTIVE_ORDERS_AS_WORKER) ||
                isInOrdersAsFinisher(engineer, inactiveByHospitalId, ACTIVE_ORDERS_AS_FINISHER);
    }

    private boolean isInInactiveOrders(final Engineer engineer) {
        final List<Order> inactiveByHospitalId = getOrderService().getInactiveByHospitalId(engineer.getHospital().getId());
        return isInOrdersAsDestination(engineer, inactiveByHospitalId, INACTIVE_ORDERS_AS_DESTINATION) ||
                isInOrdersAsOwner(engineer, inactiveByHospitalId, INACTIVE_ORDERS_AS_WORKER) ||
                isInOrdersAsFinisher(engineer, inactiveByHospitalId, INACTIVE_ORDERS_AS_FINISHER);
    }

    private boolean isInOrdersAsDestination(
            final Engineer engineer,
            final List<Order> orderList,
            final String message
    ) {
        final Set<Order> orders = filterOrders(engineer, orderList, (o, e) -> o.getDestinationEngineers().contains(e));
        if (!orders.isEmpty()) {
            showErrorNotification(String.format(message, convertToPresentation(orders)));
            return true;
        }
        return false;
    }

    private boolean isInOrdersAsOwner(
            final Engineer engineer,
            final List<Order> orderList,
            final String message
    ) {
        final Set<Order> orders = filterOrders(engineer, orderList, (o, e) -> o.getOwnerEngineer().equals(e));
        if (!orders.isEmpty()) {
            showErrorNotification(String.format(message, convertToPresentation(orders)));
            return true;
        }
        return false;
    }

    private boolean isInOrdersAsFinisher(
            final Engineer engineer,
            final List<Order> orderList,
            final String message
    ) {
        final Set<Order> orders = filterOrders(engineer, orderList, (o, e) -> o.getFinishEngineer().equals(e));
        if (!orders.isEmpty()) {
            showErrorNotification(String.format(message, convertToPresentation(orders)));
            return true;
        }
        return false;
    }

    private boolean isHospitalActive(final Engineer engineer) {
        final boolean isHospitalActive = engineer.getHospital().isActive();
        if (!isHospitalActive) {
            formatAndShowErrorMessage(HOSPITAL_NOT_ACTIVE_MESSAGE, engineer.getHospital());
            return false;
        }
        return true;
    }

    private Set<Order> filterOrders(
            final Engineer engineer,
            final List<Order> orders,
            final BiPredicate<Order, Engineer> filter
    ) {
        return orders.stream().filter(order -> filter.test(order, engineer)).collect(Collectors.toSet());
    }
}