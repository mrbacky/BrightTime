package brighttime.bll.util;

import java.text.DecimalFormat;

/**
 *
 * @author annem
 */
public class CostFormatter {

    public String formatCost(double cost) {
        DecimalFormat df = new DecimalFormat("#,###,###,###,###,##0.00 DKK");
        String formattedCost = df.format(cost);
        return formattedCost;
    }

}
