package christmas.view.input;

import static christmas.domain.event.constants.DateConstraint.DATE_LOWER_BOUND;
import static christmas.domain.event.constants.DateConstraint.DATE_UPPER_BOUND;
import static christmas.exception.ErrorMessage.EMPTY_INTEGER_INPUT;
import static christmas.exception.ErrorMessage.EMPTY_ORDERS_INPUT;
import static christmas.exception.ErrorMessage.INVALID_DATE_RANGE;
import static christmas.exception.ErrorMessage.INVALID_ORDER_FORMAT;
import static christmas.service.Parser.parseToInt;
import static christmas.view.input.InputView.ORDER_MENU_SEPARATOR;

import christmas.exception.ChristmasException;
import christmas.exception.ErrorMessage;

public class InputValidator {

    public static void validateDate(final String input) {
        validateContainWhiteSpace(input, EMPTY_INTEGER_INPUT);
        validateDateRange(input);
    }

    public static void validateMenuAndAmountStrings(final String input) {
        validateContainWhiteSpace(input, EMPTY_ORDERS_INPUT);
        validateStringWithSeparator(input, ORDER_MENU_SEPARATOR);
    }

    public static void validateStringWithSeparator(final String input, final String separator) {
        if (hasStartSeparator(input, separator) || hasEndSeparator(input, separator)) {
            throw ChristmasException.from(INVALID_ORDER_FORMAT);
        }
    }

    public static void validateContainWhiteSpace(final String input, final ErrorMessage errorMessage) {
        if (input.isBlank() || hasWhiteSpace(input)) {
            throw ChristmasException.from(errorMessage);
        }
    }

    private static void validateDateRange(final String input) {
        final int targetDate = parseToInt(input, INVALID_DATE_RANGE);
        if (isSmallerThanLowerBound(targetDate) || isGreaterThanUpperBound(targetDate)) {
            throw ChristmasException.from(INVALID_DATE_RANGE);
        }
    }

    private static boolean isGreaterThanUpperBound(final int targetDate) {
        return targetDate > DATE_UPPER_BOUND.getValue();
    }

    private static boolean isSmallerThanLowerBound(final int targetDate) {
        return targetDate < DATE_LOWER_BOUND.getValue();
    }

    private static boolean hasEndSeparator(final String input, final String separator) {
        return input.endsWith(separator);
    }

    private static boolean hasStartSeparator(final String input, final String separator) {
        return input.startsWith(separator);
    }

    private static boolean hasWhiteSpace(final String input) {
        return input.chars()
                .anyMatch(Character::isWhitespace);
    }
}
