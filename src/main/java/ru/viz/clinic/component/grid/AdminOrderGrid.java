package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;

import java.util.Objects;

public class AdminOrderGrid extends OrderGrid {
    public AdminOrderGrid(@NotNull final RecordService recordService) {
        super(Objects.requireNonNull(recordService));
        customize();
    }

    public void customize() {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, order) -> {
            layout.add(showRecords(order));
        })).setHeader("Manage");
    }

}
