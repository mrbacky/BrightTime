package brighttime.bll.util;

/**
 * DurationConverter Class is used to convert the duration value in seconds to the
 * format hh:mm:ss and the other way around.
 *
 * @author Radoslav Backovsky
 */
public class DurationConverter {

    /**
     * Converts the time from seconds to the format hh:mm:ss.
     *
     * @param sec The time in seconds.
     * @return The formatted time.
     */
    public String sec_To_Format(int sec) {
        int hours, mins, secs;
        mins = (int) (sec / 60);
        while (mins > 60) {
            mins = mins % 60;
        }
        hours = (int) ((sec / 60) / 60);
        secs = sec % 60;
        String stringTime = String.format("%02d:%02d:%02d", hours, mins, secs);
        return stringTime;
    }

    /**
     * Converts the time from the format hh:mm:ss to seconds.
     *
     * @param formatString The time in the format hh:mm:ss.
     * @return The time in seconds.
     */
    public int format_To_Sec(String formatString) {
        String[] format = formatString.split(":");
        int hh, mm, ss, hours_In_Sec, mins_In_Sec, totalSec;
        hh = Integer.parseInt(format[0]);
        mm = Integer.parseInt(format[1]);
        ss = Integer.parseInt(format[2]);
        hours_In_Sec = hh * 3600;
        mins_In_Sec = mm * 60;
        totalSec = hours_In_Sec + mins_In_Sec + ss;
        return totalSec;
    }
}
