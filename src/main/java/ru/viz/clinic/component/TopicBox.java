package ru.viz.clinic.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TopicBox extends VerticalLayout {
    VerticalLayout verticalLayout = new VerticalLayout();

    public TopicBox(
            @NotNull final String label,
            @NotNull final Component... components
    ) {
        Objects.requireNonNull(label);

        this.setWidthFull();
        final H3 header = new H3();
        header.setText(label);
        verticalLayout.setHeightFull();
        verticalLayout.setWidthFull();
        verticalLayout.add(components);
        this.add(header, new Hr(), verticalLayout);
        this.getThemeList().add("viz-group-box");
        this.getElement().getStyle().set("background", "#f4f5f7");
        this.getElement().getStyle().set("border-radius", "10px");
        this.setPadding(true);
    }
}