package christmas.domain.event;

import static christmas.domain.event.constants.EventType.CHRISTMAS_EVENT;
import static christmas.domain.event.constants.EventType.GIFT_EVENT;
import static christmas.domain.event.constants.EventType.HOLIDAY_EVENT;
import static christmas.domain.event.constants.EventType.SPECIAL_EVENT;
import static christmas.domain.event.constants.EventType.WEEKDAY_EVENT;
import static christmas.domain.event.constants.EventType.values;
import static christmas.service.Parser.convertToDayOfWeek;
import static christmas.service.Parser.convertToLocalDate;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.util.Collections.unmodifiableList;

import christmas.domain.event.constants.EventType;
import christmas.domain.order.Order;
import christmas.domain.order.Orders;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCalendar {

    private static final List<Integer> specialDays = List.of(3, 10, 17, 24, 25, 31);
    private static final String eventMenuName = "샴페인";
    private static final int eventMenuAmount = 1;

    private final List<EventType> events;
    private final Order eventMenu;

    private EventCalendar(final List<EventType> events, final Order eventMenu) {
        this.events = events;
        this.eventMenu = eventMenu;
    }

    public static EventCalendar of(final int dayOfMonth) {
        final List<EventType> events = receiveApplicableEvents(dayOfMonth);
        final Order eventMenu = generateEventMenu();
        return new EventCalendar(events, eventMenu);
    }

    public int calculateEstimatedPriceAfterDiscount(final Orders orders) {
        int sumValue = receiveBenefitDetailsWithoutGiftEvent(orders).values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        return orders.receiveTotalOrderPrice() - sumValue;
    }

    public int receiveTotalBenefitPrice(final Orders orders) {
        return receiveBenefitDetails(orders).values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Map<EventType, Integer> receiveBenefitDetails(final Orders orders) {
        Map<EventType, Integer> result = receiveBenefitDetailsWithoutGiftEvent(orders);

        if (orders.isEligibleForPrize()) {
            result.put(GIFT_EVENT, receiveEventMenuPrice());
        }

        return result;
    }

    private Map<EventType, Integer> receiveBenefitDetailsWithoutGiftEvent(Orders orders) {
        Map<EventType, Integer> result = new HashMap<>();

        for (EventType eventType : events) {
            int discountPrice = calculateDiscountPrice(orders, eventType);
            if (discountPrice > NONE_PRICE) {
                result.put(eventType, discountPrice);
            }
        }

        return result;
    }

    private int calculateDiscountPrice(Orders orders, EventType eventType) {
        if (eventType == CHRISTMAS_EVENT) {
            return orders.receiveChristmasEventDiscountPrice();
        }
        if (eventType == WEEKDAY_EVENT) {
            return orders.receiveWeekdayEventDiscountPrice();
        }
        if (eventType == HOLIDAY_EVENT) {
            return orders.receiveHolidayEventDiscountPrice();
        }
        if (eventType == SPECIAL_EVENT) {
            return orders.receiveSpecialEventDiscountPrice();
        }
        return 0;
    }

    public List<EventType> receiveTargetEvents() {
        return unmodifiableList(events);
    }

    public String receiveEventMenuInfo() {
        return eventMenu.toString();
    }

    public static int receiveEventMenuPrice() {
        return generateEventMenu().receiveOrderPrice();
    }

    public static boolean isChristmasPeriod(int dayOfMonth) {
        return isDayOfMonthWithinRange(CHRISTMAS_EVENT, dayOfMonth);
    }

    public static boolean isSpecialDay(final int dayOfMonth) {
        return specialDays.contains(dayOfMonth);
    }

    public static boolean isWeekday(final int dayOfMonth) {
        return !isHoliday(dayOfMonth);
    }

    public static boolean isHoliday(final int dayOfMonth) {
        final LocalDate reserveDate = convertToLocalDate(dayOfMonth);
        final DayOfWeek dayOfWeek = convertToDayOfWeek(reserveDate);

        return isFriday(dayOfWeek) || isSaturday(dayOfWeek);
    }

    private static boolean isSaturday(DayOfWeek dayOfWeek) {
        return dayOfWeek == SATURDAY;
    }

    private static boolean isFriday(DayOfWeek dayOfWeek) {
        return dayOfWeek == FRIDAY;
    }

    private static Order generateEventMenu() {
        return Order.from(eventMenuName, eventMenuAmount);
    }

    private static List<EventType> receiveApplicableEvents(final int dayOfMonth) {
        return Arrays.stream(values())
                .filter(eventType -> isDayOfMonthWithinRange(eventType, dayOfMonth))
                .filter(eventType -> isApplicableEvent(eventType, dayOfMonth))
                .toList();
    }

    private static boolean isApplicableEvent(final EventType eventType, final int dayOfMonth) {
        if (eventType == CHRISTMAS_EVENT) {
            return isChristmasPeriod(dayOfMonth);
        }
        if (eventType == WEEKDAY_EVENT) {
            return isWeekday(dayOfMonth);
        }
        if (eventType == HOLIDAY_EVENT) {
            return isHoliday(dayOfMonth);
        }
        if (eventType == SPECIAL_EVENT) {
            return isSpecialDay(dayOfMonth);
        }
        return false;
    }

    private static boolean isDayOfMonthWithinRange(final EventType eventType, final int dayOfMonth) {
        return isGreaterThanStartEventDate(eventType, dayOfMonth)
                && isSmallerThanEndEventDate(eventType, dayOfMonth);
    }

    private static boolean isSmallerThanEndEventDate(final EventType eventType, final int dayOfMonth) {
        return eventType.getEndDate() >= dayOfMonth;
    }

    private static boolean isGreaterThanStartEventDate(final EventType eventType, final int dayOfMonth) {
        return eventType.getStartDate() <= dayOfMonth;
    }
}
