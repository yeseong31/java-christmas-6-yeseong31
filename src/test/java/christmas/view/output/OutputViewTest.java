package christmas.view.output;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import christmas.domain.event.EventCalendar;
import christmas.domain.order.Orders;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OutputViewTest {

    @Nested
    @DisplayName("[OutputView] 기능 테스트")
    class OutputViewUnitTest {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        private final PrintStream standardOut = System.out;

        @BeforeEach
        void setUpOutputStreams() {
            System.setOut(new PrintStream(outputStream));
        }

        @AfterEach
        void restoreOutputStreams() {
            System.setOut(standardOut);
            outputStream.reset();
        }

        protected String getOutputStream() {
            return outputStream.toString().trim();
        }

        @Test
        @DisplayName("이벤트 플래너 프로그램 시작 문구를 출력한다")
        void printHelloPlanner() {
            String helloPlanner = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.";

            OutputView.printHelloPlanner();

            assertThat(getOutputStream()).isEqualTo(helloPlanner);
        }

        @Test
        @DisplayName("예상 방문 날짜 입력 안내 문구를 출력한다")
        void printAskReservationDate() {
            String askReservationDate = "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)";

            OutputView.printAskReservationDate();

            assertThat(getOutputStream()).isEqualTo(askReservationDate);
        }

        @Test
        @DisplayName("주문 메뉴 및 개수 입력 안내 문구를 출력한다")
        void printAskOrderMenuAndAmount() {
            String askOrderMenuAndAmount = "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)";

            OutputView.printAskOrderMenuAndAmount();

            assertThat(getOutputStream()).isEqualTo(askOrderMenuAndAmount);
        }

        @DisplayName("이벤트 혜택 미리 보기 문구를 출력한다")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 29, 30, 31})
        void printPreviewEventBenefits(int date) {
            String previewEventBenefits = format("12월 %d일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!", date);

            OutputView.printPreviewEventBenefits(date);

            assertThat(getOutputStream()).isEqualTo(previewEventBenefits);
        }

        static Orders receiveOrders() {
            String input = "티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1";
            List<String> menuAmounts = List.of(input.split(","));

            return Orders.from(menuAmounts, 3);
        }

        @Test
        @DisplayName("주문 메뉴 정보를 순차적으로 출력한다")
        void printOrderMenu() {
            Orders orders = receiveOrders();

            OutputView.printOrderMenu(orders);

            List<String> expectedResults = List.of(
                    "<주문 메뉴>", "티본스테이크 1개", "바비큐립 1개", "초코케이크 2개", "제로콜라 1개");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("할인 전 총주문 금액 정보를 출력한다")
        void printTotalOrderAmountBeforeDiscount() {
            Orders orders = receiveOrders();

            OutputView.printTotalOrderAmountBeforeDiscount(orders);

            List<String> expectedResults = List.of("<할인 전 총주문 금액>", "142,000원");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("증정 메뉴 정보를 출력한다")
        void printGiftMenu() {
            Orders orders = receiveOrders();
            EventCalendar eventCalendar = EventCalendar.of(3);

            OutputView.printGiftMenu(eventCalendar, orders);

            List<String> expectedResults = List.of("<증정 메뉴>", "샴페인 1개");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("혜택 내역 정보를 출력한다")
        void printBenefitsDetails() {
            Orders orders = receiveOrders();
            EventCalendar eventCalendar = EventCalendar.of(3);

            OutputView.printBenefitsDetails(eventCalendar, orders);

            List<String> expectedResults = List.of(
                    "<혜택 내역>", "크리스마스 디데이 할인: -1,200원", "평일 할인: -4,046원",
                    "특별 할인: -1,000원", "증정 이벤트: -25,000원");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("총혜택 금액 정보를 출력한다")
        void printTotalBenefitPrize() {
            Orders orders = receiveOrders();
            EventCalendar eventCalendar = EventCalendar.of(3);

            OutputView.printTotalBenefitPrice(eventCalendar, orders);

            List<String> expectedResults = List.of("<총혜택 금액>", "-31,246원");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("할인 후 예상 결제 금액 정보를 출력한다")
        void printEstimatedPriceAfterDiscount() {
            Orders orders = receiveOrders();
            EventCalendar eventCalendar = EventCalendar.of(3);

            OutputView.printEstimatedPriceAfterDiscount(eventCalendar, orders);

            List<String> expectedResults = List.of("<할인 후 예상 결제 금액>", "135,754원");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }

        @Test
        @DisplayName("이벤트 배지 정보를 출력한다")
        void printEventBadge() {
            Orders orders = receiveOrders();
            EventCalendar eventCalendar = EventCalendar.of(3);

            OutputView.printEventBadge(eventCalendar, orders);

            List<String> expectedResults = List.of("<12월 이벤트 배지>", "산타");
            for (String expectedResult : expectedResults) {
                assertThat(getOutputStream()).contains(expectedResult);
            }
        }
    }
}