package christmas.view.input;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Scanner;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InputViewTest {

    @Nested
    @DisplayName("[InputView] 기능 테스트")
    class InputViewUnitTest {

        private static InputStream generateInput(String input) {
            return new ByteArrayInputStream(input.getBytes(UTF_8));
        }

        private static Stream<Arguments> provideDateAndMenuAmountInfo() {
            return Stream.of(
                    Arguments.of("1\n", "제로콜라-1,해산물파스타-3,양송이수프-2,타파스-4"),
                    Arguments.of("2\n", "제로콜라-2"),
                    Arguments.of("30\n", "해산물파스타-1,양송이수프-2,타파스-4"),
                    Arguments.of("31\n", "타파스-4,초코케이크-3"));
        }

        private InputStream createInputStream(String date, String menuAmountInfo) {
            return new SequenceInputStream(generateInput(date), generateInput(menuAmountInfo));
        }

        @DisplayName("날짜와 메뉴-개수 정보를 정상적으로 입력받는다")
        @ParameterizedTest
        @MethodSource("provideDateAndMenuAmountInfo")
        void readLine_날짜와_메뉴_개수_정보를_정상적으로_입력받는다(String date, String menuAmountInfo) {
            InputStream in = createInputStream(date, menuAmountInfo);
            System.setIn(in);
            Scanner scanner = new Scanner(System.in);

            assertAll(
                    () -> assertThat(scanner.nextLine())
                            .isEqualTo(date.trim()),
                    () -> assertThat(scanner.nextLine())
                            .isEqualTo(menuAmountInfo)
            );
        }
    }
}