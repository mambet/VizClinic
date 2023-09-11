package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.viz.clinic.data.entity.Order;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.BTN_LEAVE_ORDER;
import static ru.viz.clinic.help.Translator.DLH_LEAVE_ORDER;

@Slf4j
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
