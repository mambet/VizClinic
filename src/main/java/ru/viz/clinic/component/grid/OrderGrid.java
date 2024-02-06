package ru.viz.clinic.component.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.component.components.DepartmentSelect;
import ru.viz.clinic.component.components.EquipmentSelect;
import ru.viz.clinic.component.components.HospitalSelect;
import ru.viz.clinic.component.dialog.ShowRecordDialog;
import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Equipment;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.entity.Order;
import ru.viz.clinic.help.Translator;
import ru.viz.clinic.service.RecordService;

import java.time.LocalDateTime;
import java.util.*;

import static ru.viz.clinic.converter.EntityToStringConverter.convertToPresentation;
import static ru.viz.clinic.converter.EntityToStringConverter.convertToPresentationAndMarkInactive;
import static ru.viz.clinic.help.Helper.getDateTimeRenderer;
import static ru.viz.clinic.help.Translator.*;

public abstract class OrderGrid extends AbstractGrid<Order> {
    private final RecordService recordService;
    private final Column<Order> idColumn;
    private final TextField idFilterHeader = new TextField();
    private static final String ID_COLUMN_WIDTH = "6em";
    private HeaderRow headerRow;
    private Column<Order> equipmnentColumn;
    private Column<Order> hospitalColumn;
    private Column<Order> departmentColumn;
    private OrderFilter orderFilter;
    private HospitalSelect hospitalSelect;
    private DepartmentSelect departmentSelect;
    private EquipmentSelect equipmentSelect;

    public OrderGrid(@NotNull final RecordService recordService) {

        this.recordService = Objects.requireNonNull(recordService);

        idColumn = this.addColumn(Order::getId)
                .setHeader(Translator.HDR_ID)
                .setWidth(ID_COLUMN_WIDTH).setFlexGrow(0);
        this.addColumn(Order::getDescription)
                .setHeader(Translator.HDR_DESCRIPTION)
                .setResizable(true);
        this.addColumn(getStyled(Order::getMedic))
                .setHeader(Translator.HDR_ORDER_GIVER)
                .setResizable(true);
        this.addColumn(getDateTimeRenderer(Order::getCreateTime))
                .setSortable(true)
                .setComparator(Order::getCreateTime)
                .setHeader(Translator.HDR_CREATE_ORDER)
                .setResizable(true);
        this.addColumn(geStyledForList(Order::getDestinationEngineers))
                .setAutoWidth(true)
                .setHeader(Translator.HDR_DESTINATION_ENGINEER)
                .setResizable(true);
        this.addColumn(order -> order.getOrderState().getValue())
                .setSortable(true)
                .setHeader(Translator.HDR_STATE_ORDER)
                .setResizable(true);
        this.addColumn(getStyled(Order::getOwnerEngineer))
                .setHeader(Translator.HDR_ORDER_TAKER)
                .setResizable(true);
        this.addColumn(getStyled(Order::getFinishEngineer))
                .setHeader(Translator.HDR_ORDER_FINISHER)
                .setResizable(true);
        this.addColumn(getDateTimeRenderer(Order::getEndTime))
                .setSortable(true)
                .setComparator(Order::getEndTime)
                .setHeader(Translator.HDR_END_ORDER)
                .setResizable(true);
        this.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, order) -> {
                    layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
                    addCustomButton(layout, order);
                    layout.add(showRecords(order));
                }))
                .setAutoWidth(true);
        this.setPartNameGenerator(order -> {
            if (order.isActive()) {
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
            } else {
                return "order-inactive";
            }
        });

        addFilterHeaderRow();
        addIdFilterHeader();
        this.setSelectionMode(SelectionMode.NONE);
        this.setAllRowsVisible(true);
    }

    @Override
    public GridListDataView<Order> setItems(@NotNull final Collection<Order> items) {
        Objects.requireNonNull(items);
        final GridListDataView<Order> recordGridListDataView = super.setItems(items);
        this.getListDataView().setSortOrder(order -> {
            if (order.getCreateTime() == null) {
                return LocalDateTime.now();
            } else {
                return order.getCreateTime();
            }
        }, SortDirection.DESCENDING);
        updateFilter(items, recordGridListDataView);

        return recordGridListDataView;
    }

    private void addFilterHeaderRow() {
        this.orderFilter = new OrderFilter();
        this.getHeaderRows().clear();
        this.headerRow = this.appendHeaderRow();
    }

    private void addIdFilterHeader() {
        idFilterHeader.setSizeFull();
        idFilterHeader.setValueChangeMode(ValueChangeMode.EAGER);
        headerRow.getCell(idColumn).setComponent(idFilterHeader);
        idFilterHeader.addValueChangeListener(textFieldStringComponentValueChangeEvent -> orderFilter.setId(
                textFieldStringComponentValueChangeEvent.getValue()));
    }

    public static class AdoptGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public AdoptGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class CommentGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public CommentGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class LeaveGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public LeaveGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order

        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class CloseGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public CloseGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class DeleteGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public DeleteGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    public static class UpdateGridEvent extends AbstractGridEvent<OrderGrid, Order> {
        public UpdateGridEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
        }
    }

    @Getter
    public static class SetActiveEvent extends AbstractGridEvent<OrderGrid, Order> {
        private final boolean active;

        public SetActiveEvent(
                @NotNull final OrderGrid source,
                @NotNull final Order order,
                final boolean active
        ) {
            super(Objects.requireNonNull(source), Objects.requireNonNull(order));
            this.active = active;
        }
    }

    protected static class OrderFilter {
        private GridListDataView<Order> dataView;
        private Hospital hospital;
        private Department department;
        private Equipment equipment;
        private String id;

        public void setDataView(final GridListDataView<Order> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setId(final String id) {
            this.id = id;
            this.dataView.refreshAll();
        }

        public void setHospital(final Hospital hospital) {
            this.hospital = hospital;
            this.dataView.refreshAll();
        }

        public void setDepartment(final Department department) {
            this.department = department;
            this.dataView.refreshAll();
        }

        public void setEquipment(final Equipment equipment) {
            this.equipment = equipment;
            this.dataView.refreshAll();
        }

        public boolean test(final Order order) {
            final boolean hospitalMatches =
                    hospital == null || hospital.equals(order.getEquipment().getDepartment().getHospital());
            final boolean departmentMatches =
                    department == null || department.equals(order.getEquipment().getDepartment());
            final boolean equipmentMatches =
                    equipment == null || equipment.equals(order.getEquipment());
            final boolean idMatches = matches(order.getId(), id);

            return hospitalMatches && departmentMatches && equipmentMatches && idMatches;
        }

        private boolean matches(
                final String value,
                final String searchTerm
        ) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

    protected HospitalSelect getHospitalSelect() {
        return hospitalSelect;
    }

    protected DepartmentSelect getDepartmentSelect() {
        return departmentSelect;
    }

    protected EquipmentSelect getEquipmentSelect() {
        return equipmentSelect;
    }

    protected OrderFilter getOrderFilter() {
        return orderFilter;
    }

    protected abstract void updateFilter(
            @NotNull final Collection<Order> items,
            GridListDataView<Order> orderGridListDataView
    );

    protected void addHospitalColumn(final int index) {
        final List<Column<Order>> originalColumns = new ArrayList<>(this.getColumns());
        hospitalColumn = this.addColumn(order -> {
                    if (order.getEquipment() != null
                            && order.getEquipment().getDepartment() != null
                            && order.getEquipment().getDepartment().getHospital() != null) {
                        return order.getEquipment().getDepartment().getHospital().getName();
                    }
                    return Strings.EMPTY;
                })
                .setHeader(HDR_HOSPITAL)
                .setResizable(true);
        hospitalSelect = HospitalSelect.createWithAllowEmpty(orderFilter::setHospital);
        headerRow.getCell(hospitalColumn).setComponent(hospitalSelect);

        originalColumns.add(index, hospitalColumn);
        this.setColumnOrder(originalColumns);
    }

    protected void addDepartmentColumn(final int index) {
        final List<Column<Order>> originalColumns = new ArrayList<>(this.getColumns());
        departmentColumn = this.addColumn(order -> {
                    if (order.getEquipment() != null
                            && order.getEquipment().getDepartment() != null) {
                        return order.getEquipment().getDepartment().getName();
                    }
                    return Strings.EMPTY;
                })
                .setHeader(HDR_DEPARTMENT)
                .setResizable(true);
        departmentSelect = DepartmentSelect.createWithAllowEmpty(orderFilter::setDepartment);
        headerRow.getCell(departmentColumn).setComponent(departmentSelect);
        originalColumns.add(index, departmentColumn);
        this.setColumnOrder(originalColumns);
    }

    protected void addEquipmentColumn(final int index) {
        final List<Column<Order>> originalColumns = new ArrayList<>(this.getColumns());
        equipmnentColumn = this.addColumn(order -> {
                    if (order.getEquipment() != null) {
                        return order.getEquipment().getName();
                    }
                    return Strings.EMPTY;
                })
                .setHeader(Translator.HDR_EQUIPMENT)
                .setResizable(true);
        equipmentSelect = EquipmentSelect.createWithAllowEmpty(orderFilter::setEquipment);
        headerRow.getCell(equipmnentColumn).setComponent(equipmentSelect);
        originalColumns.add(index, equipmnentColumn);
        this.setColumnOrder(originalColumns);
    }

    protected abstract void addCustomButton(
            @NotNull final HorizontalLayout layout,
            @NotNull final Order order
    );

    protected Button showRecords(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.INFO_CIRCLE_O));
        button.setTooltipText("Протокол");
        button.addClickListener(e -> new ShowRecordDialog(recordService.getByOrderId(order.getId())).open());
        return button;
    }

    protected Button adoptOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.SCREWDRIVER),
                e -> fireEvent(new AdoptGridEvent(this, order)));
        button.setTooltipText(TTP_ADOPT_ORDER);
        return button;
    }

    protected Button commentOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.COMMENT_O),
                e -> fireEvent(new CommentGridEvent(this, order)));
        button.setTooltipText(TTP_COMMENT_ORDER);
        return button;
    }

    protected Button leaveOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.EXIT_O),
                e -> fireEvent(new LeaveGridEvent(this, order)));
        button.setTooltipText(TTP_LEAVE_ORDER);
        return button;
    }

    protected Button closeOrderButton(@NotNull final Order order) {
        Objects.requireNonNull(order);
        final Button button = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O),
                e -> fireEvent(new CloseGridEvent(this, order)));
        button.setTooltipText(TTP_CLOSE_ORDER);
        return button;
    }

    @Override
    protected void setEntityActive(
            @NotNull final Order order,
            final boolean active
    ) {
        fireEvent(new SetActiveEvent(this, order, active));
    }

    @Override
    protected void deleteEntity(@NotNull final Order order) {
        fireEvent(new DeleteGridEvent(this, order));
    }

    @Override
    protected void updateEntity(@NotNull final Order order) {
        fireEvent(new OrderGrid.UpdateGridEvent(this, order));
    }
}