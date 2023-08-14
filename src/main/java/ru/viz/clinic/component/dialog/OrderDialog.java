package ru.viz.clinic.component.dialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.viz.clinic.data.entity.*;

import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.help.Translator.*;

public class OrderDialog extends VizConfirmDialog<Order> {
    final Select<Equipment> equipmentSelect = new Select<>();
    final MultiSelectComboBox<Engineer> engineerSelect = new MultiSelectComboBox<>();

    private OrderDialog(
            @NotNull final Order order,
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipments
    ) {
        super(DLH_CREATE_ORDER, Objects.requireNonNull(order));

        Objects.requireNonNull(equipments);
        Objects.requireNonNull(engineers);

        engineerSelect.setLabel(LBL_ENGINEER);
        engineerSelect.setItemLabelGenerator(engineer -> engineer.getFirstName() + " " + engineer.getLastName());
        engineerSelect.setItems(engineers);

        equipmentSelect.setLabel(HDR_EQUIPMENT);
        equipmentSelect.setItemLabelGenerator(Equipment::getName);
        equipmentSelect.setItems(equipments);

        final TextArea description = new TextArea(LBL_EQUIPMENT_DESCRIPTION);

        binder.forField(equipmentSelect)
                .asRequired()
                .bind(Order::getEquipment, Order::setEquipment);
        binder.forField(engineerSelect)
                .asRequired()
                .bind(Order::getDestinationEngineers, Order::setDestinationEngineers);
        binder.forField(description).bind(Order::getDescription, Order::setDescription);
        binder.readBean(order);
        this.add(new FormLayout(equipmentSelect, engineerSelect, description));
    }

    public OrderDialog(
            @NotNull final Collection<Engineer> engineers,
            @NotNull final Collection<Equipment> equipment,
            @NotNull final Order order

    ) {
        this(order, engineers, equipment);
    }

    @Override
    protected void handleConfirm() {
        fireEvent(new UpdateOrder(this, Objects.requireNonNull(item)));
    }

    @Getter
    public static class UpdateOrder extends AbstractDialogEvent<OrderDialog, Order> {
        public UpdateOrder(
                @NotNull final OrderDialog source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));

        }
    }
}
