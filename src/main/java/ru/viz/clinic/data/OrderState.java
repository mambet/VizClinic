package ru.viz.clinic.data;

public enum OrderState {
    READY("В очереди"), WORKING("В роботе"), DONE("Закончен");

    final String value;
    OrderState(String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
