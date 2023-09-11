package ru.viz.clinic.data;

public enum EventType {
    START_ORDER("создал"),
    UPDATE_ORDER("изменил"),
    ADOPT_ORDER("принял"),
    NOTE("прокомментировал"),
    LIVE_ORDER("вернул"),
    FINISH_ORDER("закрыл");
    final String value;

    EventType(final String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}