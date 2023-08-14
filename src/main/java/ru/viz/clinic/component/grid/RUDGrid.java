package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.Objects;
import java.util.function.Predicate;

import static ru.viz.clinic.help.Translator.*;

public abstract class RUDGrid<E extends AbstractEntity> extends AbstractGrid<E> {
    protected void addRUDButtons() {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, e) -> {
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            layout.add(udpateButton(e), deleteButton(e));
        })).setAutoWidth(true);
    }

    protected void addRUDButtonsIf(final Predicate<E> haveToShowPredicate) {
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, e) -> {
            if(haveToShowPredicate.test(e)) {
                layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
                layout.add(udpateButton(e), deleteButton(e));
            }
        })).setAutoWidth(true);
    }

    private Button udpateButton(@NotNull final E entity) {
        Objects.requireNonNull(entity);
        final Button button = new Button(new Icon(VaadinIcon.EDIT));
        button.setTooltipText(TTP_UPDATE_ENTITY);
        button.addClickListener(e -> updateEntity(entity));
        return button;
    }

    private Button deleteButton(@NotNull final E entity) {
        Objects.requireNonNull(entity);
        final Button button = new Button(new Icon(VaadinIcon.TRASH));
        button.setTooltipText(TTP_DELETE_ENTITY);
        button.addClickListener(e -> deleteEntity(entity));
        return button;
    }

    protected abstract void updateEntity(E entity);

    protected abstract void deleteEntity(E entity);

}
