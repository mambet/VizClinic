package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.viz.clinic.data.entity.Medic;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.repository.MedicRepository;
import ru.viz.clinic.security.AuthenticationService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.viz.clinic.help.Helper.formatAndShowErrorMessage;
import static ru.viz.clinic.help.Helper.formatAndShowSuccessMessage;

@Service
@Slf4j
public class MedicService extends CommonPersonalService<Medic, MedicRepository> {
    private static final String DEPARTMENT_NOT_ACTIVE_MESSAGE = "Отделение '%s' не активно";
    private static final String MEDIC_IN_ACTIVE_ORDERS_MESSAGE = "Медик еще в активных заявках '%s'";
    private static final String MEDIC_IN_INACTIVE_ORDERS_MESSAGE = "Медик еще в неактивных заявках '%s'";
    private static final String SUCCESS_UPDATE_MESSAGE = "Медик '%s' успешно обновлен";
    private static final String ERROR_UPDATE_MESSAGE = "Ошибка при обновлении медика '%s'";
    private static final String SUCCESS_CREATE_MESSAGE = "Медик '%s' успешно создан";
    private static final String ERROR_CREATE_MESSAGE = "Ошибка при создании медика '%s'";
    private static final String SUCCESS_DELETE_MESSAGE = "Медик '%s' успешно удален";
    private static final String ERROR_DELETE_MESSAGE = "Ошибка при удалении медика '%s'";
    private static final String SUCCESS_ACTIVATE_MESSAGE = "Медик '%s' успешно активирован";
    private static final String ERROR_ACTIVATE_MESSAGE = "Ошибка при активации медика '%s'";
    private static final String SUCCESS_DEACTIVATE_MESSAGE = "Медик '%s' успешно деактивирован";
    private static final String ERROR_DEACTIVATE_MESSAGE = "Ошибка при деактивации медика '%s'";

    public MedicService(
            @NotNull final MedicRepository medicPersonalRepository,
            @NotNull final CustomTransactionalPersonalService<Medic, MedicRepository> customTransactionalPersonalService,
            @NotNull final OrderService orderService,
            @NotNull final AuthenticationService authenticationService
    ) {
        super(Objects.requireNonNull(medicPersonalRepository),
                Objects.requireNonNull(customTransactionalPersonalService),
                Objects.requireNonNull(orderService),
                Objects.requireNonNull(authenticationService));
    }

    public Set<Medic> getActiveByDepartmentId(
            @NotNull final Long departmentId
    ) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, true);
    }

    public Set<Medic> getInactiveByDepartmentId(
            @NotNull final Long departmentId
    ) {
        return repository.getByDepartmentIdAndActiveIs(departmentId, false);
    }

    @Override
    public boolean isReadyToActivate(final Medic medic) {
        final boolean isDepartmentActive = medic.getDepartment().isActive();
        if (!isDepartmentActive) {
            formatAndShowErrorMessage(DEPARTMENT_NOT_ACTIVE_MESSAGE, medic.getDepartment());
            return false;
        }
        return true;
    }

    @Override
    public boolean isReadyToDeactivate(final Medic medic) {
        return true;
    }

    @Override
    public boolean isReadyToDelete(final Medic medic) {
        return !isInactiveOrder(medic) && !isInInactiveOrder(medic);
    }

    @Override
    public void showUpdateSuccessMessage(final Medic medic) {
        formatAndShowSuccessMessage(SUCCESS_UPDATE_MESSAGE, medic);
    }

    @Override
    public void showUpdateErrMessage(final Medic medic) {
        formatAndShowErrorMessage(ERROR_UPDATE_MESSAGE, medic);
    }

    @Override
    public void showCreateSuccessMessage(final Medic medic) {
        formatAndShowSuccessMessage(SUCCESS_CREATE_MESSAGE, medic);
    }

    @Override
    public void showCreateErrMessage(final Medic medic) {
        formatAndShowErrorMessage(ERROR_CREATE_MESSAGE, medic);
    }

    @Override
    public void showDeleteSuccessMessage(final Medic medic) {
        formatAndShowSuccessMessage(SUCCESS_DELETE_MESSAGE, medic);
    }

    @Override
    public void showDeleteErrMessage(final Medic medic) {
        formatAndShowErrorMessage(ERROR_DELETE_MESSAGE, medic);
    }

    @Override
    public void showActivateSuccessMessage(final Medic medic) {
        formatAndShowSuccessMessage(SUCCESS_ACTIVATE_MESSAGE, medic);
    }

    @Override
    public void showActivateErrMessage(final Medic medic) {
        formatAndShowErrorMessage(ERROR_ACTIVATE_MESSAGE, medic);
    }

    @Override
    public void showDeactivateSuccessMessage(final Medic medic) {
        formatAndShowSuccessMessage(SUCCESS_DEACTIVATE_MESSAGE, medic);
    }

    @Override
    public void showDeactivateErrMessage(final Medic medic) {
        formatAndShowErrorMessage(ERROR_DEACTIVATE_MESSAGE, medic);
    }

    private boolean isInactiveOrder(final Medic medic) {
        final Set<Order> orderSet = getOrderService().getActiveByDepartmentId(medic.getDepartment().getId()).stream()
                .filter(order -> order.getMedic().equals(medic))
                .collect(Collectors.toSet());
        if (!orderSet.isEmpty()) {
            formatAndShowErrorMessage(MEDIC_IN_ACTIVE_ORDERS_MESSAGE, orderSet);
            return true;
        }
        return false;
    }

    private boolean isInInactiveOrder(final Medic medic) {
        final Set<Order> orderSet = getOrderService().getInactiveByDepartmentId(medic.getDepartment().getId()).stream()
                .filter(order -> order.getMedic().equals(medic))
                .collect(Collectors.toSet());
        if (!orderSet.isEmpty()) {
            formatAndShowErrorMessage(MEDIC_IN_INACTIVE_ORDERS_MESSAGE, orderSet);
            return true;
        }
        return false;
    }
}