# Software Design and Architecture Week03 Lab 01 Worksheet

There are multiple activities each week, and you will probably not get everything done in the timetabled lab sessions; therefore, it is highly recommended that you complete the labs in your own time each week to avoid falling behind.

Completing the labs will get you ready for writing the assignment code.

**Advanced** Labs are optional, but completing the Advanced Labs will introduce you to more advanced techniques and improve your design skills.

Week 3 labs are about some key object-oriented techniques – Value Objects and implementing Strategies.

IntelliJ has a quick and simple way of creating a new Java project that we can use for many of the labs.

Intelli-J File menu -\> New \> Project…

Provide a project name, chose a location and ensure that you have ticked the **Add sample code** box.

# Evaluate the design of the Polymorphic selling price product

Take a copy of the polymorphic selling price product from the `ValueObject` project from the Student code (you will need to get the code from the module GitHub repository if you do not have it already). Run the program to ensure it compiles and runs.

Examine the design of the classes in the `polymorphicsellingpriceproduct` package and ensure you understand how the polymorphic selling price product works.

What is wrong with the design of the Product class?, specifically the getPrice() method?

```java
class Product {

    private final MinimumPrice minimumPrice;
    private SellingPrice sellingPrice;

    public Product(FullPrice price, MinimumPrice minimumPrice) {
        this.sellingPrice = price;
        this.minimumPrice = minimumPrice;
    }

    public void applyDiscount(Discount discount) {

        sellingPrice = sellingPrice.applyDiscount(minimumPrice, discount);
    }

    public void removeDiscount() {

        sellingPrice = sellingPrice.removeDiscount();
    }

    public double getPrice() {
        return sellingPrice.get();
    }
}
```

# Add a Shipping Charge Value Object

Shopping websites usually calculate a shipping charge.

![Checkout page](images/week03_checkout1.png)

Using your copy of the polymorphic selling price product from the `polymorphicsellingpriceproduct` package:

- Add to the examples of using Value Objects for Full Price, Discount and Discounted Price by writing a Value Object called `ShippingCost` representing a Shipping and Handling cost (for example for a checkout process).
- Your `ShippingCost` Value Object should encapsulate a double value representing the shipping charge.
- A `ShippingCost` can have any non-negative value (0.0 or more).
- Your `ShippingCost` Value Object should follow all the rules for Value Objects. Use the Value Object checklist below to ensure you have covered all the points.

Extend the Product class to use your `ShippingCost` Value Object and extend the example code to show it in use. Your Product class should probably look like this:

```java
class Product {

    private final MinimumPrice minimumPrice;
    private final ShippingCost shippingCost;
    private SellingPrice sellingPrice;

    public Product(FullPrice price, MinimumPrice minimumPrice, ShippingCost shippingCost) {
        this.sellingPrice = price;
        this.minimumPrice = minimumPrice;
        this.shippingCost = shippingCost;
    }

    public ShippingCost getShippingCost() {
        return shippingCost;
    }

    //rest of implementation
}
```

Extend the example code to show the ShippingCost for the product and the total cost (the Selling Price + Shipping Cost). How will you handle the addition of a Selling Price and a Shipping Cost and the return of the total cost?

## Hints and Tips

> ☑ Most code you have written will use primitive types (strings, doubles and integers for example) to hold values. It is much better to work with dedicated classes that represent the values in your software design which encapsulate one or more primitive values. Although it is a bit more work to start with, every time you use an instance of your class rather than a primitive, you will see a benefit.

Value Objects encapsulate one or more primitive types in an immutable class. Immutable means that the data inside cannot be changed once created, so that they behave like primitive values (which also cannot be changed). To do this we declare all the fields as being final so that they can only be initialized in the constructor and never changed.

We can put all the pre-condition, post-condition and class invariant checking code in one place, guaranteeing that when we create an instance, it will be valid (providing you got your pre-conditions right of course). As these objects are immutable, once created, they always remain valid. Clients can then just use instance without having to do further checking, because of the guarantees we have built into the class.

Two instances of a Value Object are equal when the values of their instance variables are equal - in other words they have content equality. Value Object are classes that represent small things such as values or measures in a specific class, but in doing so provide a single place to put all the creation and usage logic.

Value Objects Checklist

  - There is no special Java keyword that makes a class a Value Object.

  - Value Objects are always immutable (like primitives) - their internal state cannot be changed once created.

  - Value Objects usually encapsulate a small number of primitive values or other Value Objects.

  - If they encapsulate reference types then those types MUST themselves be immutable.

  - Value Object implement content equality by overriding the equals() and the getHash() methods for testing the equality of contents.

  - Value Object implement override the toString() method.

  - We frequently create static final instances for common concepts such as zero or empty. This means we don't have to create new physical instances for common values, which can be an issue in large systems. Because the Value Object is immutable, there is no problem providing a static instance.

  - Being immutable, value objects are simpler to use and reason about since they can’t change state after creation. They are also inherently thread-safe, because multiple threads can use them simultaneously without risk of their state being changed.

A template for a ValueObject.
```java
import java.util.Objects;

public final class ValueObject {

    //Create a Constant object that represents a common value such as Zero or One
    public final static ValueObject Zero = new ValueObject(0);
    public final static ValueObject One  = new ValueObject(1);

    private final int value;

    public ValueObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ValueObject that = (ValueObject) obj;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString()
    {
        return String.format("%d", value);
    }
}
```

> ☑ Intelli J supports the automatic creation of the equals(), hashCode() and toString() methods, which saves a lot of time when creating Value Objects.
>
>
> Right click in the class body to get a context menu with the **Generate.. (Alt+Insert)** option
>
> This then provides a Generate menu with options for equal(), hashCode() and toString().
>
> The Generate menu also supports creating constructors, getters and other code.
>
>Professional developers make use all the code generation and refactoring tools provided by an IDE to save writing boilerplate code and improve productivity.


