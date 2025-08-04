package applicationcode.usecase.calculateshippingrequestresponse;

public class CalculateShippingResponse {

    private final String countryCode;
    private final double weight;
    private final String regionCode;
    private final double cost;


    //Package private  constructor that uses the request object
    CalculateShippingResponse(CalculateShippingRequest request, String regionCode, double cost) {
        this(request.getCountryCode(), request.getWeight(), regionCode, cost);
    }

    public CalculateShippingResponse(String countryCode, double weight, String regionCode, double cost) {
        this.countryCode = countryCode;
        this.weight = weight;
        this.regionCode = regionCode;
        this.cost = cost;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getWeight() {
        return weight;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public double getCost() {
        return cost;
    }
}

