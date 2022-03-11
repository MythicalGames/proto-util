package games.mythical.proto_util.testmodels;

import java.sql.Timestamp;

public class TimeSimpleSqlDto {
    private Timestamp timeValue;

    public Timestamp getTimeValue() {
        return timeValue;
    }

    public TimeSimpleSqlDto setTimeValue(Timestamp timeValue) {
        this.timeValue = timeValue;
        return this;
    }
}
