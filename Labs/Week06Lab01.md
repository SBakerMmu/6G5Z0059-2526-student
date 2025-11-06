# Software Design and Architecture Week06 Lab 01 Worksheet

Week 6 Labs show how to work with multiple classes using Facades and Mediators.

There are multiple activities each week and you will probably not get everything done in the timetabled lab sessions; therefore, it is highly recommended that you start on the lab work ahead of time and use the timetabled sessions for support. Compete the labs in your own time each week to avoid falling behind.

# Create a Stateless Façade to implement a Dice Game

Create a new Java project using:

Intelli-J `File menu -> New -> Project`. Provide a project name, chose a location and ensure that you have ticked the **Add sample code** box.

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
We previously implemented a simple game board with the following positions:

```Text
       1 [HOME]
[END]6    2
     5    3
       4
```

A game piece starts at position 1 (the HOME position) and advances clockwise based on the throw of a 6-sided die.

For example, a throw of 4 will move the piece from position 1 to 5.

A subsequent throw of 3 with move the piece from position 5 to position 2.

One simple implementation of the game board is

```java

class GameBoard {

    private static final int HOME = 0;
    private static final int END = 5;
    private static final int LENGTH = END - HOME + 1;
    private int index;

    GameBoard() {
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
We calculate the new index using the mod (%) operator, which returns a remainder.

The lab task is to write Façade classes that integrates the Game and the SingleDiceShaker implements the following interface.

```java
interface StatelessFacade {
  int play();
}
```
In this example, the game is ‘won’ by returning the game piece to exactly the HOME position. Overshooting the home position is not a win – keep advancing until the game piece lands exactly on the HOME position

The `play()` method should return the number of ‘shakes’ of a DiceShaker taken to ‘win’.

The lab task is to write a **stateless** façade that implements the StatelessFacade interface.


# Create a Stateful Façade to implement a Dice Game

We could define a different interface that separates out the command and query operations.

```java
interface StatefulFacade {
    void play();
    int getShakes();
}
```
The play method runs the game until it is ‘won’. The getShakes() method returns the number of ‘shakes’ it took to ‘win’.

Write a **stateful** façade that implements the StatefulFacade interface.

## Prompt Questions

Some prompt questions for you to think about

Q) Which class should be responsible for deciding if the game is ‘won’ or more ‘shakes’ are required ?

Q) Which class is responsible for counting the number of moves

Q) What behaviour would you expect if you call the play() method twice on the same façade instance?

## Hints and Tips

As a Façade is a normal class, it can hold state in private fields (instance variables) and use these fields to hold information about past calls or which modifies the behaviour of future calls. If it holds information in private fields it is **stateful**… it holds **state**. With a stateful façade the order that methods are called may be significant, which could mean that the client has to have knowledge about how to use the façade.

Stateless facades do not hold any state in private fields and as such any method can be called at any time and in any order. We might describe as Stateless facades as being a **Service**.
