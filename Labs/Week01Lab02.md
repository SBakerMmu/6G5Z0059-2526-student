# Software Design and Architecture Week01 Lab 02 Worksheet

## Name the parts of a class correctly

Name the parts of this class using the lecture notes, Google style guide and your own research.

Copy the code into your IDE and annotate the code using comments to identify the parts of the class.

```java
public abstract class Student {
    private static final String UNIVERSITY = "Example University";
    private String name = "Unknown";
    private final int studentID;
    private String course = "Undeclared";


    public Student(int studentID) {
        this.studentID = studentID;
    }

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
## Write Java class correctly

IntelliJ has a quick and straightforward way of creating a new Java project that we can use for many of the labs.

`IntelliJ File menu → New > Project…`

Note that some settings may be different on your installation.

Provide a project name, choose a location, and ensure that you have ticked the Add sample code box.

This should generate a runnable program:

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

When run, this outputs:

```
Hello, World!
Process finished with exit code 0
```

Implement equals(), toString(), and hashCode() for this DiceRoll class.
You can write your test code in the static main method in the code generated for you by IntelliJ. See hints and tips below.
```java

class DiceRoll {
    private final int value;

    DiceRoll(int value) {
        this.value = value;
    }
}
```

Implement equals(), toString(), and hashCode() for the TwoDiceRoll class.
You can write your test code in the static main method in the code generated for you by IntelliJ.

```java

class TwoDiceRoll {
private final DiceRoll one;
private final DiceRoll two;

    TwoDiceRoll(DiceRoll one, DiceRoll two) {
        this.one = one;
        this.two = two;
    }
}
```
### Hints and Tips

Recall that Java has primitive and reference types.

`==` checks values for primitive types, but only checks the referential equality of two objects (i.e. both sides of the operation are the same instance in memory).

The default `Object.equals()` method also only checks the referential equality of two objects (i.e. equivalent to ==), so we need to override this default implementation to check the contents of our class.

Writing `equals()` correctly requires some care. You need to determine which fields participate (usually all of them) and then write the equality correctly.

The signature is `public boolean equals(Object obj)` so you need to deal with obj being null and also obj being a different class (in both cases return false).

Java has strict rules for equals() with non-null object references:

- Reflexive: for any non-null reference value x, x.equals(x) should return true.
- Symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
- Transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
- Consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.

> ⚠ If you implement equals() you must implement hashCode() and vice-versa.

Some algorithms and data structures use the hash code value as a representation of a contents of the object—most notably the HashMap, which uses the hash code to decide which bucket should hold a key-value pair. As with equals(), there are a set of rules for implementing the hashCode() method:

It must be consistent like equals: `hashCode()` must consistently return the same integer, provided no information used in equals comparisons on the object is modified.

If two objects are equal according to the `equals()` method, their `hashCode()` methods must return the same integer value.

Hash codes are not necessarily unique, but a good `hashCode()` method distributes values widely.

> ☑ We strongly advise you use static int hash(Object... values) method of java.util.Objects to generate suitable hash values when there is more than one field involved in the equals. This is surprisingly hard to do well yourself.
