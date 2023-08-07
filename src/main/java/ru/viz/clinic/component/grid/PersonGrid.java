package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.help.Translator;

public class PersonGrid<T extends Personal> extends Grid<T> {

    public PersonGrid() {
        this.addColumn(Personal::getUsername).setHeader(Translator.LBL_USER);
        this.addColumn(Personal::getFirstName).setHeader(Translator.LBL_FIRST_NAME);
        this.addColumn(Personal::getLastName).setHeader(Translator.LBL_LAST_NAME);
        this.addColumn(Personal::getPhone).setHeader(Translator.LBL_PHONE);
        this.addColumn(Personal::getBirthDate).setHeader(Translator.LBL_BIRTHDAY);
        this.addColumn(person -> person.getGender() != null ? person.getGender().getGenderAsString() : Strings.EMPTY)
                .setHeader(Translator.LBL_GENDER);
        this.setAllRowsVisible(true);
        this.setSelectionMode(Grid.SelectionMode.NONE);
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