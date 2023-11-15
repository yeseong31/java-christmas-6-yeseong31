package christmas.exception;

public class ChristmasException extends IllegalArgumentException {

    private ChristmasException(ErrorMessage errorMessage, Exception exception) {
        super(errorMessage.getMessage(), exception);
    }

    private ChristmasException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }

    public static ChristmasException of(ErrorMessage errorMessage, Exception exception) {
        return new ChristmasException(errorMessage, exception);
    }

    public static ChristmasException from(ErrorMessage errorMessage) {
        return new ChristmasException(errorMessage);
    }
}
