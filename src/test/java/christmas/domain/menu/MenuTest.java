package christmas.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @Nested
    @DisplayName("[Menu] 기능 테스트")
    class MenuUnitTest {

        @DisplayName("메뉴판에 포함된 이름인지 확인하고, Menu 객체를 반환한다")
        @ParameterizedTest
        @ValueSource(strings = {
                "양송이수프", "타파스", "시저샐러드",
                "티본스테이크", "바비큐립", "해산물파스타", "크리스마스파스타",
                "초코케이크", "아이스크림",
                "제로콜라", "레드와인", "샴페인"})
        void receiveMenu(String menuName) {
            assertAll(
                    () -> assertThatNoException().isThrownBy(
                            () -> Menu.receiveMenu(menuName)),
                    () -> assertThat(Menu.receiveMenu(menuName))
                            .isInstanceOf(Menu.class)
            );
        }
    }

    @Nested
    @DisplayName("[Menu] 예외 테스트")
    class MenuExceptionTest {

        @DisplayName("메뉴판에 포함되어 있지 않은 이름을 입력하면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"짜장면", "짬뽕", "치즈킹", "알리오올리오", "뿌링클"})
        void receiveMenuException(String menuName) {
            assertThatThrownBy(() -> Menu.receiveMenu(menuName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        }
    }
}