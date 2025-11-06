# Software Design and Architecture - Week 06 Lab Solutions

## Create a Façade to implement a Dice Game

We first need the implementations for the dice shaker

```java
interface DiceShaker {
  int shake();
}

class SingleDiceShaker implements DiceShaker {
  private final Random random = new Random();

  SingleDiceShaker() {
  }

  public int shake() {
    return this.random.nextInt(6) + 1;
  }
}
```

An example implementation for the GameBoard

```java
class GameBoard {

  private static final int HOME = 0;
  private static final int END = 5;
  private static final int LENGTH = END - HOME + 1;
  private int index;
  private int moves;

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

  public void advance(int count) {
    int newIndex = index + count;
    setIndex(newIndex % LENGTH);
    moves++;
  }

  public boolean isHome() {
    return (index == HOME);
  }

  public int getMoves() {
    return moves;
  }
}
```

An implementation of a **stateless** facade.

```java
interface StatelessFacade {
  int play();
}

class ConcreteStatelessFacade implements StatelessFacade {
  @Override
  public int play() {
    DiceShaker shaker = new SingleDiceShaker();
    GameBoard board = new GameBoard();
    while(!board.isHome() || board.getMoves() ==0)
    {
      board.advance(shaker.shake());
    }
    return board.getMoves();
  }
}
```
The stateless version of the facade brings together the DiceShaker and the Gameboard without either of them being aware of the other.

An implementation of the **stateful** facade

```java
class ConcreteStatefulFacade implements StatefulFacade {

  final GameBoard board = new GameBoard();

  @Override
  public void play() {
    private final DiceShaker shaker = new SingleDiceShaker();
    while(!board.isHome()  || board.getMoves() ==0 )
    {
      board.advance(shaker.shake());
    }
  }

  @Override
  public int getShakes() {
    return board.getMoves();
  }
}
```
The stateful implementation holds the Diceshaker and Gameboard as state in fields.

### Stateful vs. Stateless
With the stateless version the `play()` method can be called any number of times as it will start a new game every time. It is arguably easier to understand. It is also tread-safe - multiple clients can use the facade concurrently without interference.

The stateful version has a flaw, in that you can't call `play()` twice on the same instance of the facade, but it does maintain session information.

Which is best depends on the design problem, but in general, stateless facades are the most common in use and this is how a client would *probably* expect a facade to work.

A common term in software architecture is **service**, which is a stateless, functional component such as sending an Email. A stateless Façade is a common way to implement a service.

### Questions
As with all design questions 'it depends' on the requirements, but suggested answers

**Q**) Which class should be responsible for deciding if the game is ‘won’ or more ‘shakes’ are required ?

**A**) If we put the decision into the Gameboard, we can only play one sort of game. By putting the decision into the facade we can have different games, for example winning by landing on HOME multiple times. An alternative approach would have been to use a strategy to decide what constitutes as win. The facade could then aggregate the board, the dice and the strategy.

**Q**) Which class is responsible for counting the number of moves

**A**) There is an argument to hold the count within board. If we held the count outside the board is that the logic to increment the count would be repeated in multiple places, and it is  unlikely to be any variation in this logic.

**Q**) What behaviour would you expect if you call the play() method twice on the same façade instance?

**A**) A reasonable expectation would be the game would play again. The stateful facade does not have this behaviour - you would need to create a new instance of the facade to plan another game. Therefore, the stateful facade might need documenting to explain its behaviour or an exception throwing if play() was called twice.

## Write a Mediator which mediates between an Ecommerce basket and a Discount algorithm.

Given the interfaces

```java

interface Colleague {
}

interface Mediator {
  void onChanged(Colleague colleague);
}
```
Product, Basket and Discounter classes

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

```java
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
    public double getTotalWithDiscount() {
        return totalWithDiscount;
    }
    public double getDiscountApplied() { return discountApplied *100d;    }
}
```

```java
class Discounter implements Colleague {

    private static final double DISCOUNT_0 = 0.0d;
    private static final double DISCOUNT_10 = 0.1d;
    private static final double DISCOUNT_20 = 0.2d;
    private static final double DISCOUNT_30 = 0.3d;
    private static final double DISCOUNT_40 = 0.4d;
    private static final double DISCOUNT_50 = 0.5d;
    private static final double GOOD_VALUE_DISCOUNT_THRESHOLD = 100d;
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
        goodsDiscount = goodsValue > GOOD_VALUE_DISCOUNT_THRESHOLD ? DISCOUNT_20 : DISCOUNT_0;
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
        discountApplied = Math.max(goodsDiscount, codeDiscount);
        mediator.onChanged(this);
    }

}
```

The Mediator implementation

```java
class PricingMediator implements Mediator {
    private Basket basket;
    private Discounter discounter;

    public void registerColleagues(Basket basket, Discounter discounter) {
        this.basket = basket;
        this.discounter = discounter;
    }

    @Override
    public void onChanged(Colleague colleague) {

        if (colleague == basket) {
            discounter.setGoodsValue(basket.getTotalBeforeDiscount());
        }
        if (colleague == discounter) {
            basket.setDiscountApplied(discounter.getDiscount());
        }
    }
}
```
A simple test method

```java
public final class Example {
    private final static String DISCOUNT10 = "Discount10";
    private final static String DISCOUNT50 = "Discount50";

    public static void run() {
        PricingMediator mediator = new PricingMediator();

        Basket basket = new Basket(mediator);
        Discounter discounter = new Discounter(mediator);
        mediator.registerColleagues(basket, discounter);

        Product a1 = new Product("A1", 50.0d);
        Product a2 = new Product("A2", 250.0d);

        System.out.format("Empty Basket%n", a1);
        showBasket(basket);

        System.out.format("Add %s%n", a1);
        basket.addProduct(a1);
        showBasket(basket);

        System.out.format("Add %s%n", a2);
        basket.addProduct(a2);
        showBasket(basket);

        System.out.format("Apply %s%n", DISCOUNT10);
        discounter.setDiscountCode(DISCOUNT10);
        showBasket(basket);
        System.out.format("Apply %s%n", DISCOUNT50);
        discounter.setDiscountCode(DISCOUNT50);
        showBasket(basket);

        System.out.format("Remove Discount Code%n");
        discounter.removeDiscountCode();
        showBasket(basket);

        System.out.format("Remove %s%n", a2);
        basket.removeProduct(a2);
        showBasket(basket);

        System.out.format("Remove %s%n", a1);
        basket.removeProduct(a1);
        showBasket(basket);

    }

    private static void showBasket(Basket basket) {
        System.out.format("Total price with discount %.0f%%: %.2f%n", basket.getDiscountApplied(), basket.getTotalWithDiscount());
    }
}
```

Should output

```Text
Empty Basket
Total price with discount 0%: 0.00
Add A1 £50.00
Total price with discount 0%: 50.00
Add A2 £250.00
Total price with discount 20%: 240.00
Apply Discount10
Total price with discount 20%: 240.00
Apply Discount50
Total price with discount 50%: 150.00
Remove Discount Code
Total price with discount 20%: 240.00
Remove A2 £250.00
Total price with discount 0%: 50.00
Remove A1 £50.00
Total price with discount 0%: 0.00
```

**Question**: Both Façade and Mediator are patterns that move collaboration code out of classes like Basket and Discounter, leaving them more general purpose. What are the differences between the two patterns, and when would you use one rather than the other?

**Answer**:
The Facade pattern hides the supplier classes (Basket and Discounter) behind a simple API designed for the client to use. As such the Facade is determining what the client can achieve.

The Mediator pattern does not hide the existence of ths supplier (Basket and Discounter) classes from the client, but the Mediator encapsulates and hides the communication between the classes from the client code. The client code might have more flexibility to achieve a greater range of goals with the Mediator handling object communication in the background. However the PricingMediator class in this example has a single onChanged method that will grow and become more complex if more colleagues are added, whereas a Facade may have multiple public methods each with a subset of the communication code.

Use Facade when you can hide the existence of the suppliers from the client, use Mediator when the client needs to work with the suppliers directly.

Another use for Mediators is in GUI Dialog Box style code. There is no concept of a client trying to achieve a goal (so you can't use Facade), individual controls are notifying a central mediator of state changes, and the mediator is using that information to instruct other controls to change their state. 

For example imagine we have a drop-down list of shipping destinations (UK, EUR, ROW). If the user selects UK we want to hide a shipping cost field, if the user changes their selection to EUR or ROW we want to show the shipping cost field. Rather than putting all that logic in an `onChanged` method on the Shipping Destination control, put the logic into a central mediator. 