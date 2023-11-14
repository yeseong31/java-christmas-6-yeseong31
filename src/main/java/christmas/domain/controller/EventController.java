package christmas.domain.controller;

import static christmas.view.output.OutputView.printErrorMessage;

import christmas.exception.ChristmasException;
import christmas.view.input.InputView;
import christmas.view.output.OutputView;

public class EventController {

    private EventController() {
    }

    public static void start() {
        int date = receiveDate();
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
