package christmas.domain.order;

import static christmas.domain.event.EventCalendar.isChristmasPeriod;
import static christmas.domain.event.EventCalendar.isHoliday;
import static christmas.domain.event.EventCalendar.isSpecialDay;
import static christmas.domain.event.EventCalendar.isWeekday;
import static christmas.domain.event.constants.Discount.CHRISTMAS_BASE_DISCOUNT;
import static christmas.domain.event.constants.Discount.CHRISTMAS_INCREASE_DISCOUNT;
import static christmas.domain.event.constants.Discount.HOLIDAY_DISCOUNT;
import static christmas.domain.event.constants.Discount.SPECIAL_DISCOUNT;
import static christmas.domain.event.constants.Discount.WEEKDAY_DISCOUNT;
import static christmas.exception.ErrorMessage.DUPLICATE_ORDER;
import static christmas.exception.ErrorMessage.INVALID_ORDERS_INPUT;
import static christmas.service.Parser.convertToLocalDate;
import static christmas.service.Parser.parseToInt;
import static christmas.service.Parser.splitByDelimiter;
import static christmas.view.input.InputValidator.validateStringWithSeparator;
import static christmas.view.output.OutputView.NONE_PRICE;

import christmas.exception.ChristmasException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Orders {

    private static final int INITIAL_SUM_VALUE = 0;
    private static final int MENU_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;
    private static final int DECREASE_DAY_OF_MONTH = 1;
    private static final int MINIMUM_PURCHASE_PRICE = 10000;
    private static final int ELIGIBLE_GIFT_EVENT_PRIZE = 120000;
    private static final String MENU_AND_AMOUNT_SEPARATOR = "-";

    private final List<Order> orders;
    private final LocalDate day;

    private Orders(final List<Order> orders, final LocalDate day) {
        this.orders = orders;
        this.day = day;
    }

    public static Orders from(final List<String> ordersInput, final int dayOfMonth) {
        final List<Order> orders = initializeOrders(ordersInput);
        final LocalDate day = convertToLocalDate(dayOfMonth);
        return new Orders(orders, day);
    }

    public int receiveSpecialEventDiscountPrice() {
        if (!isEventTarget() || !isSpecialDay(day.getDayOfMonth())) {
            return NONE_PRICE;
        }
        return SPECIAL_DISCOUNT.getDiscountPrice();
    }

    public int receiveHolidayEventDiscountPrice() {
        if (!isEventTarget() || isWeekday(day.getDayOfMonth())) {
            return NONE_PRICE;
        }

        return orders.stream()
                .filter(Order::isMainDishMenu)
                .map(order -> order.receiveDiscountPrice(HOLIDAY_DISCOUNT))
                .reduce(INITIAL_SUM_VALUE, Integer::sum);
    }

    public int receiveWeekdayEventDiscountPrice() {
        if (!isEventTarget() || isHoliday(day.getDayOfMonth())) {
            return NONE_PRICE;
        }

        return orders.stream()
                .filter(Order::isDessertMenu)
                .map(order -> order.receiveDiscountPrice(WEEKDAY_DISCOUNT))
                .reduce(INITIAL_SUM_VALUE, Integer::sum);
    }

    public int receiveChristmasEventDiscountPrice() {
        if (!isChristmasPeriod(day.getDayOfMonth())) {
            return NONE_PRICE;
        }
        return calculateChristmasEventDiscountPrice(day.getDayOfMonth());
    }

    public boolean isEventTarget() {
        return receiveTotalOrderPrice() >= MINIMUM_PURCHASE_PRICE;
    }

    public boolean isEligibleForPrize() {
        return receiveTotalOrderPrice() >= ELIGIBLE_GIFT_EVENT_PRIZE;
    }

    public int receiveTotalOrderPrice() {
        return orders.stream()
                .map(Order::receiveOrderPrice)
                .reduce(INITIAL_SUM_VALUE, Integer::sum);
    }

    public List<String> receiveOrdersInfo() {
        return orders.stream()
                .map(Order::toString)
                .toList();
    }

    private int calculateChristmasEventDiscountPrice(final int dayOfMonth) {
        return CHRISTMAS_BASE_DISCOUNT.getDiscountPrice() + incrementAmountByDate(dayOfMonth);
    }

    private int incrementAmountByDate(final int dayOfMonth) {
        return (dayOfMonth - DECREASE_DAY_OF_MONTH) * CHRISTMAS_INCREASE_DISCOUNT.getDiscountPrice();
    }

    private static List<Order> initializeOrders(final List<String> ordersInput) {
        final List<Order> orders = generateOrders(ordersInput);

        validateOrders(orders);
        return orders;
    }

    private static List<Order> generateOrders(final List<String> ordersInput) {
        final List<Order> orders = new ArrayList<>();

        for (String orderInput : ordersInput) {
            validateStringWithSeparator(orderInput, MENU_AND_AMOUNT_SEPARATOR);

            final List<String> menuAndAmount = splitByDelimiter(orderInput, MENU_AND_AMOUNT_SEPARATOR);
            final Order order = Order.from(receiveMenu(menuAndAmount), receiveAmount(menuAndAmount));

            validateDuplicateMenu(orders, order);
            orders.add(order);
        }

        return orders;
    }

    private static void validateOrders(final List<Order> orders) {
        if (areAllOrdersBeverage(orders)) {
            throw ChristmasException.from(INVALID_ORDERS_INPUT);
        }
    }

    private static boolean areAllOrdersBeverage(final List<Order> orders) {
        return orders.stream()
                .allMatch(Order::isBeverageMenu);
    }

    private static void validateDuplicateMenu(final List<Order> orders, final Order order) {
        if (orders.contains(order)) {
            throw ChristmasException.from(DUPLICATE_ORDER);
        }
    }

    private static int receiveAmount(final List<String> menuAndAmount) {
        return parseToInt(menuAndAmount.get(AMOUNT_INDEX), INVALID_ORDERS_INPUT);
    }

    private static String receiveMenu(final List<String> menuAndAmount) {
        return menuAndAmount.get(MENU_INDEX);
    }
}
