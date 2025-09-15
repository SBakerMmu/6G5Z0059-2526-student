package infrastructure.driven;

import applicationcode.Region;

import java.util.EnumMap;
import java.util.Map;

public class ShippingCostDatabase {

    final Map<Region, Double> costPerKgMap = new EnumMap<>(Region.class);
    final Map<Region, Double> minChargeMap = new EnumMap<>(Region.class);


    public ShippingCostDatabase() {
        costPerKgMap.put(Region.UK, 0.0);
        costPerKgMap.put(Region.EUR, 1.25d);
        costPerKgMap.put(Region.ROW, 5.5d);

        minChargeMap.put(Region.UK, 0.0);
        minChargeMap.put(Region.EUR, 0.0);
        minChargeMap.put(Region.ROW, 10.0);
    }
}
