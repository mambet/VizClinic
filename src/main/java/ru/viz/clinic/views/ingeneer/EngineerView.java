package ru.viz.clinic.views.ingeneer;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.views.MainLayout;

@PageTitle("EngineerView")
@Route(value = "EngineerView", layout = MainLayout.class)
@RolesAllowed("ENGINEER")
public class EngineerView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public EngineerView() {
        name = new TextField("Your name Engineer");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
             Helper.showErrorNotification("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }

}
