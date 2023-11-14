package christmas.view.input;

import static christmas.exception.ErrorMessage.INVALID_INTEGER_INPUT;
import static christmas.service.Parser.parseToInt;
import static christmas.service.Parser.splitByDelimiter;
import static christmas.view.input.InputValidator.validateDate;
import static christmas.view.input.InputValidator.validateMenuAndAmountStrings;

import java.util.List;

public class InputView {

    public static final String ORDER_MENU_SEPARATOR = ",";

    public static int readDate() {
        final String input = readLine();
        validateDate(input);
        return parseToInt(input, INVALID_INTEGER_INPUT);
    }

    public static List<String> readMenuAndAmountStrings() {
        final String input = readLine();
        validateMenuAndAmountStrings(input);
        return splitByDelimiter(input, ORDER_MENU_SEPARATOR);
    }

    private static String readLine() {
        return camp.nextstep.edu.missionutils.Console.readLine();
    }
}
