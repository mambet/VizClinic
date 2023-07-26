package ru.viz.clinic.data;

public enum Gender {
    M("Муж"), F("Жен");
    final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getGenderAsString() {
        return value;
    }
}
