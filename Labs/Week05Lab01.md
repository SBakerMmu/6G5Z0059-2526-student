# Software Design and Architecture Week05 Lab 01 Worksheet

There are multiple activities each week, and you will probably not get everything done in the timetabled lab sessions; therefore, it is highly recommended that you complete the labs in your own time each week to avoid falling behind.

Completing the labs will get you ready for writing the assignment code.

**Advanced** Labs are optional, but completing the Advanced Labs will introduce you to more advanced techniques and improve your design skills.

Week 5 Labs show how to add functionality to closed classes using Decorators and Observers.

# Use Decorators to implement different Dice Shakers

Create a new Java project using:

Intelli-J File menu -\> New \> Project. Provide a project name, chose a location and ensure that you have ticked the **Add sample code** box.

Previously we implemented a DiceShaker using an interface and an implementation that shook a single 6-sided die.

Add a DiceShaker interface to your project

```java
interface DiceShaker {
    int shake();
}
```

Create an implementation that shakes a single die.

```java
class RandomSingleDiceShaker implements DiceShaker{
    private final Random random = new Random();
    @Override
    public int shake() {
        //generate number between 1 and 6
        return random.nextInt(6) + 1;
    }
}
```

Write a test program to test the implementation.

```java
public static void main(String[] args)
{
    DiceShaker shaker = new RandomSingleDiceShaker();
    System.out.format("Shake %d%n", shaker.shake());
}
```
Now, using the Decorator pattern, write a Decorator that you can add in front of the RandomSingleDiceShaker to simulate a shaker with 2 six-sided dice. You should be able to chain your decorators together shake any number of six-sided dice.

Write a test program that demonstrates your chain of decorators.

Create an Abstract Factory (see previous lab) to create DiceShakers with different number of dice.

The DiceShakerFactory interface can be defined as:

```java
interface DiceShakerFactory {
    DiceShaker create();
}
```

Create concrete factories that will create a combination of the RandomSingleDiceShaker class and decorators to produce:

  - Random Single Dice Shaker (throws a single six-sided die)

  - Random Double Dice Shaker (throws two six-sided dice)

  - Random Triple Dice Shaker (throws three six-sided dice)

Hint: You may find you can reuse the factories too by aggregation.

Write a test program to demonstrate your factories using a method such as

```java
private static void show(DiceShakerFactory factory)
{
    DiceShaker shaker = factory.create();
    System.out.format("%d%n", shaker.shake());
    System.out.format("%d%n", shaker.shake());
    System.out.format("%d%n", shaker.shake());
}

```

Writing the code to output to the screen is repetitive. Write an additional decorator for the DiceShaker interface that writes the value of a shake to the console (using `System.out`). Add this to the show() test method to eliminate the repetitive code.

```java
System.out.format("%d%n", shaker.shake());
```

## Hints and Tips

The **Decorator** pattern allows you to "decorate" a class with additional features, creating a customized version without modifying the original code.

The original concrete class must implement an interface, and the decorator shares the same interface.

Each decorator object wraps around a concrete object or another decorator, implementing the same interface. The decorator delegates operations to the wrapped object and then adds its own behaviour before or after the delegation.

The Decorator pattern is sometimes called the **Wrapper** pattern because the Decorator pattern wraps the underlying interface.

Decorators follow the **Open Closed** principle-the original class is closed (not modified) but as long as it implements an interface, it is open for extension by a decorator intercepting any calls made to the interface.

Decorators also enable us to follow the **Single Responsibility** Principle. Each decorator focuses on a specific responsibility

Decorators provide a form of **subclassing**. In Java subclassing the subclass extends the super class and can add or replace functionality provided by the superclass. Using conventional subclassing the functionality has to be decided at compile time. With decorators, the functionality can be decided dynamically at runtime by adding one or more decorators.

To implement the Decorator pattern, first define the interface for the component that needs decorating

```java
 interface Component {
    void operation();
}
```


Then implement one or more concrete components that implement the Component interface and supply some original component functionality.

```java
public class ConcreteComponent implements Component {
    @Override
    public void operation() {
        //Original functionality here
    }
}
```

Create one or more concrete Decorators that also implement the Component interface. Decorators can add functionality before the call or after the call to the inner component. As the Decorator aggregates a Component interface, then one concrete Decorator could be calling another concrete Decorator(s) or the original Concrete Component.

```java
class ConcreteDecorator implements Component{
    private final Component component;

    public ConcreteDecorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        //Decorator can add functionality before the call to the inner component
        //including changing any parameters
        component.operation();
        //Decorator can add functionality after the call to the inner component
        //including changing the return from the inner component
    }
}

```
The client code is unaware if they are referencing the original component or a decorator.

```java
//Not the reference to the interface type, not the concrete type
Component component = new ConcreteComponent();
component  = new ConcreteDecorator(component);
component.operation();
```
