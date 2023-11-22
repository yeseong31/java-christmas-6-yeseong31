package christmas.controller;

import static christmas.view.output.OutputView.printAskOrderMenuAndAmount;
import static christmas.view.output.OutputView.printAskReservationDate;
import static christmas.view.output.OutputView.printBenefitsDetails;
import static christmas.view.output.OutputView.printErrorMessage;
import static christmas.view.output.OutputView.printEstimatedPriceAfterDiscount;
import static christmas.view.output.OutputView.printEventBadge;
import static christmas.view.output.OutputView.printGiftMenu;
import static christmas.view.output.OutputView.printHelloPlanner;
import static christmas.view.output.OutputView.printOrderMenu;
import static christmas.view.output.OutputView.printPreviewEventBenefits;
import static christmas.view.output.OutputView.printTotalBenefitPrice;
import static christmas.view.output.OutputView.printTotalOrderAmountBeforeDiscount;

import christmas.domain.event.EventCalendar;
import christmas.domain.order.Orders;
import christmas.exception.ChristmasException;
import christmas.view.input.InputView;
import java.util.List;

public class EventController {

    private EventController() {
    }

    public static void start() {
        int date = receiveDate();
        Orders orders = receiveOrders(date);
        EventCalendar eventCalendar = EventCalendar.of(date);

        showEventProcess(date, orders, eventCalendar);
    }

    private static void showEventProcess(final int date, final Orders orders, final EventCalendar eventCalendar) {
        printPreviewEventBenefits(date);
        printOrderMenu(orders);
        printTotalOrderAmountBeforeDiscount(orders);
        printGiftMenu(eventCalendar, orders);
        printBenefitsDetails(eventCalendar, orders);
        printTotalBenefitPrice(eventCalendar, orders);
        printEstimatedPriceAfterDiscount(eventCalendar, orders);
        printEventBadge(eventCalendar, orders);
    }

    private static Orders receiveOrders(final int date) {
        printAskOrderMenuAndAmount();

        return repeatInputOrders(date);
    }

    private static Orders repeatInputOrders(final int date) {
        try {
            final List<String> menuAndAmounts = InputView.readMenuAndAmountStrings();
            return Orders.from(menuAndAmounts, date);
        } catch (ChristmasException exception) {
            printErrorMessage(exception);
            return repeatInputOrders(date);
        }
    }

    private static int receiveDate() {
        printHelloPlanner();
        printAskReservationDate();

        return repeatInputDate();
    }

    private static int repeatInputDate() {
        try {
            return InputView.readDate();
        } catch (ChristmasException exception) {
            printErrorMessage(exception);
            return repeatInputDate();
        }
    }
}
