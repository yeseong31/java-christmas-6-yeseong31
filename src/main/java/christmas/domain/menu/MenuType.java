package christmas.domain.menu;

public enum MenuType {

    APPETIZER("애피타이저"),
    MAIN_DISH("메인"),
    DESSERT("디저트"),
    BEVERAGE("음료"),
    ;

    private final String name;

    MenuType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
