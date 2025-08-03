package infrastructure;


import applicationcode.usecase.calculateshipping.ShippingCost;

import java.util.Set;

public class ShippingCostDatabaseAdapter implements applicationcode.usecase.calculateshipping.Required, applicationcode.usecase.listavailablecountries.Required  {

    private final ShippingCostDatabase database;

    public ShippingCostDatabaseAdapter(ShippingCostDatabase database) {
        this.database = database;
    }

    @Override
    public String getRegionCode(String countryCode) {
        return database.regionMap.get(countryCode);
    }

    @Override
    public ShippingCost getShippingCostForRegion(String regionCode) {
        return new ShippingCost(database.minChargeMap.get(regionCode),database.costPerKgMap.get(regionCode));
    }

    @Override
    public Set<String> getRegionCodes() {
        return Set.copyOf(database.regionMap.values());
    }

    @Override
    public Set<String> getCountryCodes() {
        return Set.copyOf(database.regionMap.keySet());
    }
}
