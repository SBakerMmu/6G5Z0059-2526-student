# Software Design and Architecture Week 02 Lab 02 Worksheet

## Create a Package
> ☑ Code written for the assignment should be written using packages and make proper use of the private, protected and public access declarations.

Use the Intelli-J File menu -> New > Project… to create a new sample project with a main method as before

### Create a new Package

In the src directory, create a new package. Call it `mypackage`

Right-click on the package and create a new class called Product within the package.

```java
package mypackage;
class Product {
    double discount = 0d;
    double fullPrice;
    double sellingPrice;

    Product(double fullPrice)
    {
        this(fullPrice, 0d);
    }
    Product(double fullPrice, double discount)
    {
        this.fullPrice = fullPrice;
        this.discount = discount;
        setSellingPrice();
    }
    void setDiscount(double discount) {
        //check pre- and post-conditions
        this.discount = discount;
        setSellingPrice();
    }
    void setFullPrice(double fullPrice) {
        //check pre- and post-conditions
        this.fullPrice = fullPrice;
        setSellingPrice();
    }
    double getSellingPrice() {
        return sellingPrice;
    }
    void setSellingPrice() {
        sellingPrice = fullPrice - discount;
    }
}

```

In the main method in the default package, create the following client code

```java
public class Main {
    public static void main(String[] args) {
        Product product = new Product(100.0d);
        product.setDiscount(20d);
        //Outputs Selling Price 80.000000
        System.out.format("Selling Price %f%n", product.getSellingPrice());
    }
}
```

Now add the access control declarations (public or private) to the Product class to make this code work. The declarations should provide the minimum amount of accessibility necessary to make the client code in the main() method work.

### Hints and Tips

When you create a class in Java you want to hide as much of its
implementation as possible. This is so that we can change the
implementation without breaking our client code because client code has
used a method or accessed a field we now want to change.

Java provides mechanisms for access control, to prevent the users of a
package or class from accessing unnecessary details of the
implementation of that package or class. If access is permitted, then
the accessed entity is said to be accessible (Oracle, 2024 Ch 6.6).

When you declare a new class in Java, you can indicate the level of
access permitted to its members (fields and methods). Java provides four
levels of **access specifiers**. Three of the levels must be explicitly
specified:

**private** meaning that the member or constructor is only accessible
within the declaring class.

**protected** meaning that the member or constructor is only accessible
to subclasses of the declaring class

**public** meaning that the member or constructor is publicly
accessible.

If no access specifier is used then the member is implicitly **package
private**.

We can summarise the effect of access specifiers on members and
constructors.

| Access Level          | Same Class | Class in Same Package | Subclass in Same Package | Class in Different Package | Subclass in Different Package |
|-----------------------|------------|-----------------------|--------------------------|----------------------------|-------------------------------|
| **public**            | Yes        | Yes                   | Yes                      | Yes                        | Yes                           |
| **protected**         | Yes        | Yes                   | Yes                      | No                         | Yes                           |
| **default (package)** | Yes        | Yes                   | Yes                      | No                         | No                            |
| **private**           | Yes        | No                    | No                       | No                         | No                            |

Experiment with the different access specifiers to understand their effects. The IDE or compiler will give you feedback on if a member or constructor is accessible.

## Encapsulation and Information Hiding

Most discussions about object orientation talk about **encapsulation**.

Encapsulation at the class level is the hiding of the implementation (fields, private methods) behind a public application programming interface (API). The internal implementation is not visible outside the class. The state of an object can only read or updated via the operations the public API.

A good design of a single class has a long-lived public application programming interface. The clients of that API do not need knowledge of or access to the internal fields and private methods, which means that the internal implementation can change providing the API still behaves as the client expects.

In Java, the concept of encapsulation scales up to the package. The public API offered by the package is the set of all public members of all public classes within the package, but the internal implementation of the package is hidden by using package private classes and package private or private members.

Encapsulation is one form of **information hiding** - a general principle in software engineering of creating any API (application programming interface) that hides design decisions and reveals as little as possible of the inner workings. Hiding implementation information in general allows us to work with something without having to know about all its internal details and it should be possible to refactor (change for the better) the implementation without a need for the client code to change. Hiding implementation details behind an interface is one of the fundamental ideas of software engineering that helps us cope with complexity and change.

## Implement Inherited Interfaces (Advanced)
Interfaces can be inherited. For example:
```java
interface InterfaceA {
    String someMethod();
}
interface InterfaceB extends InterfaceA {
    String someOtherMethod();
}
```

Write an implementation class that implements `InterfaceB`.

Classes can also implement multiple interfaces.

```java
interface MyInterface
{
    String someMethod(String paramName1, int paramName2);
}
interface MyOtherInterface
{
    String someOtherMethod(String paramName1, int paramName2);
}

```
Write an implementation class that implements BOTH `MyInterface` and `MyOtherInterface`.

### Hints and Tips

Use Intelli-J features to create the boilerplate code for you.

A simple output to the console should suffice as a way of demonstrating the implementation of a method.

```java
System.out.printf("Hello from %s%n", myImplementation.someMethod());
//or if you prefer System.out.format
System.out.format("Hello from %s%n", myImplementation.someOtherMethod());
```

See https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/util/Formatter.html#syntax for details on the %s and %n format specifiers in format strings

> ☑ Having a good knowledge of string formatting in Java saves a lot of time when creating complex strings.

## Experiment with Typed References (Advanced)

> ☑ This lab exercise is intended to test your understanding how Java interfaces and abstract classes work  - important because these are the language mechanics we need for writing flexible, extensible and maintainable code - one of the main goals of software design and software engineering.

Implement the following code in your test program and confirm that each reference can only “see” certain operations depending on the type. What do you observe?

```java
interface MyInterface {
    void interfaceMethodA();
}
```

```java
//D implements MyInterface
public class D implements MyInterface {
    @Override
    public void interfaceMethodA() {
        System.out.printf("Hello from D.interfaceMethodA%n");
    }

    public void methodA() {
        System.out.printf("Hello from D.methodA%n");
    }
}

```
```java
//E is a subtype of D and MyInterface
public class E extends D {
    @Override
    public void methodA() {
        System.out.printf("Hello from E.methodA%n");
    }

    public void methodB() {
        System.out.printf("Hello from E.methodB%n");
    }

    public void methodC() {
        System.out.printf("Hello from E.methodC%n");
    }
}

```
The code to use the implementations of D and E

```java
//Operations available to the variable of type MyInterface are just interfaceMethodA()
MyInterface myInterface = new E();
myInterface.interfaceMethodA(); //prints Hello from D.interfaceMethodA

//Operations available to the variable of type D are interfaceMethodA() and methodA()
D d = new E();
d.interfaceMethodA(); //Hello from D.interfaceMethodA
d.methodA(); //Hello from E.methodA

//Operations available to the variable of type E interfaceMethodA(), methodA() and methodB();
E e = new E();
e.interfaceMethodA(); //Hello from D.interfaceMethodA
e.methodA(); //Hello from E.methodA
e.methodB(); //Hello from E.methodB
```
### Hints and Tips
We have seen that when we are writing a Java class, methods can be either abstract (without an implementation) or concrete (with an implementation). If we are writing a Java interface, all its methods are abstract.

We can discuss design in terms of **operations**, without having to specify if the operation is abstract or concrete.

We can say that clients of an object request **operations**. Getting or setting field values, and invoking methods are all operations.

If the operation is abstract because it is defined as an abstract class method or within a Java interface, It's up to the Java runtime to map the requested operation to the correct method of a concrete class.

The type of the variable determines the set of operations available to the client. The set of operations available to the client are the public operations defined by the type and all its supertypes.
To put this in more theoretical terms,

- E is a subtype of MyInterface because MyInterface has a subset of the operations of E.

- E is a subtype of D because D has a subset of the operations of E.

- E is a subtype of itself because E has a subset of the operations of E (the same set of operations).

Therefore

- You can create and assign an instance of E to a variable of type MyInterface (MyInterface has a subset of the operations of E, E is a subtype of MyInterface).

- You can create and assign an instance of E to a variable of type D (D has a subset of the operations of E, E is a subtype of D).

- You can create and assign an instance of E to a variable of type E (E has a subset of the operations of E, E is a subtype of itself)

- You *can't* create and assign an instance of D to a variable of type E because E does not have a subset of the operations of D, and D is not a subtype of E.

In Java all reference types are subtypes of Object. This is why you can assign an instance of any Reference Type to Object because Object has a subset of the operations of any reference type.

> ☑ Understanding how Java interfaces and abstract classes work is important because these are the language mechanics we need for writing flexible, extensible and maintainable code - one of the main goals of software design and software engineering.

## Implement a Generic Interface (Advanced)

Implement the `Comparable<T>` interface for this immutable DiceRoll class

```java
public final class DiceRoll {
    private final int value;

    public DiceRoll(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return String.format("DiceRoll(%d)", value);
    }
}

```
### Hints and Tips

You need to
- implement the equals() and hashCode() methods
- implement the Comparable<DiceRoll> interface.

```java
public class DiceRoll implements Comparable<DiceRoll> {
//your implementation
}
```


Also create a class that implements the Comparator<T> interface for the DiceRoll class.
```java

public class DiceRollComparator implements Comparator<DiceRoll> {
//your implementation
}
``````

Why does Java have both Comparable<T> and Comparator<T> interfaces?

## Understand Command Operations and Query Operations (Advanced)

Operations can be **commands** (an operation which changes the values of one or more instance variables, the state of the object) or **queries** (an operation that returns a value but does not change the state of an object).

Whilst it may seem an academic distinction to make, a true query does not have **side effects** because it doesn't change the state of the object - sometimes referred to as a **read only** operation, which means that a client can call a query knowing it won't change the state of the object. Knowing that an operation is read only is actually very useful, because you know you can call the operation as many times as you like, and it will not change the state of the object.

In Java is there is no language mechanism to say if an operation is a command or a query. By convention in Java the method `int getX()` method would return the value of the field x as an integer. Anyone calling that method would be very surprised if `int getX()` had changed the object state. You should follow that convention.

⚠ Be aware that you could call the same query twice in different parts of the program and there is no guarantee it would get the same value returned, because a command (a state changing operation) could have been requested between the two query operations.

☑ Where possible write your code so that operations that return values are **queries** and all **command** (state altering) operations are written as void methods. If you do write a command (state altering method) that returns a value, ensure you document that it is state altering.
