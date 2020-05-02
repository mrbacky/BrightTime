package brighttime.bll.util;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author rado
 */
public class EntryDurationCalculator {

    public Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

}
