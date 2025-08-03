package infrastructure;

import java.util.HashMap;
import java.util.Map;

public class ShippingCostDatabase {

    final Map<String, Double> costPerKgMap = new HashMap<>();
    final Map<String, Double> minChargeMap = new HashMap<>();
    final Map<String, String> regionMap = new HashMap<>();

    public ShippingCostDatabase() {

        // Europe
        regionMap.put("AL", "EUR");
        regionMap.put("AD", "EUR");
        regionMap.put("AT", "EUR");
        regionMap.put("BY", "EUR");
        regionMap.put("BE", "EUR");
        regionMap.put("BA", "EUR");
        regionMap.put("BG", "EUR");
        regionMap.put("HR", "EUR");
        regionMap.put("CY", "EUR");
        regionMap.put("CZ", "EUR");
        regionMap.put("DK", "EUR");
        regionMap.put("EE", "EUR");
        regionMap.put("FI", "EUR");
        regionMap.put("FR", "EUR");
        regionMap.put("DE", "EUR");
        regionMap.put("GR", "EUR");
        regionMap.put("HU", "EUR");
        regionMap.put("IS", "EUR");
        regionMap.put("IE", "EUR");
        regionMap.put("IT", "EUR");
        regionMap.put("LV", "EUR");
        regionMap.put("LI", "EUR");
        regionMap.put("LT", "EUR");
        regionMap.put("LU", "EUR");
        regionMap.put("MT", "EUR");
        regionMap.put("MD", "EUR");
        regionMap.put("MC", "EUR");
        regionMap.put("ME", "EUR");
        regionMap.put("NL", "EUR");
        regionMap.put("MK", "EUR");
        regionMap.put("NO", "EUR");
        regionMap.put("PL", "EUR");
        regionMap.put("PT", "EUR");
        regionMap.put("RO", "EUR");
        regionMap.put("RU", "EUR");
        regionMap.put("SM", "EUR");
        regionMap.put("RS", "EUR");
        regionMap.put("SK", "EUR");
        regionMap.put("SI", "EUR");
        regionMap.put("ES", "EUR");
        regionMap.put("SE", "EUR");
        regionMap.put("CH", "EUR");
        regionMap.put("UA", "EUR");
        regionMap.put("VA", "EUR");
        // GB, US, Canada
        regionMap.put("GB", "UK");
        regionMap.put("US", "ROW");
        regionMap.put("CA", "ROW");


        costPerKgMap.put("UK", 0.0);
        costPerKgMap.put("EUR", 1.25d);
        costPerKgMap.put("ROW", 5.5d);

        minChargeMap.put("UK", 0.0);
        minChargeMap.put("EUR", 0.0);
        minChargeMap.put("ROW", 10.0);
    }
}
