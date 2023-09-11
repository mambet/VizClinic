package ru.viz.clinic.converter;

import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.data.entity.AbstractEntity;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityToStringConverter {
    public static String convertToPresentation(final AbstractEntity abstractEntity) {
        if (abstractEntity == null) {
            return Strings.EMPTY;
        }
        return abstractEntity.getEntityName();
    }

    public static String convertToPresentation(final Collection<? extends AbstractEntity> abstractEntities) {
        Objects.requireNonNull(abstractEntities);
        return abstractEntities.stream().map(EntityToStringConverter::convertToPresentation).collect(
                Collectors.joining(", "));
    }

    public static String convertToPresentationAndMarkInactive(final AbstractEntity abstractEntity) {
        if (abstractEntity == null) {
            return Strings.EMPTY;
        }
        String entityName = abstractEntity.getEntityName();
        if (!abstractEntity.isActive()) {
            entityName = "*" + entityName;
        }
        return entityName;
    }

    public static String convertToPresentationAndMarkInactive(final Collection<? extends AbstractEntity> abstractEntities) {
        Objects.requireNonNull(abstractEntities);
        return abstractEntities.stream().map(EntityToStringConverter::convertToPresentation).collect(
                Collectors.joining(", "));
    }
}
