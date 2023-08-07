package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.component.dialog.ShowRecordDialog;
import ru.viz.clinic.converter.PersonalToStringConverter;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.data.entity.Personal;
import ru.viz.clinic.data.entity.Record;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.OrderService;
import ru.viz.clinic.service.RecordService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.viz.clinic.converter.PersonalToStringConverter.convertToPresentation;

public abstract class OrderGrid extends Grid<Order> {
    private final RecordService recordService;

    public OrderGrid(@NotNull final RecordService recordService) {

        this.recordService = Objects.requireNonNull(recordService);

        this.addColumn(Order::getId)
                .setHeader(Translator.LBL_ID)
                .setAutoWidth(true);
        this.addColumn(order -> order.getEquipment().getName())
                .setHeader(Translator.LBL_EQUIPMENT)
                .setAutoWidth(true);
        this.addColumn(Order::getDescription)
                .setHeader(Translator.LBL_DESCRIPTION)
                .setAutoWidth(true);
        this.addColumn(order -> convertToPresentation(order.getMedic()))
                .setHeader(Translator.LBL_MEDIC)
                .setAutoWidth(true);
        this.addColumn(getRenderer(Order::getCreateTime))
                .setHeader(Translator.LBL_CREATE_ORDER)
                .setSortable(true)
                .setComparator(Order::getCreateTime);
        this.addColumn(order -> convertToPresentation(order.getDestinationEngineers()))
                .setHeader(Translator.LBL_DESTINATION_ENGINEER)
                .setAutoWidth(true);
        this.addColumn(order -> order.getOrderState().getValue())
                .setHeader(Translator.LBL_STATE_ORDER)
                .setSortable(true);
        this.addColumn(order -> convertToPresentation(order.getOwnerEngineer()))
                .setHeader(Translator.LBL_ORDER_TAKER);
        this.addColumn(order ->  convertToPresentation(order.getFinishEngineer()))
                .setHeader(Translator.LBL_ORDER_FINISHER);
        this.addColumn(getRenderer(Order::getEndTime))
                .setHeader(Translator.LBL_END_ORDER);
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

    private static LocalDateTimeRenderer<Order> getRenderer(ValueProvider<Order, LocalDateTime> getTime) {
        return new LocalDateTimeRenderer<>(getTime, "dd.MM.yyyy HH:mm");
    }
    @Override
    public GridListDataView<Order> setItems(Collection<Order> items) {
        GridListDataView<Order> recordGridListDataView = super.setItems(items);
        this.getListDataView().setSortOrder(Order::getCreateTime, SortDirection.DESCENDING);
        return recordGridListDataView;
    }

    public Button showRecords(@NotNull final Order order) {
        Objects.requireNonNull(order);
        Button button = new Button(new Icon(VaadinIcon.INFO_CIRCLE_O));
        button.setTooltipText("Протокол");
        button.addClickListener(e -> new ShowRecordDialog(recordService.getByOrderId(order.getId())).open());
        return button;
    }

    @Override
    public <E extends ComponentEvent<?>> Registration addListener(
            Class<E> eventType,
            ComponentEventListener<E> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }

    public static class PersonFilter<T extends Personal> {
        public final GridListDataView<T> dataView;
        private String firstName;
        private String email;
        private String userName;

        public PersonFilter(GridListDataView<T> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
            this.dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.email = email;
            this.dataView.refreshAll();
        }

        public void setUserName(String userName) {
            this.userName = userName;
            this.dataView.refreshAll();
        }

        public boolean test(T person) {
            boolean matchesFirstname = matches(person.getFirstName(), firstName);
            boolean matchesEmail = matches(person.getEmail(), email);
            boolean matchesUsername = matches(person.getUsername(),
                    userName);

            return matchesFirstname && matchesEmail && matchesUsername;
        }

        private boolean matches(
                String value,
                String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}