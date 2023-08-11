package ru.viz.clinic.converter;

import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.Personal;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PersonalToStringConverter {
    public static String convertToPresentation(Personal personal ) {
        if (personal == null) {
            return Strings.EMPTY;
        }
        return personal.getFirstName().concat(" " + personal.getLastName());
    }

    public static List<String> convertToPresentation(final Collection<? extends Personal> personals) {
        Objects.requireNonNull(personals);
        return personals.stream().map(PersonalToStringConverter::convertToPresentation).toList();
    }
}
