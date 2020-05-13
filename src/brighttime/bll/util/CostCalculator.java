package brighttime.bll.util;

/**
 *
 * @author annem
 */
public class CostCalculator {

    public double calculateTotalCost(int durationSeconds, int rate) {
        double durationHours = (double) (durationSeconds * rate) / (60 * 60);
        return durationHours;
    }

}
