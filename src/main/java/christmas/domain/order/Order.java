package christmas.domain.order;

import static christmas.domain.menu.Menu.receiveMenu;
import static christmas.domain.menu.MenuType.APPETIZER;
import static christmas.domain.menu.MenuType.BEVERAGE;
import static christmas.domain.menu.MenuType.DESSERT;
import static christmas.domain.menu.MenuType.MAIN_DISH;
import static christmas.exception.ErrorMessage.INVALID_ORDER_AMOUNT;
import static christmas.service.Parser.convertFormatPrice;
import static java.lang.String.format;

import christmas.domain.event.constants.Discount;
import christmas.domain.menu.Menu;
import christmas.exception.ChristmasException;

public class Order {

    private static final int LEAST_ORDER_AMOUNT = 1;
    private static final int MAX_ORDER_AMOUNT = 20;
    private static final String ORDER_PRINT_FORMAT = "%s %s개";

    private final Menu menu;
    private final int amount;

    private Order(final Menu menu, final int amount) {
        this.menu = menu;
        this.amount = amount;
    }

    public static Order from(final String menuName, final int amount) {
        Menu menu = receiveMenu(menuName);
        validateAmount(amount);
        return new Order(menu, amount);
    }

    public int receiveDiscountPrice(Discount discount) {
        return discount.getDiscountPrice() * amount;
    }

    public int receiveOrderPrice() {
        return menu.getMenuPrice() * amount;
    }

    public boolean isAppetizerMenu() {
        return menu.getMenuType() == APPETIZER;
    }

    public boolean isMainDishMenu() {
        return menu.getMenuType() == MAIN_DISH;
    }

    public boolean isDessertMenu() {
        return menu.getMenuType() == DESSERT;
    }

    public boolean isBeverageMenu() {
        return menu.getMenuType() == BEVERAGE;
    }

    private static void validateAmount(int amount) {
        if (isLessThanMinimumAmount(amount) || isGreaterThanMaximumAmount(amount)) {
            throw ChristmasException.from(INVALID_ORDER_AMOUNT);
        }
    }

    private static boolean isGreaterThanMaximumAmount(int amount) {
        return amount > MAX_ORDER_AMOUNT;
    }

    private static boolean isLessThanMinimumAmount(int amount) {
        return amount < LEAST_ORDER_AMOUNT;
    }

    @Override
    public String toString() {
        return format(ORDER_PRINT_FORMAT, menu, convertFormatPrice(amount));
    }
}