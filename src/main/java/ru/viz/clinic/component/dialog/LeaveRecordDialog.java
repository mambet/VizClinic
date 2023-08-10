package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.data.entity.Order;

import java.util.function.BiConsumer;

import static ru.viz.clinic.help.Translator.*;

@Log4j2
public class LeaveRecordDialog extends RecordDialog {
    public LeaveRecordDialog(
            @NotNull final Order order,
            @NotNull final BiConsumer<Order, String> leaveOrder

    ) {
        super(DLH_LEAVE_ORDER, BTN_LEAVE_ORDER);
        addConfirmListener(confirmEvent -> leaveOrder.accept(order, commentTextArea.getValue()));
    }
}
