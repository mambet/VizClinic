package ru.viz.clinic.help;

public class Regex {
    private Regex() {
    }

    public static final String REGEX_PHONE = "^(((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10})?$";
    public static final String REGEX_EMAIL = "^(([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6})?$";
}
