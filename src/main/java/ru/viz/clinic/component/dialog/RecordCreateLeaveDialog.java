package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.data.entity.Order;

import java.util.function.BiConsumer;

@Log4j2
public class RecordCreateLeaveDialog extends RecordDialog {
    public RecordCreateLeaveDialog(
            @NotNull final Order order,
            @NotNull final BiConsumer<Order, String> leaveOrder

    ) {
        addConfirmListener(confirmEvent -> leaveOrder.accept(order, commentTextArea.getValue()));
    }
}
