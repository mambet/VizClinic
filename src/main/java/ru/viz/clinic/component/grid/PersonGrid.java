package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.help.Translator;

import static ru.viz.clinic.help.Translator.*;

public class PersonGrid<T extends Personal> extends Grid<T> {
    public PersonGrid() {
        this.addColumn(Personal::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(Personal::getUsername).setHeader(HDR_USER).setAutoWidth(true);
        this.addColumn(Personal::getFirstName).setHeader(HDR_FIRST_NAME).setAutoWidth(true);
        this.addColumn(Personal::getLastName).setHeader(HDR_LAST_NAME).setAutoWidth(true);
        this.addColumn(Personal::getPhone).setHeader(HDR_PHONE).setAutoWidth(true);
        this.addColumn(Personal::getEmail).setHeader(HDR_MAIL).setAutoWidth(true);
        this.addColumn(Personal::getBirthDate).setHeader(HDR_BIRTHDAY).setAutoWidth(true);
        this.addColumn(person -> person.getGender() != null ? person.getGender().getGenderAsString() : Strings.EMPTY)
                .setHeader(HDR_GENDER);
        this.setAllRowsVisible(true);
        this.setSelectionMode(Grid.SelectionMode.NONE);
        this.addClassName("primary");
    }

    public static class PersonFilter<T extends Personal> {
        public final GridListDataView<T> dataView;
        private String firstName;
        private String email;
        private String userName;

        public PersonFilter(GridListDataView<T> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
            this.dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.email = email;
            this.dataView.refreshAll();
        }

        public void setUserName(String userName) {
            this.userName = userName;
            this.dataView.refreshAll();
        }

        public boolean test(T person) {
            boolean matchesFirstname = matches(person.getFirstName(), firstName);
            boolean matchesEmail = matches(person.getEmail(), email);
            boolean matchesUsername = matches(person.getUsername(),
                    userName);

            return matchesFirstname && matchesEmail && matchesUsername;
        }

        private boolean matches(
                String value,
                String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}