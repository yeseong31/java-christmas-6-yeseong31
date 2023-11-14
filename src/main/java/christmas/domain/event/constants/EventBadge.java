package christmas.domain.event.constants;

public enum EventBadge {

    NONE_BADGE("없음", 0),
    STAR_BADGE("별", 5000),
    TREE_BADGE("트리", 10000),
    SANTA_BADGE("산타", 20000),
    ;

    private final String name;
    private final int basePrice;

    EventBadge(final String name, final int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public static EventBadge receiveBadge(final int totalBenefitPrice) {
        if (totalBenefitPrice >= SANTA_BADGE.basePrice) {
            return SANTA_BADGE;
        }
        if (totalBenefitPrice >= TREE_BADGE.basePrice) {
            return TREE_BADGE;
        }
        if (totalBenefitPrice >= STAR_BADGE.basePrice) {
            return STAR_BADGE;
        }
        return NONE_BADGE;
    }

    public String getName() {
        return name;
    }
}
