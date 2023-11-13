package christmas.service;

import static christmas.domain.event.constants.DateConstraint.FIXED_MONTH;
import static christmas.domain.event.constants.DateConstraint.FIXED_YEAR;

import christmas.exception.ChristmasException;
import christmas.exception.ErrorMessage;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private static final String PRICE_FORMAT = "###,###";

    private Parser() {
    }

    public static String convertFormatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat(PRICE_FORMAT);
        return decimalFormat.format(price);
    }

    public static DayOfWeek convertToDayOfWeek(final LocalDate localDate) {
        return localDate.getDayOfWeek();
    }

    public static LocalDate convertToLocalDate(int dayOfMonth) {
        return LocalDate.of(
                FIXED_YEAR.getValue(),
                Month.of(FIXED_MONTH.getValue()),
                dayOfMonth);
    }

    public static int parseToInt(String input, ErrorMessage errorMessage) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw ChristmasException.of(errorMessage, exception);
        }
    }

    public static List<String> splitByDelimiter(String input, String delimiter) {
        return Arrays.stream(input.split(delimiter))
                .map(String::valueOf)
                .toList();
    }
}
