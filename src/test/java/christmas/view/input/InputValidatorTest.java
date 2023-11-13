package christmas.view.input;

import static christmas.view.input.InputValidator.validateDate;
import static christmas.view.input.InputValidator.validateMenuAndAmountStrings;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputValidatorTest {

    @Nested
    @DisplayName("[InputValidator] 정상 테스트")
    class InputValidatorSuccessTest {

        @DisplayName("날짜 값이 유효한 값인지 확인한다")
        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "30", "31"})
        void 날짜_값이_유효한_값인지_확인한다(String input) {
            assertThatNoException().isThrownBy(
                    () -> validateDate(input));
        }

        @DisplayName("메뉴 및 개수 정보들이 유효한 값인지 확인한다")
        @ParameterizedTest
        @ValueSource(strings = {"짜장면-1,짬뽕-2", "치즈킹-1,콤비네이션-1"})
        void 메뉴_및_개수_정보들이_유효한_값인지_확인하다(String input) {
            assertThatNoException().isThrownBy(
                    () -> validateMenuAndAmountStrings(input));
        }
    }

    @Nested
    @DisplayName("[InputValidator] 예외 테스트")
    class InputValidatorExceptionTest {

        @DisplayName("날짜 값에 공백이 포함되어 있으면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\n", "\t"})
        void 날짜_값에_공백이_포함되어_있으면_예외가_발생한다(String input) {
            assertThatThrownBy(() -> validateDate(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }

        @DisplayName("날짜 값이 정해진 범위를 벗어나면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "32", "100"})
        void 날짜_값이_정해진_범위를_벗어나면_예외가_발생한다(String input) {
            assertThatThrownBy(() -> validateDate(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }

        @DisplayName("메뉴 및 개수 정보에 공백이 포함되어 있으면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\n", "\t"})
        void 메뉴_및_개수_정보에_공백이_포함되어_있으면_예외가_발생한다(String input) {
            assertThatThrownBy(() -> validateMenuAndAmountStrings(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }

        @DisplayName("메뉴 및 개수 정보에 잘못된 구분자 사용이 있다면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {",", ",,,", "짜장면-1,짬뽕-1,", ",치즈킹-1"})
        void 메뉴_및_개수_정보에_잘못된_구분자_사용이_있다면_예외가_발생한다(String input) {
            assertThatThrownBy(() -> validateMenuAndAmountStrings(input))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }
    }
}