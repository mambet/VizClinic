package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ru.viz.clinic.data.entity.Order;

import java.util.Objects;
import java.util.function.BiConsumer;

import static ru.viz.clinic.help.Translator.*;

@Log4j2
public class LeaveRecordDialog extends RecordDialog {
    public LeaveRecordDialog(
            @NotNull final Order order
    ) {
        super(DLH_LEAVE_ORDER, BTN_LEAVE_ORDER);
        addConfirmListener(confirmEvent -> fireEvent(new LeaveOrderEvent(this, Objects.requireNonNull(order),
                commentTextArea.getValue())));
    }

    @Getter
    public static class LeaveOrderEvent extends RecordDialog.AbstractDialogEvent<LeaveRecordDialog, Order> {
        private final String comment;

        public LeaveOrderEvent(
                @NotNull final LeaveRecordDialog source,
                @NotNull final Order hospital,
                @NotNull final String comment
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(hospital));
            this.comment = comment;
        }
    }
}
