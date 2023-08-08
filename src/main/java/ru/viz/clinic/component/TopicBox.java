package ru.viz.clinic.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class TopicBox extends VerticalLayout {
    private final HorizontalLayout horizontalLayout;
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private Button hide;

    private TopicBox(
            @NotNull final String label,
            @NotNull final Component... components
    ) {
        Objects.requireNonNull(label);
        Objects.requireNonNull(components);

        this.setWidthFull();
        this.setHeightFull();

        final H3 header = new H3(label);

        horizontalLayout = new HorizontalLayout(header);
        horizontalLayout.setWidthFull();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setPadding(false);

        verticalLayout.setWidthFull();
//        verticalLayout.add(new Hr());
        verticalLayout.add(components);

        verticalLayout.setPadding(false);

        this.addClassName("custom-vertical-layout");
        this.setPadding(true);

        this.add(horizontalLayout, verticalLayout);
    }

    private void appendEye() {
        hide = new Button(new Icon(VaadinIcon.EYE_SLASH));
        hide.getStyle().set("margin-left", "auto");
        hide.addClickListener(this::listenHide);
        horizontalLayout.add(hide);
    }

    private void listenHide(ClickEvent<Button> buttonClickEvent) {
        verticalLayout.setVisible(!verticalLayout.isVisible());
        if (verticalLayout.isVisible()) {
            hide.setIcon(new Icon(VaadinIcon.EYE_SLASH));
        } else {
            hide.setIcon(new Icon(VaadinIcon.EYE));
        }
    }

    public static TopicBox getInstance(
            @NotNull final String label,
            @NotNull final Component... components
    ) {
        return new TopicBox(Objects.requireNonNull(label), Objects.requireNonNull(components));
    }

    public static TopicBox getInstanceWithEye(
            @NotNull final String label,
            @NotNull final Component... components
    ) {

        TopicBox topicBox = getInstance(Objects.requireNonNull(label), Objects.requireNonNull(components));
        topicBox.appendEye();
        return topicBox;
    }
}