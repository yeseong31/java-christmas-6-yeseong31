package christmas.view.output;

import static christmas.domain.event.constants.DateConstraint.FIXED_MONTH;
import static java.lang.String.format;

public enum OutputMessage {

    HELLO_PLANNER(
            format("안녕하세요! 우테코 식당 %d월 이벤트 플래너입니다.", FIXED_MONTH.getValue())),
    ASK_RESERVATION_DATE(
            format("%d월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)", FIXED_MONTH.getValue())),
    ASK_ORDER_MENU_AND_AMOUNT(
            "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)"),
    PREVIEW_EVENT_BENEFITS(
            "%d월 %d일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!"),

    PRINT_ORDER_MENU("<주문 메뉴>"),
    PRINT_TOTAL_ORDER_AMOUNT_BEFORE_DISCOUNT("<할인 전 총주문 금액>"),
    PRINT_GIFT_MENU("<증정 메뉴>"),
    PRINT_BENEFITS_DETAILS("<혜택 내역>"),
    PRINT_TOTAL_BENEFIT_AMOUNT("<총혜택 금액>"),
    PRINT_ESTIMATED_PRICE_AFTER_DISCOUNT("<할인 후 예상 결제 금액>"),
    PRINT_MONTHLY_EVENT_BADGES(
            format("<%d월 이벤트 배지>", FIXED_MONTH.getValue())),
    ;

    private final String message;

    OutputMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
