

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SoftwareProduct {

    public static void main(String[] args) {
        infrastructure.ShippingCostDatabase shippingCostDatabase = new infrastructure.ShippingCostDatabase(); // Create an instance of ShippingCostDatabase
        infrastructure.ShippingCostDatabaseAdapter shippingCostDatabaseAdapter = new infrastructure.ShippingCostDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        applicationcode.usecase.listavailablecountries.Provided listCountries = applicationcode.usecase.listavailablecountries.Provided.create(shippingCostDatabaseAdapter);
        applicationcode.usecase.calculateshipping.Provided calculateShipping = applicationcode.usecase.calculateshipping.Provided.create(shippingCostDatabaseAdapter);
        infrastructure.ShippingCostCliAdapter cli = new infrastructure.ShippingCostCliAdapter(listCountries, calculateShipping); // Create an instance of the CLI
        cli.run();
    }






}
