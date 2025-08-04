import infrastructure.*;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SoftwareProduct {

    public static void main(String[] args) {
        infrastructure.ShippingCostDatabase shippingCostDatabase = new infrastructure.ShippingCostDatabase(); // Create an instance of ShippingCostDatabase

        PutRegionDatabaseAdapter putRegionDatabaseAdapter = new PutRegionDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.putregion.Provided putRegions = applicationcode.usecase.putregion.Provided.create(putRegionDatabaseAdapter);

        ListAvailableCountriesDatabaseAdapter listAvailableCountriesDatabaseAdapter = new ListAvailableCountriesDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.listavailablecountries.Provided listCountries = applicationcode.usecase.listavailablecountries.Provided.create(listAvailableCountriesDatabaseAdapter);

        CalculateShippingDatabaseAdapter shippingCostDatabaseAdapter = new CalculateShippingDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.calculateshipping.Provided calculateShipping = applicationcode.usecase.calculateshipping.Provided.create(shippingCostDatabaseAdapter);

        CalculateShippingRequestResponseDatabaseAdapter shippingCostDatabaseRequestResponseAdapter = new CalculateShippingRequestResponseDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.calculateshippingrequestresponse.Provided calculateShippingRequestResponse = new infrastructure.CalculateShippingUseCaseDecorator(applicationcode.usecase.calculateshippingrequestresponse.Provided.create(shippingCostDatabaseRequestResponseAdapter));

        infrastructure.ShippingCostDatabaseInitializer initializer = new infrastructure.ShippingCostDatabaseInitializer(putRegions); // Initialize the database with some data
        initializer.run();

        infrastructure.ShippingCostCliAdapter cli = new infrastructure.ShippingCostCliAdapter(listCountries, calculateShipping); // Create an instance of the CLI
        cli.run();
        ShippingCostRequestResponseCliAdapter cliRequestResponse = new ShippingCostRequestResponseCliAdapter(listCountries, calculateShippingRequestResponse); // Create an instance of the CLI
        cliRequestResponse.run();

    }






}
