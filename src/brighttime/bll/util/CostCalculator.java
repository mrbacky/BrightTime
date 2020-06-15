package brighttime.bll.util;

/**
 *
 * @author annem
 */
public class CostCalculator {

    public double calculateTotalCost(int durationSeconds, int rate) {
        double cost = (double) (durationSeconds * rate) / (60 * 60);
        return cost;
    }

}
