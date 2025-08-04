package applicationcode.usecase.calculateshippingrequestresponse;

public class CalculateShippingRequest {
    private final String countryCode;
    private final double weight;

    public CalculateShippingRequest(String countryCode, double weight) {

        if (countryCode == null || countryCode.isBlank()) {
            throw new IllegalArgumentException("countryCode must not be null or blank");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be greater than 0");
        }
        this.countryCode = countryCode;
        this.weight = weight;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public double getWeight() {
        return weight;
    }

}

