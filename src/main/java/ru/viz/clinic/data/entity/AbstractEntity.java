package ru.viz.clinic.data.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractEntity {
    boolean active = true;
    public abstract String getId();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractEntity abstractEntity = (AbstractEntity) o;
        return Objects.equals(getId(), abstractEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public abstract String getEntityName();
}
