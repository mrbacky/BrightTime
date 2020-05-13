package brighttime.bll.util;

/**
 *
 * @author annem
 */
public class DurationFormatter {

    public String formatDuration(int durationSeconds) {
        int hours, min;
        min = (int) (durationSeconds / 60);
        while (min > 60) {
            min = min % 60;
        }
        hours = (int) ((durationSeconds / 60) / 60);
        String formattedDuration = String.format("%02d h %02d min", hours, min);
        return formattedDuration;
    }

}
