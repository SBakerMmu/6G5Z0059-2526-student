# Software Design and Architecture Week10 Lab 02 Worksheet

# Create a version of the Calculate Shipping Use Case using the Request-Response Model pattern

> ⚠ This lab assumes you have completed Lab 01.

The task is to create a new use case based on the `Provided` interface for the `calculateshipping` use case using the Request-Response Model pattern.
The existing interface as defined:

```Java
public interface Provided {
    double calculate(String countryCode, double weight);
}

```

Create a new package in the `applicationcode` package called `calculateshippingrequestresponse`.

Create another CliAdapter class in the `infrastructure` package and configure it to run after the existing `ShippingCostCliAdapter` class. This will be used to run your new use case.

```Java
@Component
public class ShippingCostRequestResponseCliAdapter  implements org.springframework.boot.CommandLineRunner, org.springframework.core.Ordered {

    @Override
    public void run(String... args) throws Exception {
    }
        @Override
    public int getOrder() {
        return org.springframework.core.Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
```

Copy the code from the `calculateshipping` package into this new package. You will need to deal with package declarations and imports and ensure that the classes in your new package are being configured by the Spring Boot application correctly.

> ⚠ You might find that using package-specific configuration classes (one for each package) will make this easier to manage. This was covered in the advanced part of Lab01.

Get the ShippingCostRequestResponseCliAdapter to run the use case.

Create a request object class called `Request` in the `calculateshippingrequestresponse` package that encapsulates the input parameters for the `calculate` method.

```Java
public class Request {
    private final String countryCode;
    private final double weight;

    public Request(String countryCode, double weight) {
        //Validation code here
    }
}
```

Create a response object class called `Response` in the `calculateshippingrequestresponse` package that encapsulates the output parameters for the `calculate` method.

```Java
public class Response {

    private final String countryCode;
    private final double weight;
    private final String regionCode;
    private final double cost;


    public Response(String countryCode, double weight, String regionCode, double cost) {
        this.countryCode = countryCode;
        this.weight = weight;
        this.regionCode = regionCode;
        this.cost = cost;
    }
}
```
Change the provided interface and the use case implementation to use the new request and response objects.

```Java
public interface Provided {
    Response handle(Request request);
}
```
Change the `ShippingCostRequestResponseCliAdapter` class to use your new request and response objects.

When you run the application, you should see the same output as in Lab 01, but this time using the Request-Response Model pattern.

### Using the Scanner in multiple runners

Where there is more than one runner that uses the scanner, you will need to inject it using another Bean in the Spring Configuration class to provide the Scanner instance.

```Java
    @Bean
    @Scope("singleton")
    Scanner createScanner() {
        return new Scanner(System.in);
    }
```

The Spring container will automatically close the Scanner for you when the application ends (this is because Scanner implements the Closeable interface).

### Hints and Tips

If we have created a Use Case class for each action that a driving actor can take, then we can standardise on UseCase classes having a single method that takes a Request object and returns a Response object.

These are like Value Objects in that they are immutable and contain only data, but they are different to Value Objects because they specific to the Use Case and are not shared across different Use Cases. They do not implement the `equals` and `hashCode` methods because as they are not used in collections or compared for equality. Implementing `toString` can be useful for debugging.

> ⚠ You may see the terms "input model" and "output model" used instead of request and response objects. The word "model" conflicts with the "domain model" (meaning the entities, value objects and services modelling the business domain) and so we will use request and response to mean the input and output of a Use Case.

### Request Objects

We can now validate the inputs to the use case in the Request constructor, so the Use Case implementation can assume that the inputs are valid.

This places the responsibility for validating the inputs to the Request object in the Request class itself, rather than in the Use Case implementation and the Use Case implementation takes responsibility for validating the business rules using the domain model or queries to the database (only the Use Case implementation has access to the domain model in this architecture).

> ☑ Validation is a reason that it is not recommended to share request classes across different Use Case implementations, in addition to creating a coupling between Use Cases, each Use Case will have its own specific validation requirements.

### Response Objects

Like request objects, response objects are immutable and contain only data specific to the Use Case. Like request objects they do not need to implement the `equals` and `hashCode` methods because they are not used in collections or compared for equality, although implementing `toString` can be useful for debugging.

> ☑ It is not recommended to share response classes across different Use Case implementations. Again this would create coupling between Use Cases but response classes would potentially end up with optional fields which are set by some use cases and not by others, which would leak implementation details from one use case to another.

**Question**  What are the advantages and disadvantages of the Request-Response Model pattern compared to using multiple parameters on a Use Case interfaces ?

# Convert the Request-Response classes to Java Records (Advanced)

A request or response class is a kind of **Data Transfer Object (DTO)** - classes that contain only immutable data and have no behaviour.

Java **record classes** (introduced in Java 16) provide a way to create immutable data classes with minimal boilerplate code.

Java record classes support validation, so you can add validation logic to ensure that the data is valid when the record is created (as we did with the request class earlier).

Convert the `Request` and `Response` classes to Java Records using the documentation on [Java Records](https://docs.oracle.com/en/java/javase/17/language/records.html).

# Wrap a logging Decorator around the Request-Response version of the shipping cost calculation Use Case (Advanced)

The request-response version of a use case works well with the decorator pattern because there is a standard method signature for the use case interface. Adding additional properties to the request and response objects does not change the method signature.

Create a decorator class called CalculateShippingRequestResponseUseCaseDecorator in the `infrastructure.driving` package that implements the `Provided` interface for the request-response version of the shipping cost calculation use case.

> ⚠ We met the decorator pattern earlier in the module, review that section if you need a reminder of how the pattern works.

```Java
package uk.ac.mmu.lab1001.infrastructure.driving;

import uk.ac.mmu.lab1001.applicationcode.usecase.calculateshippingrequestresponse.Request;
import uk.ac.mmu.lab1001.applicationcode.usecase.calculateshippingrequestresponse.Response;
import uk.ac.mmu.lab1001.applicationcode.usecase.calculateshippingrequestresponse.Provided;

public class CalculateShippingRequestResponseUseCaseDecorator implements Provided {

    private double totalCost;
    private final Provided decoratee;

    public CalculateShippingRequestResponseUseCaseDecorator(Provided decoratee) {
        this.decoratee = decoratee;
    }


    @Override
    public Response handle(Request request) {
        System.out.format("%s%n", request);
        Response response = decoratee.handle(request);
        System.out.format("%s%n", response);
        return response;
    }
}
```
The challenge here is to get the Spring configuration to wrap the existing Use Case implementation with this decorator.

If configured correctly, when you run the application you should see the request and response objects printed to the console by the decorator.

```Plain Text
Select a country to ship to (RO,DK,RS,RU,AD,ZA,AL,SE,HR,SI,HU,EE,AT,AU,SK,SM,LI,LT,LU,LV,IE,ES,BA,BE,BG,PL,IN,PT,MC,MD,ME,IS,IT,BR,MK,FI,BY,MT,FR,CA,MX,CH,UA,JP,CN,GB,NL,CY,NO,US,CZ,GR,DE,NZ,VA): NZ
Enter the weight of the package in kg: 10
Request[countryCode=NZ, weight=10.0]
Response[countryCode=NZ, weight=10.0, regionCode=ROW, cost=55.0]
Shipping cost of 10.000000kg to NZ (ROW): 55.000000
```
## Using Spring Shell (Advanced)

This is very optional. In our examples so far, we have used the CommandLineRunner interface to run code when the application starts and this is fine for our examples and for supporting the assessment code.

However, Spring Boot also supports a more interactive approach using Spring Shell. This allows you to create a command-line interface (CLI) for your application where you can type commands to execute different functions. If you are comfortable with Spring Boot and want to explore further, you can look into using Spring Shell to create an interactive CLI.

See [Spring Shell Documentation](https://docs.spring.io/spring-shell/reference/index.html).
