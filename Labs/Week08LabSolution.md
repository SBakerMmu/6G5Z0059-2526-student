# Software Design and Architecture Week08 Lab Solutions

# Apply the Dependency Inversion Principle (DIP)

Examine the `OrderWithoutDip` class and design a Java interface that represents a contract for shipping a parcel from the point of view of the Order class. This interface should define a method for shipping a parcel, with parameters that are independent of the specific Gateway implementations.

The interface should look something like this:

```java
public enum DeliveryOption {
    NEXT_DAY, //Deliver the next day
    STANDARD, //2-3 days
    BUDGET, //5 days or more
}

public interface CourierGateway {
    void ship(double weightInLbs, DeliveryOption option);
}
```
This expresses the requirement that the Order class has for shipping a parcel, without reference to any specific Gateway implementation.
Note the use of the `DeliveryOption` enum makes that enum part of the interface.

You then create two classes, one for each Gateway implementation. Each class will implement the interface you defined and will internally use the respective Gateway implementation to perform the shipping operation.

```java
public class DefaultCourier implements CourierGateway {

    private final DefaultCourierGateway gateway = new DefaultCourierGateway();

    public DefaultCourier() {
    }

    @Override
    public void ship(double weightInLbs, DeliveryOption option) {
        //  Default Courier uses grams as weight unit and days as delivery time unit
        double weightInGrams = convertLbsToGrams(weightInLbs);
        int deliveryTimeInDays = switch (option) {
            case NEXT_DAY -> 1;
            case STANDARD -> 3;
            case BUDGET -> 5;
        };
        gateway.send(weightInGrams, deliveryTimeInDays);
    }

    private static double convertLbsToGrams(double lbs) {
        return lbs * 0.453592d * 1000d;
    }
}
```

```java
public class AlternativeCourier implements CourierGateway {

    private final AlternativeCourierGateway gateway = new AlternativeCourierGateway();

    public AlternativeCourier() {
    }

    @Override
    public void ship(double weightInLbs, DeliveryOption option) {
        double weightInKgs = convertLbsToKg(weightInLbs);
        AlternativeCourierService service = switch (option) {
            case NEXT_DAY -> AlternativeCourierService.PRIORITY_OVERNIGHT;
            case STANDARD -> AlternativeCourierService.STANDARD;
            case BUDGET -> AlternativeCourierService.ECONOMY;
        };
        gateway.shipPackage(weightInKgs, service);
    }

    private static double convertLbsToKg(double lbs) {
        return lbs * 0.453592d;
    }
}
```
Some example code that uses the `OrderWithDip` class:

```java
double parcel1 = 2.5; // lbs
double parcel2 = 10.0; // lbs

// Use the DefaultCourier
OrderWithDip orderWithDip1= new OrderWithDip(parcel1, DeliveryOption.NEXT_DAY, new DefaultCourier());
orderWithDip1.ship();

// Use the AlternativeCourier
OrderWithDip orderWithDip2 = new OrderWithDip(parcel2, DeliveryOption.BUDGET, new AlternativeCourier());
orderWithDip2.ship();
```
Note how the decision about which Gateway implementation to use is now made outside the `OrderWithDip` class.
It becomes the responsibility of some part of the calling code to decide which implementation to use and pass that into the `OrderWithDip` class.

### DRY Principle Refactor
You may have noticed that both Adapter classes have a method to convert from lbs. You should refactor this into a utility class as otherwise the knowledge of how to convert (the conversion constant, the precision and rounding) is in two places which is a DRY principle violation.

It is OK for the adapter classes to depend directly on a utility class as that class is unlikely to change often (the conversion factor from lbs to kgs is a standard). However, if you think that elements of the conversion algorithm were going to change (the precision and rounding for example) then you could use the Strategy pattern to encapsulate the different conversion algorithm and have the adapter classes depend on an abstraction for the conversion strategy.


# Compare the application of the Dependency Inversion Principle (DIP) to the Strategy pattern. (Advanced)

The application of the Dependency Inversion Principle (DIP) requires us to **inject** dependencies into a class rather than having the class create its own dependencies. We use the **Dependency Injection** pattern allows us to implement the **Dependency Inversion Principle (DIP)**.

In our solution above the `OrderWithDip` class has a concrete implementation of the CourierGateway interface injected into it via its constructor.

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. The Strategy pattern lets the algorithm vary independently of clients that use it.

The Strategy pattern is often realized using the Dependency Injection pattern. In this case the client class (the context) depends on an abstraction (the strategy interface) rather than a concrete implementation of the algorithm. The specific strategy implementation is injected into the client class at runtime.

However, you can also apply the Strategy pattern without using Dependency Injection by having the client class select a strategy implementation (rather than being given it by injection). In this case the difference is where the decision is made, inside the client class or outside the client class. For example, a class could decide to choose a different implementation of a sorting algorithm based on number of objects to be sorted.

### Strategy Pattern
- Strategy pattern is used when abstracting away algorithms for making calculations or decisions that would be part of the business logic of the application.
- The application of the Strategy pattern is typically local to a single context class to solve the specific problem of managing variations of a single, well-defined question.

### Dependency Inversion Principle and Dependency Injection Pattern
- Dependency Inversion is generally  referred to as the process abstracting away dependencies on infrastructure type code (code that deals with databases, files and external services).
- It is an architectural principle applied to an entire application to govern how components are created, wired together, and managed, using the Dependency Injection pattern.
- Dependencies are injected once at construction and do not change through the duration of a specific application instance. Changing the concrete dependencies would typically require some code or configuration change and starting a new runtime instance of the application.
- The mechanism for dependency injection is often (but not always) a **Dependency Injection (DI) container** (sometimes called an **Inversion of Control** container ). Java examples are Spring or Google Guice.

# Convert Lab01 to use the Spring DI container

Given the interface and adapter classes created in the DIP solution above, example Runner and Configuration classes for a Spring application using Dependency Injection could look like this:

```java
@Component
class Runner03 implements org.springframework.boot.CommandLineRunner, Ordered {

    private final CourierGateway gateway;

    Runner03(CourierGateway gateway)
    {
        this.gateway = gateway;
    }

    @Override
    public void run(String... args)  {
        System.out.format("Hello from %s%n", this.getClass());

        double parcel1 = 2.5; // lbs
        double parcel2 = 10.0; // lbs

        // Use the provided CourierGateway
        OrderWithDip orderWithDip1= new OrderWithDip(parcel1, DeliveryOption.NEXT_DAY, gateway);
        orderWithDip1.ship();

        // Use the AlternativeCourier
        OrderWithDip orderWithDip2 = new OrderWithDip(parcel2, DeliveryOption.BUDGET, gateway);
        orderWithDip2.ship();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
```
You would then need a Spring `@Configuration` class to define which implementation of the `CourierGateway` interface to use. For example, to use the DefaultCourier implementation:

```java
@Configuration
class OrderConfig {
    @Bean
    CourierGateway createCourierGateway() {
        return new DefaultCourier();
    }
}
```
or the AlternativeCourier implementation:
```java
@Configuration
class OrderConfig {
    @Bean
    CourierGateway createCourierGateway() {
        return new AlternativeCourier();
    }
}
```

# Use External Properties or Profiles to choose different implementations at runtime (Advanced)

An example is using Spring Profiles to choose between a default configuration and a production configuration.

If no profile is specified, the **default profile** is used, as shown by the log output

```Text
    : No active profile set, falling back to 1 default profile: "default"
```

Mark the original AppConfig class with the `@Profile("default")` annotation to indicate that it should be used when the default profile is active.

```java
@Configuration
@Profile("default")
class AppConfig {
}
```

Create another configuration class for the production profile, marked with the `@Profile("production")` annotation.

```java
@Configuration
@Profile("production")
class ProductionAppConfig {

}
```
In the `application.properties` file (found in the `resources` directory   you can specify the active profile by adding the following line:

```properties
spring.profiles.active=production
```
When you run the application with this configuration, Spring should:

- Display the log message indicating that the "production" profile is active. `: The following 1 profile is active: "production"`
- Use the `ProductionAppConfig` class to configure the beans instead of the default `AppConfig` class.






