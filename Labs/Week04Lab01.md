# Software Design and Architecture Week04 Lab 01 Worksheet

There are multiple activities each week, and you will probably not get everything done in the timetabled lab sessions; therefore, it is highly recommended that you complete the labs in your own time each week to avoid falling behind.

Completing the labs will get you ready for writing the assignment code.

**Advanced** Labs are optional, but completing the Advanced Labs will introduce you to more advanced techniques and improve your design skills.

## Hint: Starting a new Project

IntelliJ has a quick and simple way of creating a new Java project that we can use for many of the labs.

Intelli-J File menu -\> New \> Project…

Provide a project name, chose a location and ensure that you have ticked the **Add sample code** box.


# Create Singletons representing Currencies

We want to represent a currency as a class. A currency has

  - A code (String) which a three-letter ISO 4217 currency code (e.g., "GBP", "USD", “EUR”).

  - A name (String): Stores the full name of the currency (e.g., "British Pound", "US Dollar").

  - A symbol (char): Stores the currency symbol (e.g., '£', '$',’ €’).

  - Decimals (int): Stores the number of decimal places typically used for this currency (usually 2).

The currency code uniquely identifies the currency.

The code below is a simple currency ValueObject.

Use Intelli-J File menu -\> New \> Project… to create a new sample project and then write public final static Singletons for the following Currencies:

|      | Code | Name          | Symbol | Decimals |
|------|------|---------------|--------|----------|
| GBP  | GBP  | British Pound | £      | 2        |
| USD  | USD  | US Dollar     | $      | 2        |
| EUR  | EUR  | Euro          | €      | 2        |


```java
class Currency {
    final static char POUND_SYMBOL = '£';
    final static char DOLLAR_SYMBOL = '$';
    final static char EURO_SYMBOL = '€';

    private final String code;
    private final String name;
    private final char symbol;
    private final int  decimals;

    public Currency(String code, String name, char symbol, int decimals) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
    }

    public String getName() {
        return name;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getDecimals() {
        return decimals;
    }
}

```

This a Value Object so you need to implement the `equals()`, `hashCode()` and `toString()` methods.

This Value Object has multiple fields, so you need to decide which ones to include in these method implementations.

Hint. To compute a hashCode for multiple fields use

`Objects.hash(Object... values)`

This method is very useful for implementing Object. hashCode() on objects containing multiple fields

In the main method of the test program, write a small program that prints each singleton Currency to the console as

```text
GBP (£)

USD ($)

EUR (€)
```
**Question**: Why is it important that any Singletons are final and immutable?

## Hints and Tips

See the lecture notes and module textbook for a discussion of patterns for creating objects.

Singletons are intended to be single primitive value or single object instance within a program.

In Java static fields can be used to hold constant primitive values or a single instance of an immutable class. If the value is constant, we use the CONSTANT\_NAME naming convention.

``