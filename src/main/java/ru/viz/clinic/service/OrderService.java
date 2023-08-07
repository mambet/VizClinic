package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    // Save or update a order

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Get all hospitals
    @Transactional
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    // Get hospital by ID
    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Delete a hospital by ID
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public List<Order> getByHospital(@NotNull final Long hospitalId) {
        return orderRepository.getByHospital(hospitalId);
    }
    @Transactional
    public List<Order> getByDepartment(@NotNull final Long departmentId) {
        return orderRepository.getByDepartment(departmentId);
    }


}