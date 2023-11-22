package christmas.controller;

import christmas.domain.dto.response.OrderDateResponseDto;
import christmas.domain.event.EventCalendar;
import christmas.domain.order.Orders;
import christmas.exception.ChristmasException;
import christmas.view.input.InputView;

import java.util.List;

import static christmas.view.output.OutputView.*;

public class EventController {

    private EventController() {
    }

    public static void start() {
        OrderDateResponseDto orderDateResponseDto = receiveDate();
        Orders orders = receiveOrders(orderDateResponseDto.date());
        EventCalendar eventCalendar = EventCalendar.of(orderDateResponseDto.date());

        showEventProcess(orderDateResponseDto.date(), orders, eventCalendar);
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

    private static OrderDateResponseDto receiveDate() {
        printHelloPlanner();
        printAskReservationDate();

        return repeatInputDate();
    }

    private static OrderDateResponseDto repeatInputDate() {
        try {
            return InputView.readDate();
        } catch (ChristmasException exception) {
            printErrorMessage(exception);
            return repeatInputDate();
        }
    }
}
