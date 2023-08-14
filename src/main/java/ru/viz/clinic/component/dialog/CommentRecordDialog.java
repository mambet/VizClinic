package ru.viz.clinic.component.dialog;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.Order;

import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class CommentRecordDialog extends RecordDialog {
    public CommentRecordDialog(@NotNull final Order order) {
        super(DLH_COMMENT, BTN_CREATE_COMMENT);
        addConfirmListener(confirmEvent -> fireEvent(new CommentOrderEvent(this, Objects.requireNonNull(order),
                commentTextArea.getValue())));
    }

    @Getter
    public static class CommentOrderEvent extends AbstractDialogEvent<CommentRecordDialog, Order> {
        private final String comment;

        public CommentOrderEvent(
                @NotNull final CommentRecordDialog source,
                @NotNull final Order hospital,
                @NotNull final String comment
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(hospital));
            this.comment = comment;
        }
    }
}
