package applicationcode.usecase.calculateshippingrequestresponse;

public interface Required {
    String getRegionCode(String countryCode);

    ShippingCost getShippingCostForRegion(String regionCode);
}

