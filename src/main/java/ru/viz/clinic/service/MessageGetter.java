package ru.viz.clinic.service;

import ru.viz.clinic.data.entity.AbstractEntity;

public interface MessageGetter <E extends AbstractEntity>  {
    boolean isReadyToActivate(E e);
    boolean isReadyToDeactivate(E e);
    boolean isReadyToDelete(E e);

    void showUpdateSuccessMessage(E e);
    void showUpdateErrMessage(E e);

    void showCreateSuccessMessage(E e);
    void showCreateErrMessage(E e);

    void showDeleteSuccessMessage(E e);
    void showDeleteErrMessage(E e);

    void showActivateSuccessMessage(E e);
    void showActivateErrMessage(E e);

    void showDeactivateSuccessMessage(E e);
    void showDeactivateErrMessage(E e);
}