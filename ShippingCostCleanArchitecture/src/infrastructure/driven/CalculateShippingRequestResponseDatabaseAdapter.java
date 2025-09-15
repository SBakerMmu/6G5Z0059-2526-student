package infrastructure.driven;



import applicationcode.usecase.calculateshippingrequestresponse.ShippingCost;



public class CalculateShippingRequestResponseDatabaseAdapter implements applicationcode.usecase.calculateshippingrequestresponse.Required
{

    private final ShippingCostDatabase database;

    public CalculateShippingRequestResponseDatabaseAdapter(ShippingCostDatabase database) {
        this.database = database;
    }

    @Override
    public String getRegionCode(String countryCode) {
        return database.countryRegionMap.get(countryCode);
    }

    @Override
    public ShippingCost getShippingCostForRegion(String regionCode) {
        return new ShippingCost(database.minChargeMap.get(regionCode),database.costPerKgMap.get(regionCode));
    }
}
