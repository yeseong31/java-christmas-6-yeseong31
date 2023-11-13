package christmas.domain.event.constants;

public enum Discount {

    CHRISTMAS_BASE_DISCOUNT(1000),
    CHRISTMAS_INCREASE_DISCOUNT(100),
    WEEKDAY_DISCOUNT(2023),
    HOLIDAY_DISCOUNT(2023),
    SPECIAL_DISCOUNT(1000),
    ;

    private final int discountPrice;

    Discount(final int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
