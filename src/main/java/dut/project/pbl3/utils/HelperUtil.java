package dut.project.pbl3.utils;

import java.sql.Timestamp;
import java.util.Arrays;

public class HelperUtil {

    private HelperUtil() {

    }

    public static double getDiffHour(Timestamp startAt, Timestamp endAt) {
        long startMiliseconds = startAt.getTime();
        long endMiliseconds = endAt.getTime();

        return (Math.abs(endMiliseconds - startMiliseconds) / 36e5);
    }

    public static String[] getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }
}
