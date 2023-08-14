package ru.viz.clinic.help;

public class Translator {
    /***
     Buttons
     */
    public static final String BTN_CANCEL = "Выйти";
    public static final String BTN_LOGIN = "Войти";
    public static final String BTN_CONFIRM_CREATE = "Сохранить";
    public static final String BTN_CLOSE_ORDER = "Закрыть заявку";
    public static final String BTN_CONFIRM_CREATE_PLUS = "+ Создать";
    public static final String BTN_CREATE_COMMENT = "Оставить комментарий";
    public static final String BTN_LEAVE_ORDER = "Отказываюсь";
    /***
     Labels
     */
    public static final String LBL_LOGIN_USERNAME = "Пользователь";
    public static final String LBL_LOGIN_PASS = "Пароль";
    public static final String LBL_LOGIN_TEXT = "Вход";
    public static final String LBL_HOSPITAL = "Клиника";
    public static final String LBL_HOSPITAL_NAME = "Наименование клиники";
    public static final String LBL_DEPARTMENT_NAME = "Наименование отделения";
    public static final String LBL_EQUIPMENT_NAME = "Наименование оборудования";
    public static final String LBL_ENGINEER = "Инженер";
    public static final String LBL_EQUIPMENT_NUMBER_1 = "Номер оборудования 1";
    public static final String LBL_EQUIPMENT_NUMBER_2 = "Номер оборудования 2";
    public static final String LBL_EQUIPMENT_DESCRIPTION = "Описание оборудования";
    public static final String LBL_EQUIPMENT_CREATE_DATE = "Дата выпуска";
    public static final String LBL_FIRST_NAME = "Имя";
    public static final String LBL_LAST_NAME = "Фамилия";
    public static final String LBL_BIRTHDAY = "Дата рождения";
    public static final String LBL_GENDER = "Пол";
    public static final String LBL_USER = "Пользователь";
    public static final String LBL_PASSWORD = "Пароль";
    public static final String LBL_PHONE = "Номер телефона";
    public static final String LBL_EMAIL = "Електронная почта";
    public static final String LBL_STREET = "Улица";
    public static final String LBL_CITY = "Город";
    public static final String LBL_POSTAL_CODE = "Индекс";
    public static final String LBL_REGION = "Регион";
    public static final String LBL_COMMENT = "Комментарий";
    /***
     Dialog Header
     */
    public static final String DLH_LOGIN_TITLE = "Клиника";
    public static final String DLH_CREATE_MEDIC = "Создать медработника";
    public static final String DLH_CREATE_HOSPITAL = "Создать клинику";
    public static final String DLH_UPDATE_HOSPITAL = "Изменить клинику";
    public static final String DLH_CREATE_DEPARTMENT = "Создать отделение";
    public static final String DLH_CREATE_ENGINEER = "Создать инженера";
    public static final String DLH_CREATE_EQUIPMENT = "Создать оборудование";
    public static final String DLH_CREATE_ORDER = "Создать заявку";
    public static final String DLH_CLOSE_ORDER = "Закрыть заявку";
    public static final String DLH_HOSPITAL = "Клиника";
    public static final String DLH_DEPARTMENT = "Отделение";
    public static final String DLH_MEDIC = "Медработник";
    public static final String DLH_ENGINEER = "Инженер";
    public static final String DLH_EQUIPMENT = "Оборудование";
    public static final String DLH_ORDER = "Заявки";
    public static final String DLH_COMMENT = "Комментировать";
    public static final String DLH_LEAVE_ORDER = "Отказаться от заявки";
    public static final String DLH_CHANGE_PASS = "Смена пароля";
    /***
     Error Messages
     */
    public static final String ERR_MSG_LOGIN_TITLE = "Имя пользователя или пароль не верны.";
    public static final String ERR_MSG_LOGIN_FAILED = "Неправильное имя пользователя или пароль. Попробуйте ещё раз";
    public static final String ERR_MSG_PHONE_IS_INVALID = "Номер не правильный";
    public static final String ERR_MSG_EMAIL_IS_INVALID = "Почта не правильная";
    public static final String ERR_MSG_USER_NAME_BUSY = "Пользователь уже сущесьвует";
    public static final String ERR_MSG_USER_NAME_IS_SHORT = "Пользователь должен быть минимум 4 знака";
    public static final String ERR_MSG_PASS_NAME_IS_SHORT = "Пароль должен быть минимум 4 знака";
    public static final String ERR_MSG_INVALID_DATA = "Данные введены неверно";
    public static final String ERR_MSG_RECORD_UPDATE_FAILED = "Протокол не обновлен";
    public static final String ERR_MSG_ORDER_ADOPT_FAILED = "Заявка не принята";
    public static final String ERR_ORDER_SAVED_FAILED = "Ошибка при сохранении заявки";
    public static final String ERR_RECORD_SAVED_FAILED = "Ошибка при сохранении протокола";
    public static final String ERR_PASSWORDS_NOT_EQUALS_ERROR = "Пароли не совпадают";
    public static final String ERR_AUTHENTICATION = "Ошибка при смене проля";
    public static final String ERR_BAD_CREDENTIALS = "Пароль не верный";
    public static final String ERR_MSG_BIRTHDAY_IS_AFTER_NOW = "Год рождения не может быть в будущем";
    public static final String ERR_MSG_CREATION_DATE_IS_AFTER_NOW = "Год выпуска не может быть в будущем";
    public static final String ERR_MSG_NO_ENGINEERS = "В вашей больнице нет мастеров";
    public static final String ERR_MSG_NO_EQUIPMENT = "В вашей больнице нет оборудования";
    public static final String ERR_MSG_MEDIC_SUCCESS_SAVED = "ошибка при сохранении медика";
    public static final String ERR_MSG_ENGINEER_SUCCESS_SAVED = "ошибка при сохранении мастера";
    /***
     Messages
     */
    public static final String MSG_LOGIN = "Введите логин и пароль";
    public static final String MSG_HOSPITAL_SUCCESS_SAVED = "Клиника успешно создана";
    public static final String MSG_DEPARTMENT_SUCCESS_SAVED = "Отделение успешно создано";
    public static final String MSG_ENGINEER_SUCCESS_SAVED = "Инженер успешно создан";
    public static final String MSG_MEDIC_SUCCESS_SAVED = "Медработник успешно создан";
    public static final String MSG_EQUIPMENT_SUCCESS_SAVED = "Оборудование успешно создано";
    public static final String MSG_ORDER_SUCCESS_SAVED = "Заявка успешно сохранена";
    public static final String MSG_ORDER_SUCCESS_ADOPT = "Заявка успешно принята";
    public static final String MSG_ORDER_SUCCESS_LEAVED = "Заявка отдана";
    public static final String MSG_ORDER_SUCCESS_MODIFIED = "Заявка успешно изменена";
    public static final String MSG_ORDER_SUCCESS_CLOSED = "Заявка успешно закрыта";
    public static final String MSG_CLOSE_ORDER_QUESTION = "Вы действительно хотите закрыть заявку?";
    public static final String MSG_RECORD_UPDATED = "Протокол обновлен";
    /***
     Menu Items
     */
    public static final String MIT_PERSONAL = "Персонал";
    public static final String MIT_HOSPITAL = "Клиника";
    public static final String MIT_EQUIPMENT = "Оборудование";
    public static final String MIT_ORDER = "Заявки";
    /***
     Table Header
     */
    public static final String HDR_HOSPITAL = "Клиника";
    public static final String HDR_DEPARTMENT = "Отделение";
    public static final String HDR_ADDRESS = "Адресс";
    public static final String HDR_ID = "Номер";
    public static final String HDR_USER = "Пользователь";
    public static final String HDR_FIRST_NAME = "Имя";
    public static final String HDR_LAST_NAME = "Фамилия";
    public static final String HDR_PHONE = "Номер телефона";
    public static final String HDR_MAIL = "Почта";
    public static final String HDR_BIRTHDAY = "Дата рождения";
    public static final String HDR_GENDER = "Пол";
    public static final String HDR_EQUIPMENT = "Оборудование";
    public static final String HDR_EQUIPMENT_NUMBER_1 = "Номер оборудования 1";
    public static final String HDR_EQUIPMENT_NUMBER_2 = "Номер оборудования 2";
    public static final String HDR_EQUIPMENT_CREATION_TIME = "Год выруска";
    public static final String HDR_EQUIPMENT_DESCRIPTION = "Описание оборудования";
    public static final String HDR_DESCRIPTION = "Описание";
    public static final String HDR_ORDER_GIVER = "Заявитель";
    public static final String HDR_CREATE_ORDER = "Создание заявки";
    public static final String HDR_DESTINATION_ENGINEER = "Для мастеров";
    public static final String HDR_STATE_ORDER = "Состояние заявки";
    public static final String HDR_ORDER_TAKER = "Мастер работает";
    public static final String HDR_ORDER_FINISHER = "Мастер закончил";
    public static final String HDR_END_ORDER = "Окончание заявки";
    public static final String HDR_MEDIC = "Медик";
    public static final String HDR_ENGINEER = "Инженер";
    public static final String HDR_MODIFY_ORDER = "Изменение заявки";
    /***
     Tooltip
     */
    public static final String TTP_MODIFY_ORDER = "Изменить";
    public static final String TTP_CREATE_ENTITY = "Создать";
    public static final String TTP_UPDATE_ENTITY = "Изменить";
    public static final String TTP_DELETE_ENTITY = "Удалить";
    public static final String TTP_CLOSE_ORDER = "Закрыть";
    public static final String TTP_ADOPT_ORDER = "Принять";
    public static final String TTP_LEAVE_ORDER = "Вернуть";
    public static final String TTP_COMMENT_ORDER = "Комментировать";

    /***
     Pagetitile
     */
    public static final String PTT_ADMIN = "Пероснал";
    public static final String PTT_EQUIPMENT = "Оборудование";
    public static final String PTT_LOGIN = "Вход";
    public static final String PTT_CHANGE_PASS = "Смена пароля";

    private Translator() {
    }
}
