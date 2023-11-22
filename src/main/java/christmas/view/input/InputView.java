package christmas.view.input;

import christmas.domain.dto.response.OrderDateResponseDto;

import java.util.List;

import static christmas.exception.ErrorMessage.INVALID_INTEGER_INPUT;
import static christmas.service.Parser.parseToInt;
import static christmas.service.Parser.splitByDelimiter;
import static christmas.view.input.InputValidator.validateDate;
import static christmas.view.input.InputValidator.validateMenuAndAmountStrings;

public class InputView {

    public static final String ORDER_MENU_SEPARATOR = ",";

    public static OrderDateResponseDto readDate() {
        final String input = readLine();
        validateDate(input);

        return new OrderDateResponseDto(
                parseToInt(input, INVALID_INTEGER_INPUT));
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
