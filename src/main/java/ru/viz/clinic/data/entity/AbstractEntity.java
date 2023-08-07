package ru.viz.clinic.data.entity;

import java.util.Objects;

public abstract class AbstractEntity {
    protected abstract Long getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity abstractEntity = (AbstractEntity) o;
        return Objects.equals(getId(), abstractEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
