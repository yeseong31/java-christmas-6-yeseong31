package christmas.domain.event.constants;

public enum DateConstraint {

    DATE_LOWER_BOUND(1),
    DATE_UPPER_BOUND(31),
    FIXED_YEAR(2023),
    FIXED_MONTH(12),
    FIRST_DAY_OF_MONTH(1),
    LAST_DAY_OF_MONTH(31),
    CHRISTMAS_DAY(25),
    ;

    private final int value;

    DateConstraint(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
