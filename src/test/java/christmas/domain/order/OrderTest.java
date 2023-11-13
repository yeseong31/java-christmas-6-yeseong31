package christmas.domain.order;

import static christmas.domain.event.constants.Discount.HOLIDAY_DISCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @Nested
    @DisplayName("[Order] 기능 테스트")
    class OrderUnitTest {

        @DisplayName("메뉴 이름과 주문 개수를 입력받아 Order 객체를 생성한다")
        @ParameterizedTest
        @CsvSource({"타파스,1", "양송이수프,3", "초코케이크,5"})
        void from(String menuName, int amount) {
            assertThatNoException()
                    .isThrownBy(() -> Order.from(menuName, amount));
        }

        @DisplayName("주말에 메인 메뉴에 대한 할인 가격을 반환한다")
        @ParameterizedTest
        @CsvSource({"바비큐립,5,10115"})
        void receiveDiscountPrice(String menuName, int amount, int result) {
            Order order = Order.from(menuName, amount);

            assertThat(order.receiveDiscountPrice(HOLIDAY_DISCOUNT))
                    .isEqualTo(result);
        }

        @DisplayName("메뉴에 대한 주문 가격을 반환한다")
        @ParameterizedTest
        @CsvSource({"양송이수프,3,18000", "타파스,4,22000", "초코케이크,10,150000"})
        void receiveOrderPrice(String menuName, int amount, int totalPrice) {
            Order order = Order.from(menuName, amount);

            assertThat(order.receiveOrderPrice())
                    .isEqualTo(totalPrice);
        }

        @DisplayName("주문한 메뉴가 애피타이저라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(strings = {"양송이수프", "타파스", "시저샐러드"})
        void isAppetizerMenu(String menuName) {
            Order order = Order.from(menuName, 1);

            assertThat(order.isAppetizerMenu())
                    .isTrue();
        }

        @DisplayName("주문한 메뉴가 메인이라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(strings = {"티본스테이크", "바비큐립", "해산물파스타", "크리스마스파스타"})
        void isMainDishMenu(String menuName) {
            Order order = Order.from(menuName, 1);

            assertThat(order.isMainDishMenu())
                    .isTrue();
        }


        @DisplayName("주문한 메뉴가 디저트라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(strings = {"초코케이크", "아이스크림"})
        void isDessertMenu(String menuName) {
            Order order = Order.from(menuName, 1);

            assertThat(order.isDessertMenu())
                    .isTrue();
        }

        @DisplayName("주문한 메뉴가 음료라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(strings = {"제로콜라", "레드와인", "샴페인"})
        void isBeverageMenu(String menuName) {
            Order order = Order.from(menuName, 1);

            assertThat(order.isBeverageMenu())
                    .isTrue();
        }

        @DisplayName("Order 정보가 정해진 형식으로 출력되는지 확인한다")
        @ParameterizedTest
        @CsvSource({"타파스,1,타파스 1개", "초코케이크,10,초코케이크 10개"})
        void testToString(String menuName, int amount, String formattedOrder) {
            Order order = Order.from(menuName, amount);

            assertThat(order.toString())
                    .isEqualTo(formattedOrder);
        }
    }

    @Nested
    @DisplayName("[Order] 예외 테스트")
    class OrderExceptionTest {

        @DisplayName("메뉴판에 없거나 잘못된 형식의 이름 또는 주문 개수가 입력으로 들어오면 예외가 발생한다")
        @ParameterizedTest
        @CsvSource({
                ",2", " ,2", "\n,2", "\t,2", "\r,2",
                "새우필라프,2", "고르곤졸라피자,2", "맛초킹,2",
                "타파스,0", "타파스,-1"})
        void exceptionOrderMenuName(String menuName, int amount) {
            assertThatThrownBy(() -> Order.from(menuName, amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }
    }
}