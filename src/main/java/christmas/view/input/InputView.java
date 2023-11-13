package christmas.view.input;

import static christmas.service.Parser.parseToInt;
import static christmas.service.Parser.splitByDelimiter;
import static christmas.view.input.InputValidator.validateDate;
import static christmas.view.input.InputValidator.validateMenuAndAmountStrings;

import java.util.List;

public class InputView {

    public static final String ORDER_MENU_SEPARATOR = ",";

    public static InputView create() {
        return new InputView();
    }

    public int readDate() {
        String input = readLine();
        validateDate(input);
        return parseToInt(input);
    }

    public List<String> readMenuAndAmountStrings() {
        String input = readLine();
        validateMenuAndAmountStrings(input);
        return splitByDelimiter(input, ORDER_MENU_SEPARATOR);
    }

    private String readLine() {
        return camp.nextstep.edu.missionutils.Console.readLine();
    }
}
