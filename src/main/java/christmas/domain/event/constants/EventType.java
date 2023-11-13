package christmas.domain.event.constants;

import static christmas.domain.event.constants.DateConstraint.CHRISTMAS_DAY;
import static christmas.domain.event.constants.DateConstraint.FIRST_DAY_OF_MONTH;
import static christmas.domain.event.constants.DateConstraint.LAST_DAY_OF_MONTH;

public enum EventType {

    CHRISTMAS_EVENT(
            "크리스마스 디데이 할인",
            FIRST_DAY_OF_MONTH,
            CHRISTMAS_DAY
    ),
    WEEKDAY_EVENT(
            "평일 할인",
            FIRST_DAY_OF_MONTH,
            LAST_DAY_OF_MONTH
    ),
    HOLIDAY_EVENT(
            "주말 할인",
            FIRST_DAY_OF_MONTH,
            LAST_DAY_OF_MONTH
    ),
    SPECIAL_EVENT(
            "특별 할인",
            FIRST_DAY_OF_MONTH,
            LAST_DAY_OF_MONTH),
    GIFT_EVENT(
            "증정 이벤트",
            FIRST_DAY_OF_MONTH,
            LAST_DAY_OF_MONTH),
    ;

    private final String name;
    private final DateConstraint startDate;
    private final DateConstraint endDate;

    EventType(final String name, final DateConstraint startDate, final DateConstraint endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getStartDate() {
        return startDate.getValue();
    }

    public int getEndDate() {
        return endDate.getValue();
    }
}
