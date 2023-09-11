package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.AbstractEntity;
import ru.viz.clinic.data.entity.Equipment;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class RUDGrid<E extends AbstractEntity> extends AbstractGrid<E> {
    protected void addActionColumn(@NotNull final Function<E, Optional<Button[]>> activeButtons) {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, e) -> {
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            activeButtons.apply(e).ifPresent(layout::add);
        })).setAutoWidth(true);
    }

    protected void addActionColumn() {
        addActionColumn(this::getActionButton);
    }

    private Optional<Button[]> getActionButton(@NotNull final E e) {
        if (e.isActive()) {
            return Optional.of(new Button[]{editButton(e), deleteButton(e), setActivateButton(e)});
        } else {
            return Optional.of(new Button[]{deleteButton(e), setActivateButton(e)});
        }
    }
}