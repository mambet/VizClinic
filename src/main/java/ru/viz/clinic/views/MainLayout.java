package ru.viz.clinic.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIcon;
import ru.viz.clinic.security.AuthenticationService;
import ru.viz.clinic.views.medic.MedicEquipmentView;
import ru.viz.clinic.views.order.EngineerOrderView;
import ru.viz.clinic.views.admin.AdminView;
import ru.viz.clinic.views.order.MedicOrderView;
import ru.viz.clinic.views.order.AdminOrderView;

import static ru.viz.clinic.help.Translator.*;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {
        private final Class<? extends Component> view;

        public MenuItemInfo(
                final String menuTitle,
                final Component icon,
                final Class<? extends Component> view
        ) {
            this.view = view;
            final RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            final Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }
    }

    private final AuthenticationService authenticationService;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(
            final AuthenticationService authenticationService,
            final AccessAnnotationChecker accessChecker
    ) {
        this.authenticationService = authenticationService;
        this.accessChecker = accessChecker;

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        final Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        final Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        final H1 appName = new H1(DLH_LOGIN_TITLE);
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName);

        final Optional<UserDetails> maybeUser = authenticationService.getUserDetails();
        if (maybeUser.isPresent()) {
            final UserDetails person = maybeUser.get();

            final Avatar avatar = new Avatar(person.getUsername());
            //            StreamResource resource = new StreamResource("profile-pic",
            //                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            //            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            final MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            final MenuItem userName = userMenu.addItem("");
            final Div div = new Div();
            div.add(avatar);
            div.add(person.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem(BTN_CANCEL, e -> {
                authenticationService.logout();
            });

            layout.add(userMenu);
        } else {
            final Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        final Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        final UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
        nav.add(list);

        for (final MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }
        }

        header.add(layout, nav);


        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{
                new MenuItemInfo(MIT_PERSONAL, LineAwesomeIcon.README.create(), AdminView.class), //
                new MenuItemInfo(MIT_ORDER, LineAwesomeIcon.PEOPLE_CARRY_SOLID.create(), MedicOrderView.class), //
                new MenuItemInfo(MIT_ORDER, LineAwesomeIcon.PEOPLE_CARRY_SOLID.create(), EngineerOrderView.class), //
                new MenuItemInfo(MIT_ORDER, LineAwesomeIcon.PEOPLE_CARRY_SOLID.create(), AdminOrderView.class), //
                new MenuItemInfo(MIT_EQUIPMENT, LineAwesomeIcon.ACCUSOFT.create(), MedicEquipmentView.class), //
        };
    }
}
