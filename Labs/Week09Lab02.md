# Software Design and Architecture Week09 Lab 02 Worksheet


# Add an administration function to the Shipping Cost Calculator Application using a Repository

> ⚠ This lab assumes you have completed Lab 01.

The lab task is to merge the shipping cost administration function implemented by the **ShippingCostRepository** project in the Student code repository into the Spring Boot application created in Lab 01.

The idea is that before the shipping cost calculation function is run, the user will be prompted to enter the shipping cost data for each region (UK, EUR, ROW) via the console.

The application trace should show something like this when run:

```Text
Enter minCharge for: UK
0
Enter the cost per kg for: UK
10
ShippingCost[region=UK, minCharge=0.0, costPerKg=10.0]
Enter minCharge for: EUR
10
Enter the cost per kg for: EUR
20
ShippingCost[region=UK, minCharge=0.0, costPerKg=10.0]
ShippingCost[region=EUR, minCharge=10.0, costPerKg=20.0]
Enter minCharge for: ROW
20
Enter the cost per kg for: ROW
30
ShippingCost[region=ROW, minCharge=20.0, costPerKg=30.0]
ShippingCost[region=UK, minCharge=0.0, costPerKg=10.0]
ShippingCost[region=EUR, minCharge=10.0, costPerKg=20.0]
Select a region to ship to (UK, EUR, ROW):
EUR
Enter the weight of the package in kg: 100
Shipping cost to EUR: 2000.000000
```

### Hints and Tips

#### Provided Interfaces

Both the cost calculation and the administration interfaces are **provided** interfaces.

You could merge these into one interface, but it is better to keep them separate to follow the **Interface Segregation Principle (ISP)**.

You could provide both interfaces into the one runner class, but this could be considered a violation of the **Single Responsibility Principle (SRP)**, so it is better to create a separate runner class for the administration function and schedule it to run before the calculation runner class.

If you do this, you will need to create the console scanner once and pass it to both runner classes (you can only create a single instance of `Scanner(System.in)` in an application).

To do this you will need to add another Bean to the Spring Configuration class to provide the Scanner instance.

```Java
    @Bean
    @Scope("singleton")
    Scanner createScanner() {
        return new Scanner(System.in);
    }
```

The Spring container will automatically close the Scanner for you when the application ends, because Scanner implements the Closeable interface.

#### Required Interfaces

Both the Shipping Cost Query and the Shipping Cost Repository are **required** interfaces.

As with the example above, you could merge these into one interface, but it is better to keep them separate to follow the Interface Segregation Principle (ISP).

If you write two separate Adapter classes to implement these two required interfaces, the adapters will need to share the same underlying data storage.

**Question**: What lifetime scope should this data storage have?
