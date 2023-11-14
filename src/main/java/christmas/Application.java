package christmas;

import camp.nextstep.edu.missionutils.Console;
import christmas.domain.controller.EventController;

public class Application {

    public static void main(String[] args) {
        EventController.start();
        Console.close();
    }
}
