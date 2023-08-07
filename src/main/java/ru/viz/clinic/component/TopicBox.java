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
    VerticalLayout verticalLayout = new VerticalLayout();
    Button hide = new Button(new Icon(VaadinIcon.EYE_SLASH));

    public TopicBox(
            @NotNull final String label,
            @NotNull final Component... components
    ) {
        Objects.requireNonNull(label);

        this.setWidthFull();
        this.setHeightFull();
        final H3 header = new H3();
        header.setText(label);

        verticalLayout.setWidthFull();
        verticalLayout.add(new Hr());
        verticalLayout.add(components);

        HorizontalLayout horizontalLayout = new HorizontalLayout(header, hide);
        horizontalLayout.setWidthFull();
        hide.getStyle().set("margin-left", "auto");

        this.add(horizontalLayout, verticalLayout);
        this.getThemeList().add("viz-group-box");
//        this.getElement().getStyle().set("background", "#28394e");
        this.getElement().getStyle().set("border-radius", "10px");
        this.setPadding(true);
        hide.addClickListener(this::listenHide);
    }

    private void listenHide(ClickEvent<Button> buttonClickEvent) {
        verticalLayout.setVisible(!verticalLayout.isVisible());
        if(verticalLayout.isVisible())
            hide.setIcon(new Icon(VaadinIcon.EYE_SLASH));
        else
            hide.setIcon(new Icon(VaadinIcon.EYE));

    }
}