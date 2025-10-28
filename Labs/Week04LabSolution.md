# Software Design and Architecture - Week 04 Lab Solutions

Suggested answers to the Week 4 Lab Exercises

## Create Singletons representing Currencies

````java
public class Currency {
    final static char POUND_SYMBOL = '£';
    final static char DOLLAR_SYMBOL = '$';
    final static char EURO_SYMBOL = '€';

    public final static Currency GBP = new Currency("GBP", "British Pound", POUND_SYMBOL, 2);
    public final static Currency USD = new Currency("USD", "US Dollar",DOLLAR_SYMBOL, 2);
    public final static Currency EUR = new Currency("EUR", "Euro",EURO_SYMBOL, 2);

    private final String code;
    private final String name;
    private final char symbol;
    private final int  decimals;

    private Currency(String code, String name, char symbol, int decimals) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
    }

    public String getName() {
        return name;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getDecimals() {
        return decimals;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Currency currency)) return false;
        return  code.equalsIgnoreCase(currency.code) &&
                name.equalsIgnoreCase(currency.name) &&
                (symbol == currency.symbol) &&
                (decimals == currency.decimals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, symbol, decimals);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",code, symbol);
    }
}
````

Note the more complex implementation of the `equals` method and the use of `Objects.hash` to compute a hash code from multiple fields.

In this example, Currencies are something that will remain constant through the lifetime of the program, and therefore we only want to create a single instance.

The name for an single instance is **Singleton**. There are several ways of creating a Singleton, but this lab shows the most common, that of making a final static instance.

Note that in the implementation code, the constructor is private, so that client code is forced to use one of the singleton instances - it cannot create its own instances.

Why is it important that any Singletons are final and immutable? because the singleton has global scope across the whole program. If you could change the value of a singleton, it could potentially break everywhere the singleton is used.

## Write a Money Factory using the Abstract Factory pattern.

First we need a Money class conforming to the Value Object pattern.

```java
public class Money {

    private final double amount;
    private final Currency currency;

    public Money(double amount, Currency currency) {
        this.currency = currency;
        double factor = Math.pow(amount, currency.getDecimals());
        this.amount = Math.round(amount * factor)/factor; //round to the number of currency decimals
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money money)) return false;
        return (amount == money.amount)  && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        //create a format string such as ".2f" to display a double to a number of decimals
        String formatString = String.format(".%df", currency.getDecimals());
        return currency.getSymbol() + String.format("%" + formatString , amount);
    }
}
```

The factory interface is simple.

```java
interface MoneyFactory {
    Money create(double amount);
}
```

Example concrete implementation.

```java
class EurMoneyFactory implements MoneyFactory{

    @Override
    public Money create(double amount) {
        return new Money(amount, Currency.EUR);
    }
}
```

If we pass the factory interface into a program, the choice of concrete implementation determines which currency the program will work in.

For example, the createMoney method will create money of various currencies, depending on which concrete factory was passed in.

```java
public final class Example {

    public static void run() {
        createMoney(new EurMoneyFactory());
        createMoney(new UsdMoneyFactory());
        createMoney( new GbpMoneyFactory());
    }

    private static void createMoney(MoneyFactory moneyFactory) {
        System.out.format("%s%n", moneyFactory.create(10d));
        System.out.format("%s%n", moneyFactory.create(10.9999d));
        System.out.format("%s%n", moneyFactory.create(0.9999d));
    }
}
```
## Write a Money Creator using a Factory Method

This is a very similar factory implementation, using an abstract base class rather than an interface.

```java
abstract class AbstractMoneyCreator {
    public Money create(double value){

        //Can add code before or after the factory method
        System.out.format("requested Money with value %f%n", value);
        Money money =  factoryMethod(value);
        System.out.format("created  %s%n", money);
        return money;
    }

    protected abstract Money factoryMethod(double value);
}
```

The key difference between the FactoryMethod and AbstractFactory patterns is that the `create` method delegates the work of actually creating the object to an `abstract factoryMethod` method, which is implemented by the various subclasses of `AbstractMoneyCreator`.

This means that you can add code before and after the call that creates the object as required.

An example sublcass providing a concrete implementation of the abstract factoryMethod.

```java
class UsdMoneyCreator extends AbstractMoneyCreator {

    @Override
    protected Money factoryMethod(double value) {
        return new Money(value, Currency.USD);
    }
}
```

The usage is very similar to the AbstractFactory example

```java

public final class Example {

    public static void run() {
        createMoney(new EurMoneyCreator());
        createMoney(new UsdMoneyCreator());
        createMoney( new GbpMoneyCreator());
    }

    private static void createMoney( AbstractMoneyCreator moneyCreator) {
        System.out.format("%s%n", moneyCreator.create(10d));
        System.out.format("%s%n", moneyCreator.create(10.9999d));
        System.out.format("%s%n", moneyCreator.create(0.9999d));
    }
}

```

Which is the better pattern to use - as ever it depends on the situation, the abstract creator version allows for some code to run before or after the object creation. The drawback is that all subclasses will have the same before and after code run, which may or may not be what is required.

A better solution is to combine both patterns, and use the abstract base class when there is some genuine implementation reuse needed - this gives you the most reuse and the most flexibility.

The client uses the MoneyFactory interface

```java
interface MoneyFactory {
    Money create(double amount);
}
```

We now can create an abstract base class and use the FactoryMethod pattern when it suits our purposes, or just write another concrete implementation of the MoneyFactory interface when we don't want to use the FactoryMethod pattern.

```java

abstract class AbstractMoneyCreator implements MoneyFactory {
    public Money create(double value){

        //Can add code before or after the factory method
        System.out.format("requested Money with value %f%n", value);
        Money money =  factoryMethod(value);
        System.out.format("created  %s%n", money);
        return money;
    }

    protected abstract Money factoryMethod(double value);
}
```

The example reverts back to using the MoneyFactory interface,

```java
public final class Example {

    public static void run() {
        createMoney(new EurMoneyCreator());
        createMoney(new UsdMoneyCreator());
        createMoney(new GbpMoneyCreator());

    }

    private static void createMoney(MoneyFactory moneyFactory) {
        System.out.format("%s%n", moneyFactory.create(10d));
        System.out.format("%s%n", moneyFactory.create(10.9999d));
        System.out.format("%s%n", moneyFactory.create(0.9999d));
    }
}
```


# Summary

The word **factory** tends gets used to mean any mechanism for creating an instance other than directly using a **class instance creation expression**, which in Java is the `new` keyword.

## static Helper Methods

A static helper method that creates and returns an instance of a class as either a concrete type or an abstract type (abstract class or interface type).

Reasons for doing this

- providing a better named method than the constructor for example `of`, `parse`, `fromString(String s)`,`fromInteger(int i)`.
- having two methods with the same parameter types, for example create a Rectangle from either `double width, double height` and `double width, double aspectRatio`.
- hiding complex logic required before creating an instance that shouldn't be in the class constructor (string parsing being an example)
- returning instances of different concrete types via an abstract type depending on the parameters passed to the method (this is a parameterized factory).
- returning null to indicate the instance could not be created rather than throwing an exception.

## Abstract Factory

Just like we can put different but related algorithms into different classes but implementing the same interface (Strategy Pattern), we can put different but related object creation code into different classes implementing the same abstract factory interface.

Now by choosing which concrete implementation of the abstract factory interface to use at runtime, we can vary what instance construction code gets used. You chose the concrete implementation of the abstract factory interface once, and that choice is used every time objects are created using the factory interface.

- The abstract factory interface can return concrete types or abstract types (interface or abstract class type).
- Furthermore, a single abstract factory interface can have multiple methods, so that your choice of concrete implementation control the construction of multiple related objects (we call this a family of related types because they vary together).

For example, take an Ecommerce order. We might want to use a different payment service and different shipping provider depending on the country the order is being shipped to.

Start with the two payment gateway and shipping provider interfaces.

```java
public interface PaymentService {
    boolean processPayment(double amount);
}
```

```java
public interface ShippingService {
    void ship(String orderId);
}
```

Create two concrete implementations of the Payment Service interface, one to use if ship to UK, one if ship to Europe.

```java

public class UkPayment implements PaymentService {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("UK payment processing...");
        return true;
    }
}

public class EurPayment implements PaymentService {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("EU payment processing...");
        return true;
    }
}
```
Create two concrete implementations of the Shipping Service interface, one to use if ship to UK, one if ship to Europe.

```java
public class UkShipping implements ShippingService {
    @Override
    public void ship(String orderId) {
        System.out.println("Shipping order to UK" + orderId + " (domestic)");
    }
}
```

```java
public class EurShipping implements ShippingService {
    @Override
    public void ship(String orderId) {
        System.out.println("Shipping order to EUR " + orderId + " (european)");
    }
}
```

In this scenario we want the concrete implementations of PaymentService and ShippingService vary *together* depending on the shipping destination.

We define an abstract factory interface to create both services.

```java
public interface EcommerceFactory {
    PaymentService createPaymentService();
    ShippingService createShippingService();
}
```

We can now create two concrete implementations of the EcommerceFactory interface, one for UK orders, one for European orders.

```java
// UkEcommerceFactory.java
public class UkEcommerceFactory implements EcommerceFactory {
    @Override
    public PaymentService createPaymentService() {
        return new UkPayment();
    }
    @Override
    public ShippingService createShippingService() {
        return new UkShipping();
    }
}

// EurEcommerceFactory.java
public class EurEcommerceFactory implements EcommerceFactory {
    @Override
    public PaymentService createPaymentService() {
        return new EurPayment();
    }
    @Override
    public ShippingService createShippingService() {
        return new EurShipping();
    }
}
```
My Ecommerce Order class now uses the EcommerceFactory interface to create and use the payment and shipping services.

```java
class Order {
    private final PaymentService paymentService;
    private final ShippingService shippingService;

    public Order(EcommerceFactory factory) {
        this.paymentService = factory.createPaymentService();
        this.shippingService = factory.createShippingService();
    }

    public void processOrder(String orderId, double amount) {
        if (paymentService.processPayment(amount)) {
            shippingService.ship(orderId);
        }
    }
}
```

When I create the Order I just need to pass in the correct concrete implementation of the EcommerceFactory interface depending on the shipping destination.

```java
EcommerceFactory factory;
if (shippingCountry.equals("UK")) {
    factory = new UkEcommerceFactory();
} else {
    factory = new EurEcommerceFactory();
}
Order order = new Order(factory);
order.processOrder("ORDER123", 100.00);
```

If later on we want to add support for US orders, we just need to create a new concrete implementation of the EcommerceFactory interface, and the corresponding PaymentService and ShippingService implementations.

```
EcommerceFactory factory;
if (shippingCountry.equals("UK")) {
factory = new UkEcommerceFactory();
} else if (shippingCountry.equals("US")) {
factory = new UsEcommerceFactory(); //NEW FACTORY FOR NEW DESTINATION
} else {
factory = new EurEcommerceFactory();
}
Order order = new Order(factory);
order.processOrder("ORDER123", 100.00);
```

## Factory Method Pattern

Factory Method pattern uses an abstract base class with a concrete method that creates an object, but delegates the actual object creation to an abstract method that is implemented by subclasses.
This allows the abstract base class to run code before and after the object creation, while allowing subclasses to vary the actual object that is created.





