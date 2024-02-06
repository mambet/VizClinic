package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Personal;

import static ru.viz.clinic.help.Helper.getDateRenderer;
import static ru.viz.clinic.help.Translator.*;

public abstract class PersonalGrid<T extends Personal> extends  RUDGrid<T> {
    public PersonalGrid() {
        this.addColumn(Personal::getId).setHeader(HDR_ID).setWidth("7em").setFlexGrow(0);
        this.addColumn(Personal::getUsername).setHeader(HDR_USER).setAutoWidth(true);
        this.addColumn(Personal::getFirstName).setHeader(HDR_FIRST_NAME);
        this.addColumn(Personal::getMiddleName).setHeader(HDR_MIDDLE_NAME);
        this.addColumn(Personal::getLastName).setHeader(HDR_LAST_NAME);
        this.addColumn(Personal::getPhone).setHeader(HDR_PHONE);
        this.addColumn(Personal::getEmail).setHeader(HDR_MAIL);
        this.addColumn(getDateRenderer(Personal::getBirthDate)).setHeader(HDR_BIRTHDAY);
        this.addColumn(person -> person.getGender() != null ? person.getGender().getGenderAsString() : Strings.EMPTY)
                .setAutoWidth(true)
                .setHeader(HDR_GENDER);
        super.addActionColumn();
        this.setAllRowsVisible(true);
        this.setSelectionMode(Grid.SelectionMode.NONE);
    }

    public static class PersonFilter<T extends Personal> {
        public final GridListDataView<T> dataView;
        private String firstName;
        private String email;
        private String userName;

        public PersonFilter(final GridListDataView<T> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setFirstName(final String firstName) {
            this.firstName = firstName;
            this.dataView.refreshAll();
        }

        public void setEmail(final String email) {
            this.email = email;
            this.dataView.refreshAll();
        }

        public void setUserName(final String userName) {
            this.userName = userName;
            this.dataView.refreshAll();
        }

        public boolean test(final T person) {
            final boolean matchesFirstname = matches(person.getFirstName(), firstName);
            final boolean matchesEmail = matches(person.getEmail(), email);
            final boolean matchesUsername = matches(person.getUsername(),
                    userName);

            return matchesFirstname && matchesEmail && matchesUsername;
        }

        private boolean matches(
                final String value,
                final String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}