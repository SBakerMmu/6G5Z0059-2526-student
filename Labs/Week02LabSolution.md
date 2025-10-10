# Software Design and Architecture Week 2 Lab Solutions
Suggested answers to the Week 2 Lab Exercises

## Implement an Interface and an Abstract Class

We asked you to create various dice shakers implementing the dice shaker interface.

```java

public interface DiceShaker {
   int shake();
}
```
Create an implementation that shakes a single die.

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
Create an implementation that shakes a two dice.
```java


public class RandomDoubleDiceShaker implements DiceShaker {
  private final Random random = new Random();

  @Override
  public int shake() {
    //generate number between 2 and 12
    return random.nextInt(6) + random.nextInt(6) + 2;
  }
}
```

Create an implementation that returns a fixed set of “shakes”.

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
We asked you to switch between different shakers based on a user input. This is a working solution, but it is not using the power of changing the implementation at runtime.

```java

private static void diceUseInterface (int option) {
  //In this example we are creating concrete objects and assigning them to
  //variables of a concrete type, not using an abstract type and repeating the client code
  if (option == 1) {
    RandomSingleDiceShaker shaker = new RandomSingleDiceShaker();
    System.out.format("Shake %d%n", shaker.shake());
  } else if (option == 2)
  {
    RandomDoubleDiceShaker shaker = new RandomDoubleDiceShaker();
    System.out.format("Shake %d%n", shaker.shake());
  } else {
    //bad stuff happens
  }
}
```
The correct way.

````java

private static void diceUseInterfaceWell(int option) {

  //In this example we are declaring a variable of an abstract type
  //and deciding at runtime which concrete implementation we will use
  DiceShaker shaker = null;

  if(option == 1) {
    shaker = new RandomSingleDiceShaker();
  } else if (option == 2)
  {
    shaker = new RandomDoubleDiceShaker();
  } else {
    //bad stuff happens
  }

   //There is a single line of client code
   //and have separated the code for selecting the implementation from the code that uses it
  System.out.format("Shake %d%n", shaker.shake());
}
````

You can also create DiceShakers using abstract classes and abstract methods.

Create an abstract superclass.

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
Create concrete implementations for 1 and 2 die (as before).

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
The last task was “Now implement a version that provides a fixed set of ‘shakes’.“ using the abstract class.

You might have implemented this:

```java

public class FixedSingleDiceShaker extends AbstractDiceShaker {

  private final int[] shakes = new int[]{
      1, 2, 3, 4, 5, 6
  };

  private int index;

  @Override
  public int shake() {
    //reset
    if (index == shakes.length) {
      index = 0;
    }
    return shakes[index++];
  }
}
```
> ☠ This is bad design – although it inherits from AbstractDiceShaker it makes no use of the random number generator or the shakeSingleDie() method.

The learning point is that an interface defines a “can-do” capability independently of implementation, whereas an inheritance models an ‘is-an’ relationship, and in the Dice Shaker case, the fixed share most definitely is not an ‘is -a’ random shaker. This becomes apparent when you try an implement a subclass of AbstractDiceShaker that isn’t random.

True inheritance relationships are less common than you would think, which is one reason why we prefer to use interfaces as abstract types rather than abstract base classes.

It doesn’t mean that you can’t use inheritance, just it’s better to use inheritance to share implementation, rather than use a base class as the abstract type.

Here is a better design using an interface to represent the capability, and two classes happen to use inheritance to share some implementation because they are true specialisations.

```java

public interface DiceShaker {
   int shake();
}

```

```java
public abstract class AbstractDiceShaker implements DiceShaker
{
   private final Random random = new Random();
   protected int shakeSingleDie() {
    //generate number between 1 and 6
    return random.nextInt(6) + 1;
  }
   public abstract int shake();
}

```
Implement the concrete random shakers using inheritance to use the `shakeSingleDie()` method

```java

public class ConcreteSingleDiceShaker extends AbstractDiceShaker {
    @Override
    public int shake() {
        //generate number between 1 and 6
        return shakeSingleDie();
    }
}
```

```java
public class ConcreteDoubleDiceShaker extends AbstractDiceShaker {
    @Override
    public int shake() {
      //generate number between 2 and 12
      return shakeSingleDie() + shakeSingleDie();
    }
}
```

We can have a completely independent implementation which does not need the Random number generator

```java
public class FixedSingleDiceShaker implements DiceShaker {
  private final int[] shakes = new int[]{
      1, 2, 3, 4, 5, 6
  };
  private int index;

  @Override
  public int shake() {
    //reset
    if (index == shakes.length) {
      index = 0;
    }
    return shakes[index++];
  }
}
```
The client code doesn’t care if the concrete class is implemented using inheritance or not

```java
DiceShaker shaker = new ConcreteSingleDiceShaker();
shaker = new FixedSingleDiceShaker();
```

# Testing for Equality using the instanceof operator and the getClass() method (Advanced)

We what is the difference between these two approaches and why would you choose one over the other?

Using `getClass()`

```java
class A {
    final int a;

    public A(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        A other = (A) o;
        return a == other.a;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a);
   }
}
```
Using `instanceof`
```java
class A {
    final int a;

    public A(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof A other)) return false;
        return a == other.a;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a);
    }
}
```

When you use getClass() you are saying that two objects are only equal if they are of the exact same class. This means that if you have a subclass of A, instances of that subclass will not be considered equal to instances of A, even if they have the same value for the field a.

When you use instanceof, you are saying that two objects can be considered equal if one is an instance of the class A or any of its subclasses. This means that if you have a subclass of A, instances of that subclass can be considered equal to instances of A, as long as they have the same value for the field a.

```java
public class B extends  A{
    public B(int a) {
        super(a);
    }
}
```
If we subclass A then we get different behaviour
```java
A a = new A(1);
B b = new B(1);
//using instanceof
System.out.println(a.equals(b)); // true
//using getClass()
System.out.println(a.equals(b)); // false
```
When to choose one over the other depends on your design (there is no simple one approach is better than the other answer).

- If you want strict equality based on the exact class, use getClass().
- If you want to test logical equality (based on the state of the object) and you want to allow subclasses to be considered equal, use instanceof.

`getClass() != o.getClass()` must be returning the same instance of something as this a reference equality test. `getClass` returns an instance of the `Class` object based the runtime object.
