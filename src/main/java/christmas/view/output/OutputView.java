package christmas.view.output;

import static christmas.domain.event.constants.DateConstraint.FIXED_MONTH;
import static christmas.service.Parser.convertFormatPrice;
import static christmas.view.output.OutputMessage.ASK_ORDER_MENU_AND_AMOUNT;
import static christmas.view.output.OutputMessage.ASK_RESERVATION_DATE;
import static christmas.view.output.OutputMessage.HELLO_PLANNER;
import static christmas.view.output.OutputMessage.PREVIEW_EVENT_BENEFITS;
import static christmas.view.output.OutputMessage.PRINT_BENEFITS_DETAILS;
import static christmas.view.output.OutputMessage.PRINT_ESTIMATED_PRICE_AFTER_DISCOUNT;
import static christmas.view.output.OutputMessage.PRINT_GIFT_MENU;
import static christmas.view.output.OutputMessage.PRINT_MONTHLY_EVENT_BADGES;
import static christmas.view.output.OutputMessage.PRINT_ORDER_MENU;
import static christmas.view.output.OutputMessage.PRINT_TOTAL_BENEFIT_AMOUNT;
import static christmas.view.output.OutputMessage.PRINT_TOTAL_ORDER_AMOUNT_BEFORE_DISCOUNT;
import static java.lang.String.format;

import christmas.domain.event.EventCalendar;
import christmas.domain.event.constants.EventBadge;
import christmas.domain.event.constants.EventType;
import christmas.domain.order.Orders;
import java.util.Map;
import java.util.Map.Entry;

public class OutputView {

    public static final int NONE_PRICE = 0;
    private static final String NOT_SUBJECT_EVENT = "없음";
    private static final String PRINT_DISCOUNT_FORMAT = "%s: -%s원";
    private static final String PRINT_PRICE_FORMAT = "%s원";
    private static final String PRINT_BENEFIT_PRICE_FORMAT = "-%s원";

    public void printHelloPlanner() {
        println(HELLO_PLANNER.getMessage());
    }

    public void printAskReservationDate() {
        println(ASK_RESERVATION_DATE.getMessage());
    }

    public void printAskOrderMenuAndAmount() {
        println(ASK_ORDER_MENU_AND_AMOUNT.getMessage());
    }

    public void printPreviewEventBenefits(int date) {
        println(format(
                PREVIEW_EVENT_BENEFITS.getMessage(),
                FIXED_MONTH.getValue(),
                date));
    }

    public void printOrderMenu(Orders orders) {
        println();
        println(PRINT_ORDER_MENU.getMessage());
        for (String orderInfo : orders.receiveOrdersInfo()) {
            println(orderInfo);
        }
    }

    public void printTotalOrderAmountBeforeDiscount(Orders orders) {
        println();
        println(PRINT_TOTAL_ORDER_AMOUNT_BEFORE_DISCOUNT.getMessage());

        println(format(
                PRINT_PRICE_FORMAT,
                convertFormatPrice(orders.receiveTotalOrderPrice())));
    }

    public void printGiftMenu(EventCalendar eventCalendar, Orders orders) {
        println();
        println(PRINT_GIFT_MENU.getMessage());

        if (orders.isEligibleForPrize()) {
            println(eventCalendar.receiveEventMenuInfo());
            return;
        }

        println(NOT_SUBJECT_EVENT);
    }

    public void printBenefitsDetails(EventCalendar eventCalendar, Orders orders) {
        println();
        println(PRINT_BENEFITS_DETAILS.getMessage());

        Map<EventType, Integer> benefitDetails = eventCalendar.receiveBenefitDetails(orders);
        printAllBenefitDetails(benefitDetails);
    }

    private void printAllBenefitDetails(Map<EventType, Integer> benefitDetails) {
        if (benefitDetails.isEmpty()) {
            println(NOT_SUBJECT_EVENT);
            return;
        }

        for (Entry<EventType, Integer> entry : benefitDetails.entrySet()) {
            println(format(
                    PRINT_DISCOUNT_FORMAT,
                    entry.getKey().getName(),
                    convertFormatPrice(entry.getValue())));
        }
    }

    public void printTotalBenefitPrice(EventCalendar eventCalendar, Orders orders) {
        println();
        println(PRINT_TOTAL_BENEFIT_AMOUNT.getMessage());

        if (!orders.isEventTarget()) {
            println(format(PRINT_PRICE_FORMAT, NONE_PRICE));
            return;
        }

        println(format(
                PRINT_BENEFIT_PRICE_FORMAT,
                convertFormatPrice(eventCalendar.receiveTotalBenefitPrice(orders))));
    }

    public void printEstimatedPriceAfterDiscount(EventCalendar eventCalendar, Orders orders) {
        println();
        println(PRINT_ESTIMATED_PRICE_AFTER_DISCOUNT.getMessage());

        if (!orders.isEventTarget()) {
            println(format(PRINT_PRICE_FORMAT, convertFormatPrice(orders.receiveTotalOrderPrice())));
            return;
        }

        println(format(
                PRINT_PRICE_FORMAT,
                convertFormatPrice(eventCalendar.calculateEstimatedPriceAfterDiscount(orders))));
    }

    public void printEventBadge(EventCalendar eventCalendar, Orders orders) {
        println();
        println(PRINT_MONTHLY_EVENT_BADGES.getMessage());

        int price = eventCalendar.receiveTotalBenefitPrice(orders);
        println(EventBadge.receiveBadge(price).getName());
    }

    private void println(final String message) {
        System.out.println(message);
    }

    private void println() {
        System.out.println();
    }
}
