package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Order;

import java.util.Objects;
import java.util.function.BiConsumer;

import static ru.viz.clinic.help.Translator.*;

public class CommentRecordDialog extends RecordDialog {
    public CommentRecordDialog(
            @NotNull final Order order,
            @NotNull final BiConsumer<Order, String> leaveOrder

    ) {
        super(DLH_COMMENT, BTN_CREATE_COMMENT);
        addConfirmListener(confirmEvent -> leaveOrder.accept(Objects.requireNonNull(order),
                commentTextArea.getValue()));
    }
}
