import infrastructure.driven.*;
import infrastructure.driving.CalculateShippingUseCaseDecorator;
import infrastructure.driving.ShippingCostCliAdapter;
import infrastructure.driving.ShippingCostRequestResponseCliAdapter;

public class SoftwareProduct {

    public static void main(String[] args) {
        ShippingCostDatabase shippingCostDatabase = new ShippingCostDatabase(); // Create an instance of ShippingCostDatabase

        applicationcode.usecase.putregion.Required putRegionDatabaseAdapter = new PutRegionDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.putregion.Provided putRegions = applicationcode.usecase.putregion.Provided.create(putRegionDatabaseAdapter);

        applicationcode.usecase.listavailablecountries.Required listAvailableCountriesDatabaseAdapter = new ListAvailableCountriesDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.listavailablecountries.Provided listCountries = applicationcode.usecase.listavailablecountries.Provided.create(listAvailableCountriesDatabaseAdapter);

        applicationcode.usecase.calculateshipping.Required shippingCostDatabaseAdapter = new CalculateShippingDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.calculateshipping.Provided calculateShipping = applicationcode.usecase.calculateshipping.Provided.create(shippingCostDatabaseAdapter);

        applicationcode.usecase.calculateshippingrequestresponse.Required shippingCostRequestResponseDatabaseAdapter = new CalculateShippingRequestResponseDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.calculateshippingrequestresponse.Provided calculateShippingRequestResponse = new CalculateShippingUseCaseDecorator(applicationcode.usecase.calculateshippingrequestresponse.Provided.create(shippingCostRequestResponseDatabaseAdapter));

        ShippingCostDatabaseInitializer initializer = new ShippingCostDatabaseInitializer(putRegions); // Initialize the database with some data
        initializer.run();

        ShippingCostCliAdapter cli = new ShippingCostCliAdapter(listCountries, calculateShipping); // Create an instance of a CLI for using the calculate shipping method
        cli.run();
        ShippingCostRequestResponseCliAdapter cliRequestResponse = new ShippingCostRequestResponseCliAdapter(listCountries, calculateShippingRequestResponse); // Create an instance of the CLI using request response objects
        cliRequestResponse.run();

    }






}
