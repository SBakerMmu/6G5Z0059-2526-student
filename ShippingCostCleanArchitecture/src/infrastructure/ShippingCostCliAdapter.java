package infrastructure;


import java.util.Set;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShippingCostCliAdapter {

    private final applicationcode.usecase.listavailablecountries.Provided listAvailableCountries;
    private final applicationcode.usecase.calculateshipping.Provided calculateShipping;

    public ShippingCostCliAdapter(applicationcode.usecase.listavailablecountries.Provided listAvailableCountries, applicationcode.usecase.calculateshipping.Provided calculateShipping) {
        this.listAvailableCountries = listAvailableCountries;
        this.calculateShipping = calculateShipping;
    }

    public void run() {




        Scanner scanner = new Scanner(System.in); // Create a Scanner object
        Set<String> availableCountries = listAvailableCountries.list();
        System.out.format("Select a country to ship to (%s): ", availableCountries.stream().collect(Collectors.joining(",")));
        String country = scanner.nextLine();
        System.out.print("Enter the weight of the package in kg: ");
        double weight = scanner.nextDouble();
        System.out.format("Shipping cost to %s: %f%n", country, calculateShipping.calculate(country,weight));
        scanner.close(); // Close the scanner
    }
}
