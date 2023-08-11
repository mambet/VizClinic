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
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.ValueProvider;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Helper {
    public static void showErrorNotification(@NotNull final String message) {
        Objects.requireNonNull(message);
        showNotification(message, NotificationVariant.LUMO_ERROR);
    }

    public static void showSuccessNotification(@NotNull final String message) {
        Objects.requireNonNull(message);
        showNotification(message, NotificationVariant.LUMO_SUCCESS);
    }

    private static void showNotification(
            @NotNull final String message,
            @NotNull final NotificationVariant lumoStyle
    ) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(lumoStyle);
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
        notification.setDuration(4500);
        notification.open();
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
    }

    public static <T> LocalDateTimeRenderer<T> getDateTimeRenderer(@NotNull final ValueProvider<T, LocalDateTime> getTime) {
        return new LocalDateTimeRenderer<>(getTime, "dd.MM.yyyy HH:mm");
    }

    public static <T> LocalDateRenderer<T> getDateRenderer(@NotNull final ValueProvider<T, LocalDate> getTime) {
        return new LocalDateRenderer<>(getTime, "dd.MM.yyyy");
    }
}
