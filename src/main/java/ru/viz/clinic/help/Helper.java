package ru.viz.clinic.help;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Helper {
    public static void showErrorNotification(String message) {
        extracted(message, NotificationVariant.LUMO_ERROR);
    }

    public static void showSuccessNotification(String message) {
        extracted(message, NotificationVariant.LUMO_SUCCESS);
    }

    private static void extracted(
            String message,
            NotificationVariant lumoStyle
    ) {
        Div text = new Div(new Text(message));
        Notification notification = new Notification();
        notification.addThemeVariants(lumoStyle);
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.add(layout);
        notification.setDuration(15000);
        notification.open();
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
    }
}
