# Software Design and Architecture - Week 07 Lab Solutions

## Using the State Pattern to manage Authentication status.

The problem with writing state machine code in this procedural way is that there is a combinatorial explosion between the events and the states - there will be switch statement for each event, and each switch statement will need as many branches as there are states. Adding a new event requires adding a new switch statements, adding a new state requires extending every switch statement by adding a new branch. The same issues apply if you replace switch with if. You should recognise that long conditional statements are not a good solution for extensibility or maintainability

A better way of coding this is to use the **State Pattern**. The State pattern creates a specific class for each State. Each State class can then be varied independently to handle the requirements of each state.

First define an interface called State that will be implemented by all the State classes and an interface called Context to be implemented by the State Machine.

```java
interface State {
    void login(Context context);
    void logout(Context context);
}

interface Context {
    void setState(State state);
}
```
Create two class that represent each state in the state machine

```java

class LoggedInState  implements State {

    @Override
    public void login(Context context) {
        //already logged in
    }

    @Override
    public void logout(Context context) {
        context.setState(new LoggedOutState());
    }

    @Override
    public String toString() {
        return "Logged In";
    }
}

class LoggedOutState implements State {
    @Override
    public void login(Context context) {
        context.setState(new LoggedInState());
    }

    @Override
    public void logout(Context context) {
        //already logged out
    }

    @Override
    public String toString() {
        return "Logged Out";
    }
}
```

All the logic associated within the state now lives in a concrete state class.

The context provides the public API to the state machine - its methods represents the possible triggers - which in this case is login and logouts.

```java
class AuthenticationContext implements Context{

    private State status = new LoggedOutState();

    public void login()
    {
        status.login(this);
    }

    public void logout()
    {
        status.logout(this);
    }



    @Override
    public void setState(State state) {
        status = state;
    }

    public String getStatus() {
        return status.toString();
    }
}
```

## Auto Logout
The auto logout version has a down count (you could implement it as an incrementing counter) in the LoggedIn state.

The State interface needs another trigger for the tick.

```java
interface State {
    void login(Context context);
    void logout(Context context);
    void tick(Context context);
}
```

The LoggedIn state contains the counter. Each tick event is counted, and when the count is done, we switch to the LoggedOut state.

```java
class LoggedInState  implements State {

    private static final int MAX_TICKS = 3;
    private int count;

    public LoggedInState() {
        this.count = MAX_TICKS;
    }

    @Override
    public void login(Context context) {
        //already logged in
    }

    @Override
    public void logout(Context context) {
        context.setState(new LoggedOutState());
    }

    @Override
    public void tick(Context context) {
        if(--count == 0)
        {
            logout(context);
        }
    }

    @Override
    public String toString() {
        return "Logged In";
    }
}

class LoggedOutState implements State {
    @Override
    public void login(Context context) {
        context.setState(new LoggedInState());
    }

    @Override
    public void logout(Context context) {
        //already logged out
    }

    @Override
    public void tick(Context context) {

    }

    @Override
    public String toString() {
        return "Logged Out";
    }
}

class AuthenticationContext implements Context {

    private State status = new LoggedOutState();

    public void login()
    {
        status.login(this);
    }

    public void logout()
    {
        status.logout(this);
    }

    public void tick()
    {
        status.tick(this);
    }

    @Override
    public void setState(State state) {
        status = state;
    }

    public String getStatus() {
        return status.toString();
    }
}
```
## Auto Logout with Reset

The final example resets the count everytime the `login()` method is called on the AuthenticationContext. Calling `login()` causes a new instance of the `LoggedInState` class, initialised with MAX_TICKS to be assigned to the state variable.

```java
class AuthenticationContext {

  private interface State {
    void login();
    void logout();
    void tick();
  }

  private class LoggedInState  implements State {

    private static final int MAX_TICKS = 3;
    private int count;

    public LoggedInState() {
      this.count = MAX_TICKS;
    }

    @Override
    public void login() {
      //already logged in
      status = new LoggedInState();
    }

    @Override
    public void logout() {
      status = new LoggedOutState();
    }

    @Override
    public void tick() {
      if(--count == 0)
      {
        logout();
      }
    }

    @Override
    public String toString() {
      return "Logged In";
    }
  }

  private class LoggedOutState implements State {
    @Override
    public void login() {

      status = new LoggedInState();
    }

    @Override
    public void logout() {
      //already logged out
    }

    @Override
    public void tick() {

    }

    @Override
    public String toString() {
      return "Logged Out";
    }
  }

  private State status  = new LoggedOutState();

  public void tick()  {
    this.status.tick();
  }
  public void login() {
    this.status.login();
  }
  public void logout() {
    this.status.logout();
  }

  @Override
  public String toString() {
    return getStatus();
  }

  String getStatus() {
    return this.status.toString();
  }
}
```
### Implementation using Nested Classes

Java allows you to define classes and interfaces within another class. Such a class is called a **nested** class.

```java
class OuterClass {
    ...
    class NestedClass {
        ...
    }
}
```
There are two types of nested classes : non-static and static. Non-static nested classes are called **inner classes** and inner classes can access the private members of their enclosing classes.

Nested classes that are declared static are called **static nested classes**. Static nested classes cannot access private members of their enclosing classes.

This code fragment shows how to declare an inner and a static nested class within an outer class.

```java
class OuterClass {
    ...
    class InnerClass {
        ...
    }
    static class StaticNestedClass {
        ...
    }
}
```
You can also declare interfaces within classes. These are called **nested interfaces**. Nested interfaces are implicitly static.

This implementation of the state pattern uses private inner classes and private nested interfaces to keep the state pattern implementation entirely private to the `AuthenticationContext`. The use of non-static inner classes means that the State implementations can set variables with the context class directly. On the other hand, I cannot reuse my state classes in another context - I can only reuse the state machine using the `AuthenticationContext` class.

### Using Nesting as part of class design
Using nesting is a way of grouping classes that are intended to only be used in one place, in this example grouping the state machine implementation classes within a context class.

By making all the nested interfaces and nested classes private, the implementation is completely hidden, and we no longer need the public 'setState' method on the context class which increases encapsulation.

The Autoreset example implemented using Nested classes

```java
class AuthenticationContext {

    private State status = new LoggedOutState();

    public void tick() {
        this.status.tick();
    }

    public void login() {
        this.status.login();
    }

    public void logout() {
        this.status.logout();
    }

    @Override
    public String toString() {
        return getStatus();
    }

    String getStatus() {
        return this.status.toString();
    }

    private interface State {
        void login();

        void logout();

        void tick();
    }

    private class LoggedInState implements State {

        private static final int MAX_TICKS = 3;
        private int count;

        public LoggedInState() {
            this.count = MAX_TICKS;
        }

        @Override
        public void login() {
            //already logged in
            status = new LoggedInState();
        }

        @Override
        public void logout() {
            status = new LoggedOutState();
        }

        @Override
        public void tick() {
            if (--count == 0) {
                logout();
            }
        }

        @Override
        public String toString() {
            return "Logged In";
        }
    }

    private class LoggedOutState implements State {
        @Override
        public void login() {

            status = new LoggedInState();
        }

        @Override
        public void logout() {
            //already logged out
        }

        @Override
        public void tick() {

        }

        @Override
        public String toString() {
            return "Logged Out";
        }
    }
}
```
Use of nested classes is a way of grouping classes that are only used in one place, and controlling their visibility. In this example the state pattern implementation is completely hidden within the context class.

The downside of using nested classes is that the concrete state classes cannot be tested independently or reused in another context because they are private to the context class.









