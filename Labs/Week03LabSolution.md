# Software Design and Architecture Week 3 Lab Solutions

Suggested answers to the Week 3 Lab Exercises

## Implement a Strategy Pattern for Calculating Shipping Costs

We provided you with some starter code of a Product class and a Basket class, representing a basket in an ecommerce checkout process.

The lab task was to replace the Destination enum parameter with a Strategy Pattern, implementing 3 concrete strategies representing UK Shipping, Europe Shipping and Rest of World Shipping.

The code to calculate the shipping charge is in the Basket class and is based on a Destination enum.

```java


enum Destination {
    UK,
    Europe,
    RestOfWorld
}
```
```java
import java.util.ArrayList;
import java.util.List;

class Basket {
    private final List<Product> products = new ArrayList<>();
    private final Destination shipTo;


    public Basket(Destination shipTo) {
        this.shipTo = shipTo;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    private double totalWeight()
    {
        double weight = 0.0d;
        for(Product product: products)
        {
            weight += product.getWeight();
        }
        return weight;
    }

    public double getShippingCharge()
    {
        return switch( shipTo) {
            case UK -> 0.0d; //Free Shipping in UK
            case Europe -> totalWeight() * 1.25; //£1.25 per Kg
            default -> switch (products.size()) //Rest of the World
                {
                    case 0 -> 0.0d;
                    default -> Math.max(10.00d, totalWeight() * 5.50); // higher of £10.00 or 5.50 per Kg
                };
        };
    }

}
```

The Basket is created with a Destination and calculates the shipping charge in the getShippingCharge() method. There are currently 3 different destination based algorithms for calculating shipping.

> An **algorithm** is the term given to any sequence of steps to solve a problem, make a decision or calculate a value.

It might look perfectly sensible, but there are several design issues with the code that could cause maintenance issues later.
1)	All the algorithm code to calculate shipping is in the Basket class, which means that if any part of any calculation needs to change, I must change the Basket class, which is wrong because the role of the Basket class is to hold and manage a list of products.
2)	The only thing that I can use to vary the shipping charge is the destination – there is nothing in the Basket class for example that would differentiate between normal or next day shipping.
3)	If I want to add a new shipping method, for example Europe Next Day shipping then I must change the Basket class again.

All these issues arise because we have mixed up responsibilities – the Basket class is responsible for managing lists of products AND calculating shipping. Calculating shipping has got nothing to do with adding and removing products. Worse, the shipping calculation is highly likely to change on a regular basis (changes to charges, adding or replacing shipping options).

We solve the design problem bt extracting the shipping calculations (algorithms) and encapsulating them behind an interface into their own classes. There is a family of shipping calculations (UK, Europe and ROW at time of writing) so we define a common interface.

```java
public interface ShippingCostStrategy {
    ShippingCost calculate (List<Product> products);
}
```
The Basket code now only needs to be concerned with adding and removing products. We remove the Destination from the Basket and replace it with the ShippingCostStrategy. This makes the Basket class much simpler. We also create a Value Object class called ShippingCost to represent the actual cost.

```java
public class ShippingCost {
    public final static ShippingCost Zero = new ShippingCost();
    private final double cost;
    private ShippingCost() {
        this(0);
    }
    public ShippingCost(double cost) {
        this.cost = cost;
    }
    public double getCost() {
        return cost;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShippingCost that = (ShippingCost) o;
        return Double.compare(cost, that.cost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cost);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShippingCost{");
        sb.append("cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }
}
```

```java
public class BasketWithShippingStrategy {
    private final List<Product> products = new ArrayList<>();
    private final ShippingCostStrategy shippingCostStrategy;
    public BasketWithShippingStrategy(ShippingCostStrategy shippingCostStrategy) {
        this.shippingCostStrategy = shippingCostStrategy;
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }
    public ShippingCost getShippingCharge()
    {
        return shippingCostStrategy.calculate(products);
    }
}
```

Note how the Basket no longer needs to know about Destination, we can provide a ShippingCost strategy based on anything we like.

Each shipping cost algorithm goes into its own class. For example, the UK strategy just returns 0.

```java

public class UKShippingStrategy implements ShippingCostStrategy {
    @Override
    public ShippingCost calculate(List<Product> products) {
        return  ShippingCost.Zero;
    }
}
```

Both Europe and ROW strategies are currently based on total weight of product. We can pull the total weight calculation up into a base class.

```java
abstract class WeightBasedShippingStrategy implements ShippingCostStrategy {
    protected static double totalWeight(List<Product> products) {
        double weight = 0.0d;
        for (Product product : products) {
            weight += product.getWeight();
        }
        return weight;
    }
}
````
The two concrete implementations extend the base class and implement the interface.
```java

public class EuropeShippingStrategy extends WeightBasedShippingStrategy  {
    private final static double CHARGE_PER_KG = 1.25d;

    @Override
    public ShippingCost calculate(List<Product> products) {
        return  new ShippingCost(totalWeight(products) * CHARGE_PER_KG);
    }
}
```

```java
public class RowShippingStrategy extends WeightBasedShippingStrategy  {
    private final static double NO_COST = 0.0d;
    private final static double MIN_CHARGE = 10.0d;
    private final static double CHARGE_PER_KG = 5.5d;

    @Override
    public ShippingCost calculate(List<Product> products) {
        double cost = products.size() == 0.0d ? NO_COST : Math.max(MIN_CHARGE, totalWeight(products) * CHARGE_PER_KG); // higher of MIN_CHARGE or CHARGE_PER_KG
        return new ShippingCost(cost);
    }
}
```

Example usage
```java

BasketWithShippingStrategy basketWithShippingStrategy = new BasketWithShippingStrategy(new RowShippingStrategy());
basketWithShippingStrategy.addProduct(book1);
System.out.format("Shipping %f%n", basketWithShippingStrategy.getShippingCharge());
```

### Summary

In a real Ecommerce system, we will still need some method of asking the user which shipping method they want and instantiating the correct concrete implementation class to pass into the Basket, and we will look at some design patterns that help us do that later, but for now a summary of what we have accomplished using the Strategy pattern.

- We have reduced the number of responsibilities the Basket class has from two (manage list of products, calculate shipping charge) to one.
- We have simplified the Basket class because it’s doing less.
- We have created independent implementations of ShippingCost strategy in separate classes. If we need change an implementation, we just update that class.
- We can create many different concrete implementations of a ShippingCost strategy for any kind of business reason, and the Basket class does not need to change.

- Each concrete implementation of a ShippingCost can be independently tested and validated.

"Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently of classes that use it." (Gamma et al. 1994 Ch5).

The Strategy pattern is one of the most useful design patterns, it simplifies code by taking algorithmic code out of a class and putting into its own class encapsulated behind and abstract interface. Getting the client code to make calls to the abstract interface means that we can choose the concrete implementation at runtime and put the code which chooses the concrete implementation somewhere else.

# Using Strategy Pattern to Determine which player has a turn in a board game

If we are playing board game with more than one player, we need to determine which player has the next turn. Here are two examples of strategy that returns a 0-based index of the player whose turn it is.

The player selector interface

```java
interface PlayerSelector {
    int next();
}
```

We can implement a simple alternating player selector for two players

```java
class TwoPlayerSelector implements PlayerSelector {
    private int currentPlayer = 0;

    @Override
    public int next() {
        int playerToReturn = currentPlayer;
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
        return playerToReturn;
    }
}
```

Or a round-robin selector for 4 players

```java
class FourPlayerSelector implements PlayerSelector {
    private int currentPlayer = 0;

    @Override
    public int next() {
        int playerToReturn = currentPlayer;
        currentPlayer = (currentPlayer + 1) % 4;
        return playerToReturn;
    }
}
```

In these examples the variation is in the number of players, but we could have other variations such as controlling the order of play.

This is a universal round-robin selector for any number of players that starts with player 0

```java
class UniversalPlayerSelector implements PlayerSelector
{
    private final int numberOfPlayers;
    private int currentPlayer;

    UniversalPlayerSelector(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public int next() {
        int playerToReturn = currentPlayer;
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        return playerToReturn;

    }
}

```
A variant of the universal selector that starts with the highest numbered player, reversing the order of play.

```java
class ReverseUniversalPlayerSelector implements PlayerSelector
{
    private final int numberOfPlayers;
    private int currentPlayer;

    ReverseUniversalPlayerSelector(int numberOfPlayers) {

        this.numberOfPlayers = numberOfPlayers;
        currentPlayer = numberOfPlayers - 1;
    }

    @Override
    public int next() {
        int playerToReturn = currentPlayer;
        currentPlayer = (currentPlayer = currentPlayer -1) < 0 ? numberOfPlayers - 1 : currentPlayer;
        return playerToReturn;

    }
}
```
The family of algorithms here is the way we decide which player has the next turn. We can create many different algorithms to select the next player, for example random selection or selection based on player progress, and hide them behind the PlayerSelector interface.

## Using for-each loops to iterate through players
The contract for the PlayerSelector interface is that the next() method returns the index of the player whose turn it is.
We can wrap our PlayerSelector in an **Iterator** to make it easier to use in a game loop.
`Iterable<T>` is a standard Java interface that allows an object to be the target of the "for-each loop" statement.

```java
public class PlayerIterable implements Iterable<Integer> {
    private final PlayerSelector playerSelector;

    public PlayerIterable(PlayerSelector playerSelector) {
        this.playerSelector = playerSelector;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PlayerIterator();
    }

    private class PlayerIterator implements Iterator<Integer> {

        @Override
        public boolean hasNext() {
            return true;
        } //always true for infinite iteration

        @Override
        public Integer next() {
            //The compiler infers Integer.valueOf(playerToReturn) to box the integer
            return playerSelector.next();

        }
    }
}
```
We can now use the PlayerIterable in a game loop. Here is an example of a game loop for a 4 player game. This is an example of an infinite iterator, so we need to provide a way to break out of the loop when the game is over.

```java
Iterable<Integer> iterableSelector = new PlayerIterable(new FourPlayerSelector());
for (int player : iterableSelector) {
    System.out.print(player + " ");
    if(gameOverCondition()){
        break;
    }
}
```




