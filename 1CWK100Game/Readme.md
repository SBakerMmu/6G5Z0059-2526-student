# 1CWK100 Game Hints

Some hints if you have encountered difficulties with the logic of tracking the positions of Red and Blue players, particularly the Blue player as they start on the board at position 10 and their position resets from 18 to 1 halfway around the board.

**If you have already solved this and your code is working, DO NOT feel you have to change your code - this is intended to guide students who haven’t got a basic version working by providing the easiest possible implementation, and we’re not looking for all students to implement it this way.**

There are many ways of managing this, but one of the simplest coding strategies is to map an index for each player to a position on the board. Each player tracks their position as a simple increasing number, but that number is mapped to different board positions depending on the player.

First, create an array of position numbers, and then use a zero based index into that array (Java arrays are zero based, in that the index of the first element of the array = 0).

Red and Blue track their progress using a zero based index. For the small board each player has an index which goes from 0 to 20 but the index is **mapped** to a position number representing a position on the board.

Red's array of position numbers looks like

```java
    private final static int[] POSITIONS = new int[]{
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3
};
```
Red's starting position is index = 0, which when applied to the array, return board position 1. If Red advanced 2 the index = 2, which maps to board position 3.
The last three  values in the array represent the three positions in the tail: R1,R2,R3.

We can use the same method for Blue. Its POSITIONS array has different values

```java
    private static final int[] POSITIONS = new int[]{
        10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3
    };
```

Blue's starting position is index = 0, which when applied to the array, return board position 10. If Blue advanced 2 the index = 2, which maps to board position 12.
The last three position values in the array represent the three positions in the tail: B1,B2,B3.

For the small board RED and BLUE both have 21 possible positions including the 3 in the tail. We can therefore tell if we are in the body of the main board or in the tail by using the comparing the current index with the index of the first position in the tail, and we can tell if we are on the last position because the index of the last position = 20.

For example
```java
class Red {
    private final static int START_OF_TAIL_INDEX = 18;
    private final static int END_OF_TAIL_INDEX = 20;
    private final static int[] POSITIONS = new int[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3
    };

    private int currentIndex = 0;


    public boolean isInBody() {
        return !isInTail();
    }

    public boolean isHome() {
        return currentIndex == 0;
    }

    public boolean isInTail() {
        return currentIndex >= START_OF_TAIL_INDEX;
    }

    public boolean isAtEnd() {
        return currentIndex == END_OF_TAIL_INDEX;
    }

    public int getPosition() {
        return POSITIONS[currentIndex];
    }

    public String getPositionAsString() {
        if (isHome())
            return String.format("HOME (Position %d)", getPosition());
        if (isAtEnd())
            return String.format("END (Tail Position %d)", getPosition());
        if (isInTail())
            return String.format("TAIL (Tail Position %d)", getPosition());
        else
            return String.format("Position %d", getPosition());

    }

    public void advance(int positions) {
        currentIndex = currentIndex + positions;
        if (currentIndex > END_OF_TAIL_INDEX) {
            currentIndex = END_OF_TAIL_INDEX;
        }
    }
}

```
The `isHome()` method simply checks if the current index is 0 (i.e. the first element of the array).

The `isInTail()` method checks if the current index is greater or equal to 18 (for Red) which is within the last 3 elements of the array.

The `isAtEnd()` method checks if the current index is the last (end) element of the array

The `advance(int positions)` method increments the current index, but if this would make the index after the end of the array (an overshoot), it sets the index to the end of the array instead

The implementation of the Blue class is identical, apart from the values inside the array, which represent the position numbers that the Blue player would follow.

```java

class Blue {

    private final static int START_OF_TAIL_INDEX = 18;
    private final static int END_OF_TAIL_INDEX = 20;

    private static final int[] POSITIONS = new int[]{
            10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3
    };

    private int currentIndex = 0;

    public boolean isInBody() {
        return !isInTail();
    }

    public boolean isHome() {
        return currentIndex == 0;
    }

    public boolean isInTail() {
        return currentIndex >= START_OF_TAIL_INDEX;
    }

    public boolean isAtEnd() {
        return currentIndex == END_OF_TAIL_INDEX;
    }

    public int getPosition() {
        return POSITIONS[currentIndex];
    }

    public String getPositionAsString() {
        if (isHome())
            return String.format("HOME (Position %d)", getPosition());
        if (isAtEnd())
            return String.format("END (Tail Position %d)", getPosition());
        if (isInTail())
            return String.format("TAIL (Tail Position %d)", getPosition());
        else
            return String.format("Position %d", getPosition());

    }

    public void advance(int positions) {
        currentIndex = currentIndex + positions;
        if (currentIndex > END_OF_TAIL_INDEX) {
            currentIndex = END_OF_TAIL_INDEX;
        }
    }

}

```

## The main program

Having created Red and Blue players, we need the main method to run the game

The game sits in a loop, alternating between red and blue players until one of them reaches the end position.

```Java
 public static void main(String[] args) {

    Red red = new Red();
    Blue blue = new Blue();
    final int[] shakes = new int[]{12, 12, 7, 8};
    int turns = 0;
    int redTurns = 0;
    int blueTurns = 0;

    System.out.format("Red:%s Blue:%s %n", red.getPositionAsString(), blue.getPositionAsString());
    while (!red.isAtEnd() & !blue.isAtEnd()) {
        int shake = shakes[turns++];
        if (turns % 2 == 1) {
            redTurns++;
            System.out.format("Red play %d rolls %d%n", redTurns, shake);
            String start = red.getPositionAsString();
            red.advance(shake);
            System.out.format("Red moves from %s to %s%n", start, red.getPositionAsString());
        } else {
            blueTurns++;
            System.out.format("Blue play %d rolls %d%n", blueTurns, shake);
            String start = blue.getPositionAsString();
            blue.advance(shake);
            System.out.format("Blue moves from %s to %s%n", start, blue.getPositionAsString());
        }
    }
    if (red.isAtEnd()) {
        System.out.format("Red wins in %d moves%n", redTurns);
    } else {
        System.out.format("BLue wins in %d moves%n", blueTurns);
    }
    System.out.format("Total plays %d%n", turns);

}
```

The starter code above should help you get the basic game working, but the code is *terrible*.
There are many things that could be improved in the code above to make a much more object-oriented solution (and hence improve the marks for code quality) but this should be enough to get you going if you were stuck.

## Implementing Variations

To implement the varations, we recommend you study the strategy pattern.

For example, the implementation of the `advance` method in Blue and Red limits the index so that it does not go past the end of the array.

```Java
  public void advance(int positions) {
        currentIndex = currentIndex + positions;
        if (currentIndex > END_OF_TAIL_INDEX) {
            currentIndex = END_OF_TAIL_INDEX;
        }
    }
```

You can replace that with a strategy that calculates the currentIndex, and provide two variations, one of which contains the original code, the other makes the player go backwards on overshoot.

An example interface

```Java
interface IndexStrategy {
    int calculateIndex(int currentIndex, int positions, int endOfTailIndex);
}
```

The implementation that simply sets the index to the end

```Java
class DoesNotNeedToLandOnEndToWin implements IndexStrategy {
    @Override
    public int calculateIndex(int currentIndex, int positions, int endOfTailIndex) {
        currentIndex = currentIndex + positions;
        if (currentIndex > endOfTailIndex) {
            currentIndex = endOfTailIndex;
        }
        return currentIndex;
    }
}
```

Now the `advance` method uses the strategy instead of having the logic hard coded, and by providing different strategy implementations, you can vary the outcome.

```java

  public void advance(int positions, IndexStrategy strategy) {
        currentIndex =  strategy.calculateIndex(currentIndex, positions, END_OF_TAIL_INDEX);
    }

```










