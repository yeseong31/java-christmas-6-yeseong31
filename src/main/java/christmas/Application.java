package christmas;

import camp.nextstep.edu.missionutils.Console;
import christmas.controller.EventController;

public class Application {

    public static void main(String[] args) {
        EventController.start();
        Console.close();
    }
}
