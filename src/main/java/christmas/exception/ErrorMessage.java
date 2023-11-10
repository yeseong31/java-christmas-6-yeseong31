package christmas.exception;

import static christmas.exception.ErrorMessageConstants.INVALID_INTEGER_ERROR_MESSAGE;
import static christmas.exception.ErrorMessageConstants.INVALID_ORDERS_ERROR_MESSAGE;

public enum ErrorMessage {

    EMPTY_INTEGER_INPUT(INVALID_INTEGER_ERROR_MESSAGE),
    INVALID_INTEGER_INPUT(INVALID_INTEGER_ERROR_MESSAGE),
    INVALID_DATE_RANGE(INVALID_INTEGER_ERROR_MESSAGE),

    EMPTY_ORDERS_INPUT(INVALID_ORDERS_ERROR_MESSAGE),
    INVALID_ORDERS_INPUT(INVALID_ORDERS_ERROR_MESSAGE),
    NOT_FOUND_MENU(INVALID_ORDERS_ERROR_MESSAGE),
    INVALID_ORDER_AMOUNT(INVALID_ORDERS_ERROR_MESSAGE),
    INVALID_ORDER_FORMAT(INVALID_ORDERS_ERROR_MESSAGE),
    DUPLICATE_ORDER(INVALID_ORDERS_ERROR_MESSAGE),
    ;

    private static final String ERROR_MESSAGE_FORMAT = "%s %s";
    private static final String EXCEPTION_PREFIX = "[ERROR]";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return String.format(ERROR_MESSAGE_FORMAT, EXCEPTION_PREFIX, message);
    }
}
