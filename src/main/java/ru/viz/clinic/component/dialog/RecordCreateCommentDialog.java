package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.data.entity.Order;

import java.util.function.BiConsumer;

public class RecordCreateCommentDialog extends RecordDialog {
    public RecordCreateCommentDialog(
            @NotNull final Order order,
            @NotNull final BiConsumer<Order, String> leaveOrder

    ) {
        addConfirmListener(confirmEvent -> leaveOrder.accept(order, commentTextArea.getValue()));
    }
}
