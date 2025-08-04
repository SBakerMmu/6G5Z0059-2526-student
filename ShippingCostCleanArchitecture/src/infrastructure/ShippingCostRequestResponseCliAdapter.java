package infrastructure;

import applicationcode.usecase.listavailablecountries.Provided;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ShippingCostRequestResponseCliAdapter {

    static class ListOfCountries {

        static ListOfCountries map(Set<String> countries)
        {
            return new ListOfCountries(countries.stream().collect(Collectors.joining(",")));
        }

        private final String value;

        ListOfCountries(String listOfCountries) {
            this.value = listOfCountries;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    static class ShippingCost {

        static ShippingCost map(applicationcode.usecase.calculateshippingrequestresponse.CalculateShippingResponse response) {
            return new ShippingCost(String.format("Shipping cost of %fkg to %s (%s): %f", response.getWeight(), response.getCountryCode(), response.getRegionCode(), response.getCost()));
        }
        private final String value;

        public ShippingCost(String shippingCost) {
            this.value = shippingCost;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final applicationcode.usecase.listavailablecountries.Provided listAvailableCountries;
    private final applicationcode.usecase.calculateshippingrequestresponse.Provided calculateShipping;

    public ShippingCostRequestResponseCliAdapter( Provided listAvailableCountries, applicationcode.usecase.calculateshippingrequestresponse.Provided calculateShipping) {
        this.listAvailableCountries = listAvailableCountries;
        this.calculateShipping = calculateShipping;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object
        System.out.format("Select a country to ship to (%s): ", ListOfCountries.map(listAvailableCountries.list()));
        String country = scanner.nextLine();
        System.out.print("Enter the weight of the package in kg: ");
        double weight = scanner.nextDouble();
        System.out.format("%s%n", ShippingCost.map(calculateShipping.handle(new applicationcode.usecase.calculateshippingrequestresponse.CalculateShippingRequest(country, weight))));
    }
}
