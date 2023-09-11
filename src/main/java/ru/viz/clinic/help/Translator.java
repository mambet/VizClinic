package ru.viz.clinic.help;

public interface Translator {
    String ENTITY_NAME_ADDRESS = "Адрес";
    String ENTITY_NAME_HOSPITAL = "Клиника";
    String ENTITY_NAME_ENGINEER = "Инженер";
    String ENTITY_NAME_DEPARTMENT = "Отделение";
    String ENTITY_NAME_MEDIC = "Медработник";
    String ENTITY_NAME_ADMIN = "Администратор";
    String ENTITY_NAME_EQUIPMENT = "Оборудование";
    String ENTITY_NAME_ORDER = "Заявка";
    String ENTITY_NAME_RECORD = "Запись протокола";
    /***
     Buttons
     */
    String BTN_CANCEL = "Выйти";
    String BTN_LOGIN = "Войти";
    String BTN_CONFIRM_CREATE = "Создать";
    String BTN_CONFIRM_UPDATE = "Изменить";
    String BTN_CLOSE_ORDER = "Закрыть заявку";
    String BTN_CONFIRM_CREATE_PLUS = "+ Создать";
    String BTN_CREATE_COMMENT = "Оставить комментарий";
    String BTN_LEAVE_ORDER = "Отказываюсь";
    /***
     Labels
     */
    String LBL_LOGIN_USERNAME = "Пользователь";
    String LBL_LOGIN_PASS = "Пароль";
    String LBL_LOGIN_TEXT = "Вход";
    String LBL_HOSPITAL = "Клиника";
    String LBL_HOSPITAL_NAME = "Наименование клиники";
    String LBL_DEPARTMENT_NAME = "Наименование отделения";
    String LBL_EQUIPMENT_NAME = "Наименование оборудования";
    String LBL_ENGINEER = "Инженер";
    String LBL_EQUIPMENT_INVENTORY_NUMBER = "Инвентарный номер";
    String LBL_EQUIPMENT_FACTORY_NUMBER = "Заводской номер";
    String LBL_EQUIPMENT_DESCRIPTION = "Описание оборудования";
    String LBL_EQUIPMENT_CREATE_DATE = "Дата выпуска";
    String LBL_EQUIPMENT_COMMISSIONING_DATE = "Дата ввода в эксплуатацию";
    String LBL_FIRST_NAME = "Имя";
    String LBL_LAST_NAME = "Фамилия";
    String LBL_BIRTHDAY = "Дата рождения";
    String LBL_GENDER = "Пол";
    String LBL_USER = "Пользователь";
    String LBL_PASSWORD = "Пароль";
    String LBL_PHONE = "Номер телефона";
    String LBL_EMAIL = "Електронная почта";
    String LBL_STREET = "Улица";
    String LBL_CITY = "Город";
    String LBL_POSTAL_CODE = "Индекс";
    String LBL_REGION = "Регион";
    String LBL_COMMENT = "Комментарий";
    /***
     Dialog Header
     */
    String DLH_LOGIN_TITLE = "Клиника";
    String DLH_CREATE_MEDIC = "Создать медработника";
    String DLH_CREATE_HOSPITAL = "Создать клинику";
    String DLH_UPDATE_HOSPITAL = "Изменить клинику";
    String DLH_CREATE_DEPARTMENT = "Создать отделение";
    String DLH_CREATE_ENGINEER = "Создать инженера";
    String DLH_CREATE_EQUIPMENT = "Создать оборудование";
    String DLH_CREATE_ORDER = "Создать заявку";
    String DLH_CLOSE_ORDER = "Закрыть заявку";
    String DLH_HOSPITAL = "Клиника";
    String DLH_DEPARTMENT = "Отделение";
    String DLH_MEDIC = "Медработник";
    String DLH_ENGINEER = "Инженер";
    String DLH_EQUIPMENT = "Оборудование";
    String DLH_ORDER = "Заявки";
    String DLH_COMMENT = "Комментировать";
    String DLH_LEAVE_ORDER = "Отказаться от заявки";
    String DLH_CHANGE_PASS = "Смена пароля";
    /***
     Error Messages
     */
    String ERR_MSG_LOGIN_TITLE = "Имя пользователя или пароль не верны.";
    String ERR_MSG_LOGIN_FAILED = "Неправильное имя пользователя или пароль. Попробуйте ещё раз";
    String ERR_MSG_PHONE_IS_INVALID = "Номер не правильный";
    String ERR_MSG_EMAIL_IS_INVALID = "Почта не правильная";
    String ERR_MSG_USER_NAME_BUSY = "Пользователь уже сущесьвует";
    String ERR_MSG_USER_NAME_IS_SHORT = "Пользователь должен быть минимум 4 знака";
    String ERR_MSG_PASS_NAME_IS_SHORT = "Пароль должен быть минимум 4 знака";
    String ERR_MSG_INVALID_DATA = "Данные введены неверно";
    String ERR_MSG_RECORD_UPDATE_FAILED = "Протокол не обновлен";
    String ERR_MSG_ORDER_ADOPT_FAILED = "Заявка не принята";
    String ERR_ORDER_SAVED_FAILED = "Ошибка при сохранении заявки";
    String ERR_RECORD_SAVED_FAILED = "Ошибка при сохранении протокола";
    String ERR_PASSWORDS_NOT_EQUALS_ERROR = "Пароли не совпадают";
    String ERR_AUTHENTICATION = "Ошибка при смене проля";
    String ERR_BAD_CREDENTIALS = "Пароль не верный";
    String ERR_MSG_BIRTHDAY_IS_AFTER_NOW = "Год рождения не может быть в будущем";
    String ERR_MSG_CREATION_DATE_IS_AFTER_NOW = "Год выпуска не может быть в будущем";
    String ERR_MSG_NO_ENGINEERS = "В вашей больнице нет мастеров";
    String ERR_MSG_NO_EQUIPMENT = "В вашей больнице нет оборудования";
    String ERR_MSG_MEDIC_SUCCESS_SAVED = "ошибка при сохранении медика";
    String ERR_MSG_ENGINEER_SUCCESS_SAVED = "ошибка при сохранении мастера";
    /***
     Messages
     */
    String MSG_LOGIN = "Введите логин и пароль";
    String MSG_HOSPITAL_SUCCESS_SAVED = "Клиника успешно сохранена";
    String MSG_DEPARTMENT_SUCCESS_SAVED = "Отделение успешно сохранено";
    String MSG_ENGINEER_SUCCESS_SAVED = "Инженер успешно сохранен";
    String MSG_MEDIC_SUCCESS_SAVED = "Медработник успешно сохранен";
    String MSG_EQUIPMENT_SUCCESS_SAVED = "Оборудование успешно сохранено";
    String MSG_ORDER_SUCCESS_SAVED = "Заявка успешно сохранена";
    String MSG_ORDER_SUCCESS_ADOPT = "Заявка успешно принята";
    String MSG_ORDER_SUCCESS_LEAVED = "Заявка отдана";
    String MSG_ORDER_SUCCESS_MODIFIED = "Заявка успешно изменена";
    String MSG_ORDER_SUCCESS_CLOSED = "Заявка успешно закрыта";
    String MSG_CLOSE_ORDER_QUESTION = "Вы действительно хотите закрыть заявку?";
    String MSG_RECORD_UPDATED = "Протокол обновлен";
    String MSG_SUCCESS_SAVE_ENTITY = "Запись '%s %s' спешно сохранена";
    String ERR_MSG_FAILED_SAVE_ENTITY = "Ошибка при сохранении записи '%s %s'";
    String MSG_SUCCESS_DELETE_ENTITY = "Запись '%s %s' спешно удалена";
    String ERR_MSG_FAILED_DELETE_ENTITY = "Ошибка при удалении записи '%s %s'";
    /***
     Menu Items
     */
    String MIT_PERSONAL = "Персонал";
    String MIT_HOSPITAL = "Клиника";
    String MIT_EQUIPMENT = "Оборудование";
    String MIT_ORDER = "Заявки";
    /***
     Table Header
     */
    String HDR_HOSPITAL = "Клиника";
    String HDR_DEPARTMENT = "Отделение";
    String HDR_ADDRESS = "Адресс";
    String HDR_ID = "Номер";
    String HDR_USER = "Пользователь";
    String HDR_FIRST_NAME = "Имя";
    String HDR_LAST_NAME = "Фамилия";
    String HDR_PHONE = "Номер телефона";
    String HDR_MAIL = "Почта";
    String HDR_BIRTHDAY = "Дата рождения";
    String HDR_GENDER = "Пол";
    String HDR_EQUIPMENT = "Оборудование";
    String HDR_EQUIPMENT_INVENTORY_NUMBER = "Инвентарный номер";
    String HDR_EQUIPMENT_FABRIC_NUMBER = "Заводской номер";
    String HDR_EQUIPMENT_CREATION_TIME = "Дата выруска";
    String HDR_EQUIPMENT_COMMISSIONING_TIME = "Дата ввода в эксплуатацию";
    String HDR_EQUIPMENT_DESCRIPTION = "Описание оборудования";
    String HDR_DESCRIPTION = "Описание";
    String HDR_ORDER_GIVER = "Заявитель";
    String HDR_CREATE_ORDER = "Создание заявки";
    String HDR_DESTINATION_ENGINEER = "Для мастеров";
    String HDR_STATE_ORDER = "Состояние заявки";
    String HDR_ORDER_TAKER = "Мастер работает";
    String HDR_ORDER_FINISHER = "Мастер закончил";
    String HDR_END_ORDER = "Окончание заявки";
    String HDR_MEDIC = "Медик";
    String HDR_ENGINEER = "Инженер";
    String HDR_MODIFY_ORDER = "Изменение заявки";
    /***
     Tooltip
     */
    String TTP_MODIFY_ORDER = "Изменить";
    String TTP_CREATE_ENTITY = "Создать";
    String TTP_UPDATE_ENTITY = "Изменить";
    String TTP_DELETE_ENTITY = "Удалить";
    String TTP_ACTIVATE_ENTITY = "Активировать";
    String TTP_DEACTIVATE_ENTITY = "Деактивировать";
    String TTP_CLOSE_ORDER = "Закрыть";
    String TTP_ADOPT_ORDER = "Принять";
    String TTP_LEAVE_ORDER = "Вернуть";
    String TTP_COMMENT_ORDER = "Комментировать";
    /***
     Pagetitile
     */
    String PTT_ADMIN = "Пероснал";
    String PTT_EQUIPMENT = "Оборудование";
    String PTT_LOGIN = "Вход";
    String PTT_CHANGE_PASS = "Смена пароля";
}
