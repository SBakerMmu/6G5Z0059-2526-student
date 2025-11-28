# Software Design and Architecture Week09 Lab Solutions

There are many possible solutions to these lab exercises. The solutions provided here are one possible approach that keeps the calculation and administration functions separate while adhering to the principles of the Ports and Adapters architecture.

## Application Code

The provided and required interfaces required to calculate shipping costs:
```Java
//Provided
public interface ShippingCostCalculation {
    double calculate(Region region, double weight);
}
//Required
public interface ShippingCostQuery {
    ShippingCost getShippingCostForRegion(Region region);
}
```

The provided and required (the repository) interfaces to administer shipping costs:
```Java
//Provided
public interface ShippingCostAdministration {
    void addShippingCost(Region region, double minCharge, double costPerKg);
    void updateShippingCost(Region region, double minCharge, double costPerKg);
    void deleteShippingCost(Region region);
    List<ShippingCost> getAllShippingCosts();
}

//Required
public interface ShippingCostRepository {
    void addShippingCost(ShippingCost shippingCost);
    void updateShippingCost(ShippingCost shippingCost);
    void deleteShippingCost(Region region);
    List<ShippingCost> getAllShippingCosts();
    ShippingCost getShippingCostForRegion(Region region);
}
```

Implementations of the provided interfaces:

```Java
public class ShippingCostCalculator implements ShippingCostCalculation {
    private final ShippingCostQuery required;

    public ShippingCostCalculator(ShippingCostQuery required) {
        this.required = required;
    }

    @Override
    public double calculate(Region region, double weight) {
        return getStrategy(region).calculate(weight);
    }


    ShippingCostStrategy getStrategy(Region region) {
        ShippingCost shippingCost = required.getShippingCostForRegion(region);
        return  switch (region) {
            case UK -> new UKShippingStrategy();
            case EUR -> new EURShippingStrategy(shippingCost.getCostPerKg());
            // Note: The ROW strategy uses both min charge and cost per kg
            case ROW -> new ROWShippingStrategy(shippingCost.getMinCharge(), shippingCost.getCostPerKg());
            default -> throw new IllegalArgumentException("Unknown region: " + region);
        };
    }
}
```

```Java
public class ShippingCostAdministrator implements ShippingCostAdministration {
    private final ShippingCostRepository required;

    public ShippingCostAdministrator(ShippingCostRepository required) {
        this.required = required;
    }


    @Override
    public void addShippingCost(Region region, double minCharge, double costPerKg) {
        if(getAllShippingCosts().stream().anyMatch(sc -> sc.getRegion().equals(region))) {
            throw new IllegalArgumentException("Shipping cost for region already exists");
        }
        ShippingCost shippingCost = new ShippingCost(region, minCharge, costPerKg);
        required.addShippingCost(shippingCost);
    }

    @Override
    public void updateShippingCost(Region region, double minCharge, double costPerKg) {
        Optional<ShippingCost> cost = getAllShippingCosts().stream().filter(sc -> sc.getRegion().equals(region)).findFirst();
        if(cost.isEmpty()) {
            throw new IllegalArgumentException("Shipping cost for region not found");
        }
        ShippingCost shippingCost = new ShippingCost(region, minCharge, costPerKg);
        required.updateShippingCost(shippingCost);
    }

    @Override
    public void deleteShippingCost(Region region) {
        Optional<ShippingCost> cost = getAllShippingCosts().stream().filter(sc -> sc.getRegion().equals(region)).findFirst();
        if(cost.isPresent()) {
            required.deleteShippingCost(region);
        }
    }

    @Override
    public List<ShippingCost> getAllShippingCosts() {
        return List.copyOf(required.getAllShippingCosts());
    }
}
```

There is a common database class that uses in-memory Sets and Maps to store the shipping cost data.
```Java
public class ShippingCostDatabase {
    public final Set<Region> regions = new HashSet<>();
    public final Map<Region, Double> costPerKgMap = new EnumMap<>(Region.class);
    public final Map<Region, Double> minChargeMap = new EnumMap<>(Region.class);
}
```

The two database adapters implement the required interfaces taking a dependency on the `ShippingCostDatabase` class.

```Java
public class ShippingCostQueryAdapter implements ShippingCostQuery {

    private final ShippingCostDatabase database;

    public ShippingCostQueryAdapter(ShippingCostDatabase database) {
        this.database = database;
    }

    @Override
    public ShippingCost getShippingCostForRegion(Region region) {
        return new ShippingCost(
                region,
                database.minChargeMap.get(region),
                database.costPerKgMap.get(region)
        );
    }
}
```

```Java
public class ShippingCostRepositoryAdapter implements uk.ac.mmu.lab0901.applicationcode.ShippingCostRepository {
    private final ShippingCostDatabase database;

    public ShippingCostRepositoryAdapter(ShippingCostDatabase database) {
        this.database = database;
    }


    @Override
    public void addShippingCost(ShippingCost shippingCost) {
        if (database.regions.add(shippingCost.getRegion())) {
            database.costPerKgMap.put(shippingCost.getRegion(), shippingCost.getCostPerKg());
            database.minChargeMap.put(shippingCost.getRegion(), shippingCost.getMinCharge());
        }
    }

    @Override
    public void updateShippingCost(ShippingCost shippingCost) {
        database.costPerKgMap.replace(shippingCost.getRegion(), shippingCost.getCostPerKg());
        database.minChargeMap.replace(shippingCost.getRegion(), shippingCost.getMinCharge());
    }

    @Override
    public void deleteShippingCost(Region region) {
        database.costPerKgMap.remove(region);
        database.minChargeMap.remove(region);
        database.regions.remove(region);
    }

    @Override
    public List<ShippingCost> getAllShippingCosts() {
        return database.regions.stream().map(region -> createShippingCost(region)).toList();
    }

    @Override
    public ShippingCost getShippingCostForRegion(Region region) {
        return createShippingCost(region);
    }

    private ShippingCost createShippingCost(Region region) {
        double costPerKg = database.costPerKgMap.get(region);
        double minCharge = database.minChargeMap.get(region);
        ShippingCost shippingCost = new ShippingCost(region, minCharge, costPerKg);
        return shippingCost;
    }

}
```

There are two separate runner classes that provide a UI for the administration and calculation of shipping costs.

```Java
@Component
class ShippingCostAdministrationCliAdaptor implements org.springframework.boot.CommandLineRunner, org.springframework.core.Ordered {

    private final Scanner scanner;
    private final ShippingCostAdministration shippingCostAdministration;

    public ShippingCostAdministrationCliAdaptor(Scanner scanner, ShippingCostAdministration shippingCostAdministration) {
        this.scanner = scanner;
        this.shippingCostAdministration = shippingCostAdministration;
    }

    @Override
    public void run(String... args) throws Exception {

        for (Region region : Region.values()) {
            System.out.format("Enter minCharge for: %s%n", region);
            double minCharge = scanner.nextDouble();
            System.out.format("Enter the cost per kg for: %s%n", region);
            double cost = scanner.nextDouble();
            shippingCostAdministration.addShippingCost(region, minCharge, cost);
            for (ShippingCost shippingCost: shippingCostAdministration.getAllShippingCosts()){
                System.out.format("%s%n",shippingCost);
            }
        }
            }


    @Override
    public int getOrder() {
        return org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
    }
}
```

```Java
 public class ShippingCostCalculationCliAdapter implements org.springframework.boot.CommandLineRunner, org.springframework.core.Ordered {
    private final Scanner scanner;
    private final ShippingCostCalculation shippingCostCalculator;

    public ShippingCostCalculationCliAdapter(Scanner scanner,ShippingCostCalculation shippingCostCalculator) {
        this.scanner = scanner;
        this.shippingCostCalculator = shippingCostCalculator;
    }

     @Override
     public void run(String... args)  {
        System.out.format("Select a region to ship to (UK, EUR, ROW): %n");
        String region = scanner.next();
        System.out.format("Enter the weight of the package in kg: ");
        double weight = scanner.nextDouble();

        switch (region.toLowerCase()) {
            case "uk":
                System.out.format("Shipping cost to %s: %f%n", region, shippingCostCalculator.calculate(Region.UK, weight));
                break;
            case "eur":
                System.out.format("Shipping cost to %s: %f%n", region, shippingCostCalculator.calculate(Region.EUR, weight));
                break;
            case "row":
                System.out.format("Shipping cost to %s: %f%n", region, shippingCostCalculator.calculate(Region.ROW, weight));
                break;
            default:
                System.out.println("Shipping to this region is not available.");
        }
    }

     @Override
     public int getOrder() {
         return org.springframework.core.Ordered.LOWEST_PRECEDENCE;
     }
 }
```
The Spring Configuration class to wire up the application.

Note that the `ShippingCostDatabase` and `Scanner` beans are singletons, while all other beans are prototypes. This is because we need to share the same instance of the database with all the database adapters and same instance of the scanner with all the CLI adapters.


```Java
@Configuration
class AppConfig {

    @Bean
    @Scope("prototype")
    ShippingCostCalculation createCalculation(ShippingCostQuery required) {
        return new ShippingCostCalculator(required);
    }

    @Bean
    @Scope("prototype")
    ShippingCostQuery createQuery(ShippingCostDatabase database) {
        return new ShippingCostQueryAdapter(database);
    }

    @Bean
    @Scope("prototype")
    ShippingCostAdministration createAdministration(ShippingCostRepository required) {
        return new ShippingCostAdministrator(required);
    }

    @Bean
    @Scope("prototype")
    ShippingCostRepository createRepository(ShippingCostDatabase database) {
        return new ShippingCostRepositoryAdapter(database);
    }

    @Bean
    @Scope("singleton")
    ShippingCostDatabase createShippingCostDatabase() {
        return new ShippingCostDatabase();
    }

    @Bean
    @Scope("singleton")
    Scanner createScanner() {
        return new Scanner(System.in);
    }

}
```

## Interface and Class Names

Here we have two provided (`ShippingCostCalculation` and `ShippingCostAdministration`) interfaces so we can't call both of them `Provided` as in simple example.

Therefore, we have given them more descriptive names that reflect their purpose in the application.

The provided interfaces have been named to reflect a *capability*, *process* or *outcome* (Calculation and Administration) and the concrete classes implementing these interfaces are named as being the code that is used to implement or perform the capability, process or outcome (Calculator and Administrator). These subtle naming differences in naming help to clarify the purpose of the interfaces and classes in the application and the Java file names.

An alternative approach would be use the 'Provided' interface names as before, but put the two provided interfaces into separate packages (e.g. `calculation` and `administration`). Then we are using the namespacing provided by the package names to distinguish between the two 'Provided' interfaces.

## Making applicationCode.ShippingCostCalculator a Spring Bean

The `ShippingCostCalculator` class needs to be made into a Spring Bean so that it can be injected into the `ShippingCostCalculationCliAdapter` class. Likewise, the `ShippingCostAdministrator` class also needs to be made into a Spring Bean so that it can be injected into the `ShippingCostAdministrationCliAdapter` class.

This is done in the `AppConfig` class above, for example by adding the `createCalculation` method that depends on the `ShippingCostQuery` interface and returns a instance of the `ShippingCostCalculation` interface.

As the AppConfig class is in a separate package, ShippingCostCalculator needs to be public (not just package private) as we need the concrete class constructor to be visible to the AppConfig class.

In fact, in this implementation, all the concrete classes need to be visible to the top level of the architecture the "software product" in order to configure (or glue) all the parts of the application together.

If we wanted to enforce strict privacy of our concrete classes (to prevent them being used directly by another developer and breaking our architecture) we could use the Java trick of allowing static methods in public interfaces, so each interface gets a static factory method to create instances of the concrete class. This keeps the concrete class package private, but allows the Spring configuration class to call the factory method to create the bean.

```java
public interface ShippingCostCalculation {
    double calculate(Region region, double weight);
    static ShippingCostCalculation create(ShippingCostQuery required) {
        return new ShippingCostCalculator(required);
    }
}
```
Now ShippingCostCalculator can remain package private, and the global Spring configuration calls the static factory method to create the bean.

```java
    @Bean
    @Scope("prototype")
    ShippingCostCalculation createCalculation(ShippingCostQuery required) {
        return ShippingCostCalculation.create(required);
    }
```

The issue with this approach is that the interface now has implementation code in it, and you can only have one concrete implementation of the interface. If you wanted to have multiple implementations of the interface (e.g. for testing ) then this approach has limitations. Possibly the ultimate solution is to use the **Abstract Factory** pattern, so that the application code package provides a public abstract factory interface, and multiple public factory implementations to suport various scenarios. The Sprint configuration class can then choose which factory implementation to use to create the beans. The actual concrete classes can then remain package private.

Pragmatically, in a small project like this lab exercise, it is probably acceptable to have the concrete classes public and rely on developer discipline to not use them directly outside the Spring configuration. In a larger system other approaches may be more appropriate.

