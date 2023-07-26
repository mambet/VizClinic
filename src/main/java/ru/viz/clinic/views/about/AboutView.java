package ru.viz.clinic.views.about;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.MainLayout;
import ru.viz.clinic.views.login.ChangePassword;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout implements BeforeEnterListener {
    private final AuthenticationService authenticationService;

    public AboutView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("This place intentionally left empty");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        authenticationService.getUserDetails().ifPresent(authenticatedUser -> {
            authenticatedUser.getAuthorities().stream().filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TEMP")).findFirst().ifPresent(grantedAuthority -> {
                getUI().ifPresent(ui -> ui.navigate(ChangePassword.class));
            });
        });
    }
}
