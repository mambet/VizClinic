package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static ru.viz.clinic.converter.EntityToStringConverter.convertToPresentation;
import static ru.viz.clinic.help.Translator.*;
import static ru.viz.clinic.help.Translator.TTP_DELETE_ENTITY;

public abstract class AbstractGrid<E extends AbstractEntity> extends Grid<E> {
    public AbstractGrid() {
        this.addClassName("select");
        this.setPartNameGenerator(e -> {
            if (e.isActive()) {
                return "active";
            } else {
                return "inactive";
            }
        });
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(
            final Class<T> eventType,
            final ComponentEventListener<T> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    @Getter
    public abstract static class AbstractGridEvent<T extends Component, E extends AbstractEntity> extends ComponentEvent<T> {
        private final E entity;

        protected AbstractGridEvent(
                @NotNull final T source,
                @NotNull final E entity
        ) {
            super(Objects.requireNonNull(source), false);
            this.entity = Objects.requireNonNull(entity);
        }
    }

    protected Button setActivateButton(@NotNull final E entity) {
        Objects.requireNonNull(entity);
        final Button button;
        if (entity.isActive()) {
            button = new Button(new Icon(VaadinIcon.CHECK));
            button.setTooltipText(TTP_DEACTIVATE_ENTITY);
            button.addClickListener(e -> setEntityActive(entity, false));
        } else {
            button = new Button(new Icon(VaadinIcon.CHECK_CIRCLE));
            button.setTooltipText(TTP_ACTIVATE_ENTITY);
            button.addClickListener(e -> setEntityActive(entity, true));
        }
        return button;
    }

    protected Button editButton(@NotNull final E entity) {
        Objects.requireNonNull(entity);
        final Button button = new Button(new Icon(VaadinIcon.EDIT));
        button.setTooltipText(TTP_UPDATE_ENTITY);
        button.addClickListener(e -> updateEntity(entity));
        return button;
    }

    protected Button deleteButton(@NotNull final E entity) {
        Objects.requireNonNull(entity);
        final Button button = new Button(new Icon(VaadinIcon.TRASH));
        button.setTooltipText(TTP_DELETE_ENTITY);
        button.addClickListener(e -> deleteEntity(entity));
        return button;
    }

    protected abstract void setEntityActive(
            E e,
            boolean b
    );

    protected abstract void deleteEntity(E e);

    protected abstract void updateEntity(E e);

    <T extends AbstractEntity> ComponentRenderer<Span, E> getStyled(final Function<E, T> getter) {
        return new ComponentRenderer<>(order -> getSpan(order, getter.apply(order)));
    }

    <T extends AbstractEntity> ComponentRenderer<VerticalLayout, E> geStyledForList(final Function<E, Set<T>> getter) {
        return new ComponentRenderer<>(order ->
        {
            final VerticalLayout layout = new VerticalLayout();
            getter.apply(order).forEach(t -> {
                layout.add(getSpan(order, t));
            });
            return layout;
        });
    }

    private <T extends AbstractEntity> Span getSpan(
            final E row,
            final T cell
    ) {
        if (cell == null || row == null) {
            return new Span();
        }
        final Span span = new Span(convertToPresentation(cell));

        if (row.isActive() && !cell.isActive()) {
            span.addClassName("inactive");
        } else if (!row.isActive() && cell.isActive()) {
            span.addClassName("active");
        }
        return span;
    }
}