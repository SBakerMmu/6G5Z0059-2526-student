# Software Design and Architecture Week05 Lab 02 Worksheet
# Use Observers to observe Game Events
A simple game board has the following positions

```Text
       1 [HOME]
[END]6    2
     5    3
       4
```

A game piece starts at position 1 (the HOME position) and advances clockwise based on the throw of a 6-sided die.

For example, a throw of 4 will move the piece from position 1 to 5. This is an “underflow” because the piece has not gone past the HOME position.

A subsequent throw of 3 with move the piece from position 5 to position 2. This is an “overflow” because the piece has gone past the END position.

The interface that describes the operations of the Game board

```java
interface GameBoard {
    void advance(int count);
}
```
One implementation of the game (without an observer):

```java
class NonObservedGameBoard implements GameBoard {

    private static final int HOME = 0;
    private static final int END = 5;
    private static final int LENGTH = END - HOME + 1;
    private int index;

    NonObservedGameBoard() {
        setIndex(HOME);
    }

    private void setIndex(int newIndex) {
        if (newIndex >= HOME && newIndex <= END) {
            index = newIndex;
        } else {
            throw new IndexOutOfBoundsException(newIndex);
        }
    }

    public int getCurrentPosition() {
        return index + 1;
    }

    @Override
    public void advance(int count) {
        int newIndex = index + count;
        setIndex(newIndex % LENGTH);
    }
}

```
We calculate the new index using the mod (`%`) operator, which returns a remainder.

We want to output both overflow (new position is \> 6) and underflow (new position \<= 6) events to the console, but do not want to put that code into the Game (it would mix up responsibilities).

The two events we want to expose are Overflow Event (when the new position will be \> 6) and an Underflow Event (when the new position will be \<=6). This is a good example of using class inheritance to hold the common code between the Overflow and Underflow events.

```java
abstract class PositionChangeEvent {
  private final int advance;
  private final int oldPosition;
  private final int newPosition;

  PositionChangeEvent(int advance, int oldPosition, int newPosition) {
    this.advance = advance;
    this.oldPosition = oldPosition;
    this.newPosition = newPosition;
  }

  public int getAdvance() {  return advance; }

  public int oldPosition() {
    return oldPosition;
  }

  public int newPosition() {
    return newPosition;
  }

  @Override
  public String toString() {
    return String.format("{advance %d oldPosition=%d, newPosition=%d}", advance, oldPosition, newPosition);
  }
}
```

The Underflow and Overflow events then just inherit from the superclass, and provide their own `toString()` methods.

```java
final class OverflowEvent extends PositionChangeEvent {

  OverflowEvent(int advance,int oldPosition, int newPosition) {
    super(advance, oldPosition, newPosition);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OverflowEvent");
    sb.append(super.toString());
    return sb.toString();
  }
}
final class UnderflowEvent extends PositionChangeEvent {

    UnderflowEvent(int advance, int oldPosition, int newPosition) {

        super(advance, oldPosition, newPosition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnderflowEvent");
        sb.append(super.toString());
        return sb.toString();
    }
}

```

Make the GameBoard class observable using the **Observer** pattern. Write a concrete observer so that the Overflow and Underflow events are reported to the console. This example should generate the following output.

```text


advance(1);
advance(1);
advance(1);
advance(1);
advance(1);
advance(1);
advance(3);
advance(3);
advance(2);
advance(3);
advance(5);
advance(9);
```

Expected output to the console (generated from the observer).
```Text
UnderflowEvent{advance 1 oldPosition=1, newPosition=2}
UnderflowEvent{advance 1 oldPosition=2, newPosition=3}
UnderflowEvent{advance 1 oldPosition=3, newPosition=4}
UnderflowEvent{advance 1 oldPosition=4, newPosition=5}
UnderflowEvent{advance 1 oldPosition=5, newPosition=6}
OverflowEvent{advance 1 oldPosition=6, newPosition=1}
UnderflowEvent{advance 3 oldPosition=1, newPosition=4}
OverflowEvent{advance 3 oldPosition=4, newPosition=1}
UnderflowEvent{advance 2 oldPosition=1, newPosition=3}
UnderflowEvent{advance 3 oldPosition=3, newPosition=6}
OverflowEvent{advance 5 oldPosition=6, newPosition=5}
OverflowEvent{advance 9 oldPosition=5, newPosition=2}

```

Once you have that working, create a HomeEvent event that is passed to observers whenever the game position returns to exactly the HOME position (i.e. when the game lands on the HOME position = 1).

## Hints and Tips

When applying the Observer pattern, we are in effect allowing the class to describe significant events that have occurred to it. We have an extensible mechanism whereby other code can subscribe to and execute their own logic in response to these events. The class and the Observers that handle the event are decoupled. The Observers know about the class, but the class doesn't know about the Observers. Furthermore, the number and behaviour of event handlers can be varied at runtime by attaching (subscribing) different Observers to an object.

Adding Observers to a class is a good way of making it closed for modification and open for extensibility. You only need to change the source code if you need to add new events.

There are lots of different ways of implementing the pattern, but here is a general version.
Start with the Observer interface.

```java
class Payload
{
}

interface Observer {
    void update(Payload payload);
}
```

The Observable class has a list of \`Observers\`.

```java
class Observable  {
    final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void update() {
        Payload payload = new Payload();
        for (Observer observer : observers) {
            observer.update(payload);
        }
    }
}
```
> ☑ The Observer pattern is very common in GUI frameworks. The Observable object might be something like a Button. When a user clicks the Button it coverts the mouse actions into events, say OnButtonDown and OnButtonUp. The Button implements the Observer pattern. Observers (which might be called **Event Handlers**, **Event Listeners** or **Callbacks**) register themselves with the Button instance and are notified of all the Button Events. This way the GUI Framework can exist without any knowledge of how your program will react to these Button events. A very good illustration of being closed for modification but open for extension.

# Do and Undo using Commands (Advanced)

The supporting material for this lab is the **Undoing operations using Commands** chapter of the online textbook

Record a die role using a Dice Shaker being presented into a **GameBoard** (from the previous lab) and then undo it. Undo means that the position in the game is restored to the original value before the position was advanced.

For example, given the Game starts in the HOME position = 1, then the sequence of positions would be

```Text

Advance 4, current position = 5

Advance 3, current position = 2

Undo, current position = 5

Undo, current position = 1.

```

## Hints and Tips

We have categorised operations as being either **Commands** (an operation which changes the state of the object) or **Queries** (an operation that returns a value but does not change the object).

There are circumstances where we want to **undo** the effects of a Command operation (as Queries make no changes, there is no need to undo a Query). Application such as Text Editors or Graphics programmes support an **Undo** feature that allow you to undo multiple commands, possibly all the way back to the last time you saved a file.

Imagine we have a class with a method that updates a single integer value. We instantiate the class to create an object, and then request an operation to add to that value.

```java
class MyClass {
    private int value = 0;
    void add(int i) {
        //some implementation
        value += i;
    }
    public int getValue() {
        return value;
    }
}
```

Client Code

```java
MyClass anInstanceOfMyClass = new MyClass();
anInstanceOfMyClass.add(2);
```

There is another way for the client code to request an operation from a supplier, which is to wrap the call in a **Command** object. This object captures the argument(s) provided to the operation, so that it can also undo the operation.

As always, we start with an interface.

```java
interface Command {
 void execute();
 void undo();
}
```
We now implement the `add()` operation my MyClass as a Command with do (execute) and undo methods.

```java
class AddCommand implements Command {
    private final MyClass receiver;
    private final int i;
    AddCommand(MyClass instanceOfMyClass, int i) {
        this.receiver = instanceOfMyClass;
        this.i = i;
    }
    @Override
    public void execute() {
        receiver.add(i);
    }

    @Override
    public void undo() {
        receiver.add(-i);
    }
}
```

Client code uses the command object thus:

//Client Code
```java

MyClass anInstanceOfMyClass = new MyClass();
Command command = new AddCommand(anInstanceOfMyClass, 2);
System.out.printf("value %d%n",anInstanceOfMyClass.getValue());
command.execute();
System.out.printf("value %d%n",anInstanceOfMyClass.getValue());
command.undo();
System.out.printf("value %d%n",anInstanceOfMyClass.getValue());

//Output
// value 0
// value 2
// value 0
```
You will need to change the interface of the Game class too – it is originally equipped with an advance method, but to implement the undo, you will need a way to set the game to a specific position.
