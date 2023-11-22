package christmas.domain.menu;

import static christmas.domain.menu.MenuType.APPETIZER;
import static christmas.domain.menu.MenuType.BEVERAGE;
import static christmas.domain.menu.MenuType.DESSERT;
import static christmas.domain.menu.MenuType.MAIN_DISH;
import static christmas.exception.ErrorMessage.NOT_FOUND_MENU;

import christmas.exception.ChristmasException;

public enum Menu {

    MUSHROOM_SOUP("양송이수프", 6000, APPETIZER),
    TAPAS("타파스", 5500, APPETIZER),
    CAESAR_SALAD("시저샐러드", 8000, APPETIZER),

    T_BONE_STEAK("티본스테이크", 55000, MAIN_DISH),
    BARBECUE_LIP("바비큐립", 54000, MAIN_DISH),
    SEAFOOD_PASTA("해산물파스타", 35000, MAIN_DISH),
    CHRISTMAS_PASTA("크리스마스파스타", 25000, MAIN_DISH),

    CHOCO_CAKE("초코케이크", 15000, DESSERT),
    ICE_CREAM("아이스크림", 5000, DESSERT),

    ZERO_COKE("제로콜라", 3000, BEVERAGE),
    RED_WINE("레드와인", 60000, BEVERAGE),
    CHAMPAGNE("샴페인", 25000, BEVERAGE),
    ;

    private final String name;
    private final int price;
    private final MenuType type;

    Menu(final String name, final int price, final MenuType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public static Menu receiveMenu(final String name) {
        for (Menu menu : values()) {
            if (menu.getName().equals(name)) {
                return menu;
            }
        }
        throw ChristmasException.from(NOT_FOUND_MENU);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public MenuType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
