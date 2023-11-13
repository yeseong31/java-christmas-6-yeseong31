package christmas.domain.event;

import static christmas.domain.event.constants.EventType.CHRISTMAS_EVENT;
import static christmas.domain.event.constants.EventType.GIFT_EVENT;
import static christmas.domain.event.constants.EventType.SPECIAL_EVENT;
import static christmas.domain.event.constants.EventType.WEEKDAY_EVENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import christmas.domain.event.constants.EventType;
import christmas.domain.order.Orders;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class EventCalendarTest {

    @Nested
    @DisplayName("[EventCalendar] 기능 테스트")
    class EventCalendarUnitTest {

        @DisplayName("이벤트 캘린더를 정상적으로 생성한다")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 29, 30, 31})
        void of(int dayOfMonth) {
            assertThatNoException().isThrownBy(
                    () -> EventCalendar.of(dayOfMonth));
        }

        @DisplayName("할인 후 예상 결제 금액을 계산한다")
        @ParameterizedTest
        @CsvSource("3,티본스테이크-1 바비큐립-1 초코케이크-2 제로콜라-1")
        void calculateEstimatedPriceAfterDiscount(int dayOfMonth, String input) {
            List<String> menuAmounts = List.of(input.split(" "));
            Orders orders = Orders.from(menuAmounts, dayOfMonth);

            EventCalendar eventCalendar = EventCalendar.of(dayOfMonth);
            assertThat(eventCalendar.calculateEstimatedPriceAfterDiscount(orders))
                    .isEqualTo(135754);
        }

        @DisplayName("총혜택 금액을 계산한다")
        @ParameterizedTest
        @CsvSource("3,티본스테이크-1 바비큐립-1 초코케이크-2 제로콜라-1")
        void receiveTotalBenefitPrice(int dayOfMonth, String input) {
            List<String> menuAmounts = List.of(input.split(" "));
            Orders orders = Orders.from(menuAmounts, dayOfMonth);

            EventCalendar eventCalendar = EventCalendar.of(dayOfMonth);
            assertThat(eventCalendar.receiveTotalBenefitPrice(orders))
                    .isEqualTo(31246);
        }

        @DisplayName("혜택 내역 정보를 구성한다")
        @ParameterizedTest
        @CsvSource("3,티본스테이크-1 바비큐립-1 초코케이크-2 제로콜라-1")
        void receiveBenefitDetails(int dayOfMonth, String input) {
            List<String> menuAmounts = List.of(input.split(" "));
            Orders orders = Orders.from(menuAmounts, dayOfMonth);

            EventCalendar eventCalendar = EventCalendar.of(dayOfMonth);
            Map<EventType, Integer> details = eventCalendar.receiveBenefitDetails(orders);

            assertAll(
                    () -> assertThat(details.get(CHRISTMAS_EVENT)).isEqualTo(1200),
                    () -> assertThat(details.get(WEEKDAY_EVENT)).isEqualTo(4046),
                    () -> assertThat(details.get(SPECIAL_EVENT)).isEqualTo(1000),
                    () -> assertThat(details.get(GIFT_EVENT)).isEqualTo(25000));
        }

        @DisplayName("전달받은 날짜에 대한 이벤트 목록을 반환한다")
        @ParameterizedTest
        @CsvSource({
                "1,CHRISTMAS_EVENT HOLIDAY_EVENT",
                "3,CHRISTMAS_EVENT WEEKDAY_EVENT SPECIAL_EVENT",
                "26,WEEKDAY_EVENT",
                "31,WEEKDAY_EVENT SPECIAL_EVENT"})
        void receiveTargetEvents(int dayOfMonth, String eventNamesInput) {
            EventCalendar eventCalendar = EventCalendar.of(dayOfMonth);
            List<EventType> result = Arrays.stream(eventNamesInput.split(" "))
                    .map(EventType::valueOf)
                    .toList();

            List<EventType> eventTypes = eventCalendar.receiveTargetEvents();

            assertThat(eventTypes).isEqualTo(result);
        }

        @Test
        @DisplayName("증정 메뉴 정보를 문자열로 반환한다 (샴페인 1개)")
        void receiveEventMenuInfo() {
            EventCalendar eventCalendar = EventCalendar.of(1);

            assertThat(eventCalendar.receiveEventMenuInfo())
                    .isEqualTo("샴페인 1개");
        }

        @Test
        @DisplayName("증정 메뉴의 가격을 반환한다 (25000)")
        void receiveEventMenuPrice() {
            assertThat(EventCalendar.receiveEventMenuPrice())
                    .isEqualTo(25000);
        }

        @DisplayName("크리스마스 디데이 이벤트 기간이라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25})
        void isChristmasPeriod(int dayOfMonth) {
            assertThat(EventCalendar.isChristmasPeriod(dayOfMonth))
                    .isTrue();
        }

        @DisplayName("크리스마스 디데이 이벤트 기간이 아니라면 false를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {26, 27, 28, 29, 30, 31})
        void isNotChristmasPeriod(int dayOfMonth) {
            assertThat(EventCalendar.isChristmasPeriod(dayOfMonth))
                    .isFalse();
        }

        @DisplayName("특별 할인 이벤트 당일이라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {3, 10, 17, 24, 25, 31})
        void isSpecialDay(int dayOfMonth) {
            assertThat(EventCalendar.isSpecialDay(dayOfMonth))
                    .isTrue();
        }

        @DisplayName("특별 할인 이벤트 당일이 아니라면 false를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15,
                16, 18, 19, 20, 21, 22, 23, 26, 27, 28, 29, 30
        })
        void isNotSpecialDay(int dayOfMonth) {
            assertThat(EventCalendar.isSpecialDay(dayOfMonth))
                    .isFalse();
        }

        @DisplayName("평일 이벤트 당일이라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {
                3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 17,
                18, 19, 20, 21, 24, 25, 26, 27, 28, 31})
        void isWeekday(int dayOfMonth) {
            assertThat(EventCalendar.isWeekday(dayOfMonth))
                    .isTrue();
        }

        @DisplayName("주말 이벤트 당일이라면 true를 반환한다")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 8, 9, 15, 16, 22, 23, 29, 30})
        void isHoliday(int dayOfMonth) {
            assertThat(EventCalendar.isHoliday(dayOfMonth))
                    .isTrue();
        }
    }
}