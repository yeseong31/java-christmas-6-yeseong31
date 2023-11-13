package christmas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ParserTest {

    @Nested
    @DisplayName("[Parser] 정상 테스트")
    class ParserSuccessTest {

        @DisplayName("문자열 데이터를 정수형으로 정상적으로 변환한다")
        @ParameterizedTest
        @ValueSource(strings = {"1", "5", "31"})
        void 문자열_데이터를_정수형으로_정상적으로_변환한다(String input) {
            assertThatNoException().isThrownBy(
                    () -> Parser.parseToInt(input)
            );
        }

        @DisplayName("문자열 데이터를 구분자를 기준으로 정상적으로 split한다")
        @ParameterizedTest
        @ValueSource(strings = {"짜장면-1,짬뽕-1", "골드킹-1,바삭킹-2", "족발-1,막국수-2"})
        void 문자열_데이터를_구분자를_기준으로_정상적으로_split한다(String input) {
            assertThatNoException().isThrownBy(
                    () -> Parser.splitByDelimiter(input, ",")
            );
        }

        @DisplayName("일 정보를 통해 LocalDate 형으로 정상적으로 변환한다")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 20, 31})
        void 일_정보를_통해_LocalDate_형으로_정상적으로_변환한다(int dayOfMonth) {
            assertAll(
                    () -> assertThat(Parser.convertToLocalDate(dayOfMonth))
                            .isInstanceOf(LocalDate.class),
                    () -> assertThat(Parser.convertToLocalDate(dayOfMonth))
                            .isEqualTo(LocalDate.of(2023, 12, dayOfMonth))
            );
        }

        @DisplayName("LocalDate 형으로 된 날짜 데이터를 통해 요일 정보를 얻는다")
        @ParameterizedTest
        @CsvSource({"3,SUNDAY", "7,THURSDAY", "19,TUESDAY", "27,WEDNESDAY"})
        void LocalDate_형으로_된_날짜_데이터를_통해_요일_정보를_얻는다(int dayOfMonth, String inputDayOfWeek) {
            LocalDate localDate = Parser.convertToLocalDate(dayOfMonth);

            assertThat(Parser.convertToDayOfWeek(localDate))
                    .isEqualTo(DayOfWeek.valueOf(inputDayOfWeek));
        }

        @Test
        @DisplayName("가격 정보를 정해진 형식의 문자열로 변환한다")
        void 가격_정보를_정해진_형식의_문자열로_변환한다() {
            int price = 25000;
            String target = "25,000";

            assertThat(Parser.convertFormatPrice(price))
                    .isEqualTo(target);
        }
    }

    @Nested
    @DisplayName("[Parser] 예외 테스트")
    class ParserExceptionTest {

        @DisplayName("입력받은 값을 정수형이 아니라면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\n", "\t", "a", "!", "1 2"})
        void 입력받은_값을_정수형이_아니라면_예외가_발생한다(String input) {
            assertThatThrownBy(() -> Parser.parseToInt(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }
    }
}