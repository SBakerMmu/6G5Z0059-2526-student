# Software Design and Architecture Week 02 Lab 01 Worksheet

Week 2 labs are about some key object-oriented techniques – interface implementation and abstract classes.

If you complete the labs, use the time to work on your assignment code and get formative feedback from the tutors.

## Implement an Interface
IntelliJ has a quick and simple way of creating a new Java project that we can use for many of the labs.
Intelli-J File menu -> `New > Project…`

Provide a project name, chose a location and ensure that you have ticked the Add sample code box.

> ⚠ Some settings will be probably different on your installation, the JDK version should be the highest available on your machine

This should generate a runnable program.
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

which when run outputs
```java
Hello, World!
Process finished with exit code 0
```
We are going to implement a simulation of a dice shaker.
![A Diceshaker](images/diceshaker.png)

Add a DiceShaker interface to your project
```java
public interface DiceShaker {
   int shake();
}
```

### Create an implementation that shakes a single die.
```java
public class RandomSingleDiceShaker implements DiceShaker {
  private final Random random = new Random();

  @Override
  public int shake() {
    //generate number between 1 and 6
    return random.nextInt(6) + 1;
  }
}
```
Test the implementation (note the type of the reference is the interface type, not the implementation type).

```java
public static void main(String[] args)
{
    DiceShaker shaker = new RandomSingleDiceShaker();
    System.out.format("Shake %d%n", shaker.shake());
}
```

### Create an implementation of the DiceShaker interface that shakes 2 dice.

Write some code to switch between the two implementations based on some user input into the main() method.
> ⚠ The Scanner class in the java.util is often used as a simple way of scanning user input from the keyboard

### Create an implementation of the DiceShaker interface that delivers a fixed set of shakes.

For the assignment code you will find it useful to implement a version of a DiceShaker that returns a fixed set of “shakes”.

```java
public class FixedSingleDiceShaker implements DiceShaker {

  private final int[] shakes = new int[]{
      1, 2, 3, 4, 5, 6
  };
  private int index;
@Override
  public int shake() {
    return shakes[index++];
  }
}
```

## Implement using Abstract Classes and Abstract Methods

### Create an abstract superclass.
```java
public abstract class AbstractDiceShaker {
    private final Random random = new Random();
    protected int shakeSingleDie() {
        //generate number between 1 and 6
        return random.nextInt(6) + 1;
    }
    public abstract int shake();
}
```
### Create concrete implementations for 1 and 2 die (as before).
```java
public class ConcreteSingleDiceShaker extends AbstractDiceShaker {
    @Override
    public int shake() {
        //generate number between 1 and 6
        return shakeSingleDie();
    }
}

public class ConcreteDoubleDiceShaker extends AbstractDiceShaker {
    @Override
    public int shake() {
        //generate number between 2 and 12
        return shakeSingleDie() + shakeSingleDie();
    }
}
```

Test your implementation in the main method (as before), note the type of the reference is the abstract class type, not the implementation type.
```java
AbstractDiceShaker abstractShaker = new ConcreteSingleDiceShaker();
System.out.format("Shake %d%n", abstractShaker.shake());
```

Now implement a version that provides a fixed set of ‘shakes’.

**Question: What are the differences between implementing an interface and extending an abstract class.**
Which one would you choose?

### Hints and Tips

An **abstract** class is a class that is incomplete (abstract means it is lacking one or more fields, or implementation of one or more methods) and requires a subclass to extend the abstract class to implement the missing methods.

This is why you can declare abstract classes, but not actually instantiate one - because an abstract has an incomplete implementation.

A method declaration lacking an implementation is abstract. In Java, you can declare methods abstract to force subclasses to implement methods with a specific signature. Abstract methods just consist of their method specification (name, parameter types and return types).

A class with a complete implementation is called a **concrete** class. You can only **instantiate** (create objects) concrete classes.

An interface is an abstract type (abstract meaning that it is lacking implementation of one or more methods).

Interfaces are abstract because they do not have fields (only constants allowed) and declare one or more abstract methods (methods without an implementation body). Note that the abstract keyword is unnecessary because interfaces are *implicitly* abstract.

Being abstract, interfaces cannot be instantiated. Instead, the methods of the interface must be **implemented** by a class using the implements keyword. A class must provide the complete set of methods required by the interface, but each class can have its own implementation. This makes an interface a **specification** to be fulfilled by classes that implement the interface.

Interfaces are key to writing extensible and flexible code because:

- one interface can be implemented by many different and unrelated classes, each of which has a unique implementation of the interface specification.

- the choice of which concrete type supplies the interface implementation can be made at runtime, allowing us to vary behaviour based on some runtime condition (such as user input, choice of operating system, production or test mode...)


