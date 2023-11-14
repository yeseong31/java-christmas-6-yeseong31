package christmas.domain.controller;

import static christmas.view.output.OutputView.printErrorMessage;

import christmas.domain.order.Orders;
import christmas.exception.ChristmasException;
import christmas.view.input.InputView;
import christmas.view.output.OutputView;
import java.util.List;

public class EventController {

    private EventController() {
    }

    public static void start() {
        int date = receiveDate();
        Orders orders = receiveOrders(date);
    }

    private static Orders receiveOrders(int date) {
        OutputView.printAskOrderMenuAndAmount();

        return repeatInputOrders(date);
    }

    private static Orders repeatInputOrders(int date) {
        try {
            List<String> menuAndAmounts = InputView.readMenuAndAmountStrings();
            return Orders.from(menuAndAmounts, date);
        } catch (ChristmasException exception) {
            printErrorMessage(exception);
            return repeatInputOrders(date);
        }
    }

    private static int receiveDate() {
        OutputView.printHelloPlanner();
        OutputView.printAskReservationDate();

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
