package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import jakarta.validation.constraints.NotNull;
import ru.viz.clinic.component.dialog.ShowRecordDialog;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.RecordService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import static ru.viz.clinic.converter.PersonalToStringConverter.convertToPresentation;
import static ru.viz.clinic.help.Helper.getDateTimeRenderer;

public abstract class OrderGrid extends AbstractGrid<Order> {
    private final RecordService recordService;

    public OrderGrid(@NotNull final RecordService recordService) {

        this.recordService = Objects.requireNonNull(recordService);

        this.addColumn(Order::getId)
                .setAutoWidth(true)
                .setHeader(Translator.HDR_ID)
                .setWidth("7em").setFlexGrow(0);
        this.addColumn(order -> order.getEquipment().getName())
                .setAutoWidth(true)
                .setHeader(Translator.HDR_EQUIPMENT)
                .setResizable(true);
        this.addColumn(Order::getDescription)
                .setAutoWidth(true)
                .setHeader(Translator.HDR_DESCRIPTION)
                .setResizable(true);
        this.addColumn(order -> convertToPresentation(order.getMedic()))
                .setAutoWidth(true)
                .setHeader(Translator.HDR_ORDER_GIVER)
                .setResizable(true);
        this.addColumn(getDateTimeRenderer(Order::getCreateTime))
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(Order::getCreateTime)
                .setHeader(Translator.HDR_CREATE_ORDER)
                .setResizable(true);
        this.addColumn(order -> convertToPresentation(order.getDestinationEngineers()))
                .setAutoWidth(true)
                .setHeader(Translator.HDR_DESTINATION_ENGINEER)
                .setResizable(true);
        this.addColumn(order -> order.getOrderState().getValue())
                .setSortable(true)
                .setHeader(Translator.HDR_STATE_ORDER)
                .setResizable(true);
        this.addColumn(order -> convertToPresentation(order.getOwnerEngineer()))
                .setHeader(Translator.HDR_ORDER_TAKER)
                .setResizable(true);
        this.addColumn(order -> convertToPresentation(order.getFinishEngineer()))
                .setHeader(Translator.HDR_ORDER_FINISHER)
                .setResizable(true);
        this.addColumn(getDateTimeRenderer(Order::getEndTime))
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(Order::getEndTime)
                .setHeader(Translator.HDR_END_ORDER)
                .setResizable(true);
        this.setPartNameGenerator(order -> {
            if (order.getOrderState() != null) {
                switch (order.getOrderState()) {
                    case READY -> {
                        return "ready";
                    }
                    case WORKING -> {
                        return "working";
                    }
                    case DONE -> {
                        return "done";
                    }
                }
            }
            return null;
        });

        this.setSelectionMode(SelectionMode.NONE);
        this.setAllRowsVisible(true);
        this.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    @Override
    public GridListDataView<Order> setItems(@NotNull final Collection<Order> items) {
        Objects.requireNonNull(items);
        final GridListDataView<Order> recordGridListDataView = super.setItems(items);
        this.getListDataView().setSortOrder(record -> {
            if (record.getCreateTime() == null) {
                return LocalDateTime.now();
            } else {
                return record.getCreateTime();
            }
        }, SortDirection.DESCENDING);
        return recordGridListDataView;
    }

    public Button showRecords(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.INFO_CIRCLE_O));
        button.setTooltipText("Протокол");
        button.addClickListener(e -> new ShowRecordDialog(recordService.getByOrderId(order.getId())).open());
        return button;
    }

}