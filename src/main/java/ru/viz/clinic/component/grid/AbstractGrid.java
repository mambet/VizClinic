package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.Objects;

public class AbstractGrid<E> extends Grid<E> {

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(
            final Class<T> eventType,
            final ComponentEventListener<T> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    @Getter
    public abstract static class AbstractGridEvent<T extends Component,  E extends AbstractEntity> extends ComponentEvent<T> {
        private final E entity;
        protected AbstractGridEvent(
                @NotNull final T source,
                @NotNull final E entity
        ) {
            super(Objects.requireNonNull(source), false);
            this.entity = Objects.requireNonNull(entity);
        }
    }
 }