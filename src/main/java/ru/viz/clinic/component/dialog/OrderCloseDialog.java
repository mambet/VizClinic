package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Paragraph;

import static ru.viz.clinic.help.Translator.*;

public class OrderCloseDialog extends ConfirmDialog {
    protected final Paragraph commentTextArea = new Paragraph(MSG_CLOSE_ORDER_QUESTION);

    public OrderCloseDialog() {
        setHeader(DLH_CLOSE_ORDER);

        Button btnConfirm = new Button(BTN_CLOSE_ORDER);

        this.setConfirmButton(btnConfirm);
        this.setCancelText(BTN_CANCEL);
        this.setCancelable(true);

        this.add(commentTextArea);
    }
}
