package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.component.grid.EngineerOrderGrid;
import ru.viz.clinic.data.OrderState;
import ru.viz.clinic.data.entity.Engineer;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.repository.OrderRepository;
import ru.viz.clinic.help.Helper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.viz.clinic.help.Translator.ERR_ORDER_SAVED_FAILED;
import static ru.viz.clinic.help.Translator.MSG_ORDER_SUCCESS_SAVED;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(@NotNull final OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    @Transactional
    private Optional<Order> save(@NotNull final Order order) {
        try {
            Optional<Order> savedOrder = Optional.of(orderRepository.save(order));
            Helper.showSuccessNotification(MSG_ORDER_SUCCESS_SAVED);
            return savedOrder;
        } catch (Exception e) {
            Helper.showErrorNotification(ERR_ORDER_SAVED_FAILED);
            log.error("error at saving order with id {}, with error", order.getId(), e);
        }
        return Optional.empty();
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getByHospital(@NotNull final Long hospitalId) {
        return orderRepository.getByHospital(hospitalId);
    }

    public List<Order> getByDepartment(@NotNull final Long departmentId) {
        return orderRepository.getByDepartmentId(departmentId);
    }

    public Optional<Order> adoptOrder(
            @NotNull final Order order,
            @NotNull final Engineer engineer
    ) {
        Objects.requireNonNull(order);
        order.setOwnerEngineer(engineer);
        order.setOrderState(OrderState.WORKING);
        return save(order);
    }

    public Optional<Order> leaveOrder(
            @NotNull final Order order
    ) {
        order.setOwnerEngineer(null);
        order.setOrderState(OrderState.READY);
        return save(order);
    }

    public Optional<Order> createOrder(
            @NotNull final Order order
    ) {
        Objects.requireNonNull(order);
        order.setOwnerEngineer(null);
        order.setOrderState(OrderState.READY);
        return save(order);
    }

    public Optional<Order> updateOrder(
            @NotNull final Order order
    ) {
        return save(order);
    }

    public Optional<Order> closeOrder(
            @NotNull final Order order
    ) {
        Objects.requireNonNull(order);
        order.setOrderState(OrderState.DONE);
        order.setFinishEngineer(order.getOwnerEngineer());
        order.setOwnerEngineer(null);
        order.setEndTime(LocalDateTime.now());
        return save(order);
    }
}