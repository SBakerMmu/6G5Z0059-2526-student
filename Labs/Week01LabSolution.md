# Software Design and Architecture Week 1 Lab Solutions

Suggested answers to the Week 1 lab exercises.

## Coding Standard
> ☑ The main things to look for are
Class, field and method naming conventions,
sensible layout.

```java

class GoodStyleClass {

  int avalue = 0;
  int bvalue = 0;

  public GoodStyleClass() {
    avalue = 1;
    bvalue = 1;
  }

  public int add1(int x, int y) {
      if (x < 6) {
          return x + y;
      }
    return 0;
  }

  public int add2(int x, int y) {
    for (int i = 0; i < y; i++) {
      x += 1;
    }
    return x;
  }
}
```

## Name the parts of a class correctly.

```java

abstract class Student {

  //field declarations
  //static fields are sometimes called class variables.
  // The final keyword indicates the field has a constant value, i.e. once the class variable has been initialized the value cannot be changed
  private static final String UNIVERSITY = "Example University";

  //non-static fields can be called instance variables
  //The final keyword indicates the field has a constant value, i.e. once the instance variable has been initialized the value cannot be changed
  private final int studentID;
  //these non-static fields are initialised but not final
  private String name = "Unknown";
  private String course = "Undeclared";

  //Constructor
  public Student(int studentID) {
    this.studentID = studentID;
  }

  //Methods

  //get and set methods are sometimes called Properties, or Getters and Setters
  //By convention, they take the name of the field they get or set.
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStudentID() {
    return studentID;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public static String getUniversity() {
    return UNIVERSITY;
  }

  //abstract methods have no implementation
  public abstract double calculateDegreeClassification();

  @Override
  public String toString() {
    return "Student{" +
        "name='" + name + '\'' +
        ", studentID=" + studentID +
        ", major='" + course + '\'' +
        '}';
  }
}
```

## Write a Java Class implementation.

```java


class DiceRoll
{
    private final int value;

    DiceRoll(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        DiceRoll diceRoll = (DiceRoll) o;
        return value == diceRoll.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("DiceRoll %d", value);
    }
}
```

The main learning point about implementing the TwoDiceRoll class is how it delegates work to the two DiceRoll instances.

```java
import java.util.Objects;

class TwoDiceRoll {

  private final DiceRoll one;
  private final DiceRoll two;

  public TwoDiceRoll(DiceRoll one, DiceRoll two) {
    this.one = one;
    this.two = two;
  }

  public int getValue() {
    return one.getValue() + two.getValue();
  }

  //The equals implementation is made up of equals() applied to all non-static fields
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TwoDiceRoll that = (TwoDiceRoll) o;
    return one.equals(that.one) && two.equals(that.two);
  }

  //The hash  implementation is made up of Objects.hash() applied to all non-static fields
  @Override
  public int hashCode() {
    return Objects.hash(one, two);
  }


  @Override
  public String toString() {
    return String.format("TwoDiceRoll %s %s", one, two);
  }
}
```
Some test code
```java
 public static void main(String[] args) {

  DiceRoll dice1 = new DiceRoll(6);
  DiceRoll dice2 = new DiceRoll(6);
  System.out.println(dice1 == dice1);
  System.out.println(dice2 == dice2);
  System.out.println(dice1 == dice2);
  System.out.println(dice1.equals(dice2));

  System.out.println("======");
  TwoDiceRoll twoDiceRoll1 = new TwoDiceRoll(new DiceRoll(6), new DiceRoll(6));
  TwoDiceRoll twoDiceRoll2 = new TwoDiceRoll(new DiceRoll(6), new DiceRoll(6));
  System.out.println(twoDiceRoll1 == twoDiceRoll1);
  System.out.println(twoDiceRoll2 == twoDiceRoll2);
  System.out.println(twoDiceRoll1 == twoDiceRoll2);
  System.out.println(twoDiceRoll1.equals(twoDiceRoll1));
}
```
If you have implemented your classes correctly, the output should be

```Text
true
true
false
true
======
true
true
false
true
```
