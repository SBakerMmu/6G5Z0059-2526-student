import applicationcode.Provided;
import applicationcode.Required;
import infrastructure.driving.ShippingCostCliAdapter;
import infrastructure.driven.ShippingCostDatabase;
import infrastructure.driven.ShippingCostDatabaseAdapter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SoftwareProduct {

    public static void main(String[] args) {
        ShippingCostDatabase shippingCostDatabase = new ShippingCostDatabase(); // Create an instance of ShippingCostDatabase
        Required shippingCostDatabaseAdapter = new ShippingCostDatabaseAdapter(shippingCostDatabase); // Create an adapter for the database
        Provided shippingCostCalculator = Provided.create(shippingCostDatabaseAdapter); // Create an instance of Provided with the shipping cost database
        ShippingCostCliAdapter cli = new ShippingCostCliAdapter(shippingCostCalculator); // Create an instance of the CLI
        cli.run();
    }






}
