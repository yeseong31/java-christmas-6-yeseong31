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
import static christmas.view.input.InputView.MENU_AND_AMOUNT_SEPARATOR;
import static christmas.view.output.OutputView.NONE_PRICE;

import christmas.exception.ChristmasException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders {

    private static final int INITIAL_SUM_VALUE = 0;
    private static final int MENU_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;
    private static final int DECREASE_DAY_OF_MONTH = 1;
    private static final int MINIMUM_PURCHASE_PRICE = 10000;
    private static final int ELIGIBLE_GIFT_EVENT_PRIZE = 120000;

    private final Map<String, Order> orders;
    private final LocalDate day;

    private Orders(final Map<String, Order> orders, final LocalDate day) {
        this.orders = orders;
        this.day = day;
    }

    public static Orders from(final List<String> ordersInput, final int dayOfMonth) {
        final Map<String, Order> orders = generateOrders(ordersInput);
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

        return orders.values()
                .stream()
                .filter(Order::isMainDishMenu)
                .map(order -> order.receiveDiscountPrice(HOLIDAY_DISCOUNT))
                .reduce(INITIAL_SUM_VALUE, Integer::sum);
    }

    public int receiveWeekdayEventDiscountPrice() {
        if (!isEventTarget() || isHoliday(day.getDayOfMonth())) {
            return NONE_PRICE;
        }

        return orders.values()
                .stream()
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
        return orders.values().stream()
                .map(Order::receiveOrderPrice)
                .reduce(INITIAL_SUM_VALUE, Integer::sum);
    }

    public List<String> receiveOrdersInfo() {
        return orders.values()
                .stream()
                .map(Order::toString)
                .toList();
    }

    private int calculateChristmasEventDiscountPrice(int dayOfMonth) {
        return CHRISTMAS_BASE_DISCOUNT.getDiscountPrice() + incrementAmountByDate(dayOfMonth);
    }

    private int incrementAmountByDate(int dayOfMonth) {
        return (dayOfMonth - DECREASE_DAY_OF_MONTH) * CHRISTMAS_INCREASE_DISCOUNT.getDiscountPrice();
    }

    private static Map<String, Order> generateOrders(List<String> ordersInput) {
        final Map<String, Order> orders = new HashMap<>();

        for (String orderInput : ordersInput) {
            validateStringWithSeparator(orderInput, MENU_AND_AMOUNT_SEPARATOR);

            final List<String> menuAndAmount = splitByDelimiter(orderInput, MENU_AND_AMOUNT_SEPARATOR);
            String menuName = receiveMenu(menuAndAmount);
            int amount = receiveAmount(menuAndAmount);

            validateDuplicateMenu(orders, menuName);
            orders.put(menuName, Order.from(menuName, amount));
        }

        validateOrders(orders);
        return orders;
    }

    private static void validateOrders(Map<String, Order> orders) {
        if (areAllOrdersBeverage(orders)) {
            throw ChristmasException.from(INVALID_ORDERS_INPUT);
        }
    }

    private static boolean areAllOrdersBeverage(Map<String, Order> orders) {
        return orders.values()
                .stream()
                .allMatch(Order::isBeverageMenu);
    }

    private static void validateDuplicateMenu(Map<String, Order> orders, String menuName) {
        if (orders.containsKey(menuName)) {
            throw ChristmasException.from(DUPLICATE_ORDER);
        }
    }

    private static int receiveAmount(List<String> menuAndAmount) {
        return parseToInt(menuAndAmount.get(AMOUNT_INDEX));
    }

    private static String receiveMenu(List<String> menuAndAmount) {
        return menuAndAmount.get(MENU_INDEX);
    }
}
