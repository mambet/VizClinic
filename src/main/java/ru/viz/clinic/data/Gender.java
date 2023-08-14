package ru.viz.clinic.data;

public enum Gender {
    M("Муж"), F("Жен");
    final String value;

    Gender(final String value) {
        this.value = value;
    }

    public String getGenderAsString() {
        return value;
    }
}
