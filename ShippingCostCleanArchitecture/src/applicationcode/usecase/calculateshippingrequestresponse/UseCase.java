package applicationcode.usecase.calculateshippingrequestresponse;

import applicationcode.domainmodel.Region;
import applicationcode.domainmodel.ShippingRegion;
import applicationcode.domainmodel.ShippingRegionFactory;

class UseCase implements Provided {
    private final Required required;

    UseCase(Required required) {
        this.required = required;
    }

    @Override
    public CalculateShippingResponse handle(CalculateShippingRequest request) {

        String regionCode = required.getRegionCode(request.getCountryCode());

        if (regionCode == null || regionCode.isEmpty()) {
            throw new IllegalArgumentException("No Region code found for country: " + request.getCountryCode());
        }

        ShippingCost shippingCost = required.getShippingCostForRegion(regionCode);

        if (shippingCost == null) {
            throw new IllegalArgumentException("No shipping cost found for region: " + regionCode);
        }

        Region region = Region.valueOf(regionCode);

        ShippingRegion shippingRegion = ShippingRegionFactory.create(region, shippingCost.getMinCharge(), shippingCost.getCostPerKg());

        double cost = shippingRegion.calculate(request.getWeight());

        return new CalculateShippingResponse(
                request,
                regionCode,
                cost
        );
    }
}
