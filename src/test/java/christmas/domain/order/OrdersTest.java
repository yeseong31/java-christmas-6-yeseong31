package christmas.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrdersTest {

    @Nested
    @DisplayName("[Orders] 기능 테스트")
    class OrdersUnitTest {

        @DisplayName("'메뉴-개수' 리스트와 날짜를 전달받아 주문 리스트를 생성한다")
        @ParameterizedTest
        @CsvSource({"타파스-1 티본스테이크-3 제로콜라-3,3", "아이스크림-3 크리스마스파스타-4 바비큐립-4,25"})
        void from(String input, int dayOfMonth) {
            List<String> ordersInput = List.of(input.split(" "));

            assertThatNoException().isThrownBy(
                    () -> Orders.from(ordersInput, dayOfMonth));
        }

        @DisplayName("이벤트 달력에 별이 있으면 정해진 할인 금액을 반환한다")
        @ParameterizedTest
        @CsvSource({
                "타파스-1 티본스테이크-3 제로콜라-3,3", "타파스-1 티본스테이크-3 제로콜라-3,10",
                "타파스-1 티본스테이크-3 제로콜라-3,17", "타파스-1 티본스테이크-3 제로콜라-3,24",
                "타파스-1 티본스테이크-3 제로콜라-3,25", "타파스-1 티본스테이크-3 제로콜라-3,31"})
        void receiveSpecialEventDiscountPrice(String input, int dayOfMonth) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, dayOfMonth);

            assertThat(orders.receiveSpecialEventDiscountPrice())
                    .isEqualTo(1000);
        }

        @DisplayName("주말이라면 메인 메뉴에 대한 정해진 할인 금액을 반환한다")
        @ParameterizedTest
        @CsvSource({"타파스-1 티본스테이크-3 아이스크림-2,2,6069", "바비큐립-5,16,10115", "초코케이크-1,29,0"})
        void receiveHolidayEventDiscountPrice(String input, int dayOfMonth, int result) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, dayOfMonth);

            assertThat(orders.receiveHolidayEventDiscountPrice())
                    .isEqualTo(result);
        }

        @DisplayName("평일에 디저트 메뉴에 대한 정해진 할인 금액을 반환한다")
        @ParameterizedTest
        @CsvSource({"타파스-1 티본스테이크-3 아이스크림-2,3,4046", "아이스크림-1,14,0", "초코케이크-1,29,0"})
        void receiveWeekdayEventDiscountPrice(String input, int dayOfMonth, int result) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, dayOfMonth);

            assertThat(orders.receiveWeekdayEventDiscountPrice())
                    .isEqualTo(result);
        }

        @DisplayName("크리스마스 디데이 이벤트 기간에 대한 정해진 할인 금액을 반환한다")
        @ParameterizedTest
        @CsvSource({
                "1,1000", "2,1100", "3,1200", "4,1300", "5,1400",
                "6,1500", "7,1600", "8,1700", "9,1800", "10,1900",
                "11,2000", "12,2100", "13,2200", "14,2300", "15,2400",
                "16,2500", "17,2600", "18,2700", "19,2800", "20,2900",
                "21,3000", "22,3100", "23,3200", "24,3300", "25,3400",
                "26,0", "27,0", "28,0", "29,0", "30,0", "31,0"})
        void receiveChristmasEventDiscountPrice(int dayOfMonth, int result) {
            String input = "타파스-1 티본스테이크-3 아이스크림-2";
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, dayOfMonth);

            assertThat(orders.receiveChristmasEventDiscountPrice())
                    .isEqualTo(result);
        }

        @DisplayName("이벤트 적용 대상인지 확인한다")
        @ParameterizedTest
        @CsvSource({
                "제로콜라-1 양송이수프-1,false", "양송이수프-1,false", "타파스-1,false",
                "시저샐러드-3,true", "아이스크림-2,true", "크리스마스파스타-1,true"})
        void isEventTarget(String input, boolean status) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, 1);

            assertThat(orders.isEventTarget())
                    .isEqualTo(status);
        }

        @DisplayName("증정 이벤트 대상인지 확인한다")
        @ParameterizedTest
        @CsvSource({
                "제로콜라-1 티본스테이크-2,false", "티본스테이크-1 바비큐립-1,false", "초코케이크-7,false",
                "바비큐립-3,true", "양송이수프-1,레드와인-2,true", "해산물파스타-10,true"})
        void isEligibleForPrize(String input, boolean status) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, 1);

            assertThat(orders.isEligibleForPrize())
                    .isEqualTo(status);
        }

        @DisplayName("총주문 금액을 계산한다")
        @ParameterizedTest
        @CsvSource({
                "티본스테이크-5,275000", "바비큐립-10,540000", "아이스크림-2 초코케이크-4,70000",
                "제로콜라-1 해산물파스타-3 양송이수프-2 타파스-4,142000"})
        void receiveTotalOrderPrice(String input, int result) {
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, 1);

            assertThat(orders.receiveTotalOrderPrice())
                    .isEqualTo(result);
        }

        @Test
        @DisplayName("주문 정보를 리스트로 변환한다")
        void receiveOrdersInfo() {
            String input = "제로콜라-1 해산물파스타-3 양송이수프-2 타파스-4";
            List<String> ordersInput = List.of(input.split(" "));
            Orders orders = Orders.from(ordersInput, 1);

            List<String> result = orders.receiveOrdersInfo();

            assertAll(
                    () -> assertThat(result.size())
                            .isEqualTo(4),
                    () -> assertThat(result.contains("제로콜라 1개"))
                            .isTrue(),
                    () -> assertThat(result.contains("해산물파스타 3개"))
                            .isTrue(),
                    () -> assertThat(result.contains("양송이수프 2개"))
                            .isTrue(),
                    () -> assertThat(result.contains("타파스 4개"))
                            .isTrue()
            );
        }
    }

    @Nested
    @DisplayName("[Orders] 예외 테스트")
    class OrdersExceptionTest {

        @DisplayName("메뉴 이름 또는 주문 개수로 잘못된 입력이 전달되면 예외가 발생한다")
        @ParameterizedTest
        @CsvSource({
                "뿌링클-,1", "-1,1", "-,1",
                "뿌링클-1,1", "뿌링클-a,1", "뿌링클-!,1"})
        void exceptionFromOrdersInput(String input, int dayOfMonth) {
            List<String> ordersInput = List.of(input.split(" "));

            assertThatThrownBy(() -> Orders.from(ordersInput, dayOfMonth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }
    }
}