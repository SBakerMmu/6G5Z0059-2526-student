﻿# Software Design and Architecture Week06 Lab 02 Worksheet

## The Mediator Pattern

The Façade Pattern is a way of getting classes to collaborate without them knowing about each other. In the Façade example above, the Game and the DiceShaker are independent classes which have no knowledge of each other. The Façade brings them together and handles the communication between them to achieve some goal (In the previous lab this was to play a simple dice rolling game).

There is another pattern, the **Mediator** pattern

The job of the Mediator pattern is also to provide communication between classes. Unlike the Facade pattern, client code talks directly to individual classes (so they need to be public to the Client), but the Mediator handles the communication between classes "behind the scenes".

The Mediator pattern is like the **Observer** pattern in that the mediated classes tell the Mediator about events that happened, and the Mediator decides what to do with that event.

The general form of the mediator patter has two interfaces, Mediator and Colleague.

```java
interface Colleague {
}

interface Mediator {
    void send(String s, Colleague colleague);
}
```

Write concrete Colleague classes, each of which implements Colleague and holds a reference to a Mediator.

```java

class ConcreteColleague1 implements Colleague {

    private final Mediator mediator;

    ConcreteColleague1(Mediator mediator) {
        this.mediator = mediator;
    }

    void notify(String s) {
        //Receive a notification from Mediator
        System.out.format("ConcreteColleague1 notified %s%n", s);
    }

    void send(String s) {
        mediator.send(s, this);
    }
}
```

Implement the second concrete Colleague class.

```java
class ConcreteColleague2 implements Colleague {

    private final Mediator mediator;

    ConcreteColleague2(Mediator mediator) {
        this.mediator = mediator;
    }

    void notify(String s) {
        System.out.format("ConcreteColleague2 notified %s%n", s);
    }

    void send(String s) {
        mediator.send(s, this);
    }
}

```

Both concrete colleague classes send messages to the Mediator, and in then the Mediator notifies the other concrete colleague.

A ConcreteMediator class implements the Mediator interface, and **routes** messages between Colleagues

```java

class ConcreteMediator implements Mediator {

    private ConcreteColleague1 colleague1;
    private ConcreteColleague2 colleague2;

    public void registerColleagues(ConcreteColleague1 colleague1, ConcreteColleague2 colleague2) {
        this.colleague1 = colleague1;
        this.colleague2 = colleague2;
    }

    @Override
    public void send(String s, Colleague colleague) {
        if (colleague == colleague1) {
            colleague2.notify(s);
        }
        if (colleague == colleague2) {
            colleague1.notify(s);
        }
    }
}
```
**Question**: why are we using the reference equality operator (==) in the `send()` method rather than calling the `equals()` method?

Write some client code to test the pattern.
The client code talks to the two concrete colleagues.

```java
ConcreteMediator mediator = new ConcreteMediator();
ConcreteColleague1 colleague1 = new ConcreteColleague1(mediator);
ConcreteColleague2 colleague2 = new ConcreteColleague2(mediator);
mediator.registerColleagues(colleague1,colleague2);

colleague1.send("Hello from 1");
colleague2.send("Hello from 2");

```
When run the output should be

```Text
ConcreteColleague2 notified Hello from 1
ConcreteColleague1 notified Hello from 2
```

Using the pattern, rather than Colleague1 interacting directly with Colleague2, the interactions are mediated by the Mediator. The Mediator could contain logic to decide what to do with the messages it receives and which colleagues to notify.

## Write a Mediator which mediates between an Ecommerce basket and a Discount algorithm.

For the lab exercise, write a Mediator which mediates between an Ecommerce basket and a Discount algorithm.

The two interfaces you will need are Colleague and Mediator.

```java
interface Colleague {
}
```

> ⚠ An interface without any methods is often called a **marker interface**. Marker interfaces are used to indicate or "mark" a class has having some capability, in this example the class can play the role of a Colleague in the Mediator pattern.

```java

interface Mediator {
    void onChanged(Colleague colleague);
}
```

First start with a simple Product class.

```java
class Product {
    private final String code;
    private final double price;

    Product(String code, double price) {
        this.code = code;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s £%.2f", code, price);
    }
}
```
The Ecommerce basket has methods to add and remove products, and to set a discount as a % (discount value of 1 = 100%)

```java
package pricingmediator;

import java.util.ArrayList;
import java.util.List;

class Basket implements Colleague {
    private final List<Product> products = new ArrayList<>();
    private final Mediator mediator;
    double discountApplied = 0;
    double totalBeforeDiscount = 0;
    double totalWithDiscount = 0;
    public Basket(Mediator mediator) {
        this.mediator = mediator;
    }

    public void addProduct(Product product) {
        products.add(product);
        updateTotalBeforeDiscount();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        updateTotalBeforeDiscount();
    }

    private void updateTotalBeforeDiscount() {
        totalBeforeDiscount = products.stream().mapToDouble(Product::getPrice).sum();
        mediator.onChanged(this);
    }

    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
        totalWithDiscount = totalBeforeDiscount * (1d - this.discountApplied);
    }

    public double getTotalBeforeDiscount() {return totalBeforeDiscount; }

    public double getTotalWithDiscount() {return totalWithDiscount;}

    public double getDiscountApplied() { return discountApplied *100d;    }
}

```

The Discounter class calculates the **higher** of:

- A 20% discount applied based on the total value of goods being purchased

- A discount applied based on a discount code.

```java
class Discounter implements Colleague {

    private static final double DISCOUNT_0 = 0.0d;
    private static final double DISCOUNT_10 = 0.1d;
    private static final double DISCOUNT_20 = 0.2d;
    private static final double DISCOUNT_30 = 0.3d;
    private static final double DISCOUNT_40 = 0.4d;
    private static final double DISCOUNT_50 = 0.5d;
    private final Mediator mediator;
    private double goodsDiscount = 0d;
    private double codeDiscount = 0d;
    private double discountApplied = 0d;

    public Discounter(Mediator mediator) {
        this.mediator = mediator;
    }

    public double getDiscount() {
        return discountApplied;
    }

    public void setGoodsValue(double goodsValue) {
        goodsDiscount = goodsValue > 100d ? DISCOUNT_20 : DISCOUNT_0;
        updateDiscountApplied();
    }

    public void setDiscountCode(String code) {

        codeDiscount = switch (code) {
            case "Discount10" -> DISCOUNT_10;
            case "Discount20" -> DISCOUNT_20;
            case "Discount30" -> DISCOUNT_30;
            case "Discount40" -> DISCOUNT_40;
            case "Discount50" -> DISCOUNT_50;
            default -> 0.0d;
        };

        updateDiscountApplied();
    }

    public void removeDiscountCode() {
        codeDiscount = DISCOUNT_0;
        updateDiscountApplied();
    }

    private void updateDiscountApplied() {
        discountApplied = Math.max(goodsDiscount,codeDiscount);
        mediator.onChanged(this);
    }

}
```
The lab task is to write a Mediator class (let's call it `PricingMediator`) that receives notifications when the goods value changes (sent by the Basket) or when a Discount code has been applied (sent by the Discounter).

Write some code to test your implementation these cases.

| Action                                  | Basket total with Discount            |
|-----------------------------------------|---------------------------------------|
| Add to basket a product of value 50     | Total price with discount 0%: 50.00   |
| Add to basket a product of value 250    | Total price with discount 20%: 240.00 |
| Apply Code “Discount10” to discounter   | Total price with discount 20%: 240.00 |
| Apply Code “Discount50” to discounter   | Total price with discount 50%: 150.00 |
| Remove Discount Code from discounter    | Total price with discount 20%: 240.00 |
| Remove product of value 250 from basket | Total price with discount 0%: 50.00   |
| Remove product of value 50 from basket  | Total price with discount 0%: 0.00    |



**Question**: Both Façade and Mediator are patterns that move collaboration code out of classes like Basket and Discounter, leaving them more general purpose. What are the differences between the two patterns, and when would you use one rather than the other?
