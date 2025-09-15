package infrastructure.driven;


import java.util.Set;

public class ListAvailableCountriesDatabaseAdapter implements applicationcode.usecase.listavailablecountries.Required
{

    private final ShippingCostDatabase database;

    public ListAvailableCountriesDatabaseAdapter(ShippingCostDatabase database) {
        this.database = database;
    }

    @Override
    public Set<String> getRegionCodes() {
        return Set.copyOf(database.countryRegionMap.values());
    }

    @Override
    public Set<String> getCountryCodes() {
        return Set.copyOf(database.countryRegionMap.keySet());
    }

}
