# Download and Install #
**Funcito** is deployed as a jar file.  You can manually download it from [Downloads](http://code.google.com/p/funcito/downloads/list) and add it to your classpath, or point the dependency management tool of your choice (Maven, Ivy, Gradle, SBT, etc.) to the following Maven Central coordinates (version shown below is an example and current version may be higher):

```
<groupId>com.googlecode.funcito</groupId>
<artifactId>funcito</artifactId>  
<version>1.3.1</version> 
<scope>runtime</scope>
```

Find the latest versions and their coordinates at:
http://central.maven.org/maven2/com/googlecode/funcito/funcito/

There are no required transitive dependencies.  You of course need the jar file for the [supported functional-API](http://code.google.com/p/funcito#Supported_Functional_Programming_Libraries) that you are using with Funcito.  You may optionally provide a bytecode-provider for class proxying, or you may already have one if you use common frameworks like Hibernate or Spring (see more details in the [FAQ](FAQ#What_are_the_dependencies_for_the_project?.md)).

# Accessing the public API #

The public API to Funcito is rather small, and most of it is made available by one or more static imports.  You can access virtually the whole Guava API by a single static import:

```
import static org.funcito.Funcito.*;
```

The main method used across all functional libraries is the `callsTo()` method.  Other than that, you may call one of the following static methods from the org.funcito.Funcito class to access the delegate for the public API methods that are specific to the functional library that you use:
```
public static GuavaDelegate guava();
public static FJDelegate fj() // for Functional Java
public static JediDelegate jedi();
public static Play2Delegate play2();
public static CollectGenDelegate collectGen();
public static RxJavaDelegate rxJava();
```

However, if you are going to be using only one functional framework at a a time -- which is likely the case -- you can instead statically import **one** of the following:
```
import static org.funcito.FuncitoGuava.*;
// or
import static org.funcito.FuncitoFJ.*;  // Functional Java
// or
import static org.funcito.FuncitoJedi.*;
// or
import static org.funcito.FuncitoPlay2.*;
// or
import static org.funcito.FuncitoCollectGen.*;
// or
import static org.funcito.FuncitoRxJava.*;
```
which imports not only `callsTo()` but also the API methods that uniquely apply to each supported functional library.

# Basic Usage #

Let's start with the following classes:
```
public class MyClass {
    private String myString;
    private Integer other;

    public MyClass(String myString, Integer other) {
        this.myString = myString;
        this.other = other;
    }

    public String getMyString() { return myString; }
    public void setMyString(String value) { this.myString = value; }

    public Integer getOther() { return other; }
    public void setOther(Integer other) { this.other = other; }

    public boolean positive() {
        return this.other > 0;
    }

    public boolean isLengthGreaterThan(int lower) {
        return getMyString().length() > lower;
    }

    public MyClass doubled() {
        return new MyClass(
            getMyString() + getMyString(), getOther().intValue()*2 );
    }

    @Override
    public String toString() {
        return getMyString();
    }
}

public class Grows {
    int i = 0;
    public Grows incAndReturn() {
        ++i;
        return this;
    }
    public void inc() { i++; }
    public void dec() { i--; }
}
```

The key to Funcito usage across all APIs is the `T callsTo(Class<T> clazz)` method, which returns a proxy of the class that has methods you want to wrap. So, to wrap a method from `MyClass` you need to call `callsTo(MyClass.class)`.  If you plan to wrap only one method in a class, you can simply inline the use of `callsTo` as the argument to one of the `xxxxFor()` methods, such as Guava's `functionFor`:

```
Function<MyClass,String> getMyString = functionFor( callsTo(MyClass.class).getMyString() );
```

The Functional Java equivalent is:

```
F<MyClass,String> getMyString = fFor( callsTo(MyClass.class).getMyString() );
```

and the Jedi version is:
```
Functor<MyClass,String> getMyString = functorFor( callsTo(MyClass.class).getMyString() );
```

If you are going to wrap several methods from the same class, you can extract the proxy class rather than inlining every call:

```
final MyClass MY_CLASS = callsTo(MyClass.class); // a Funcito proxy
Function<MyClass,String> getMyString = functionFor( MY_CLASS.getMyString() );
Function<MyClass,Integer> getOther = functionFor( MY_CLASS.getOther() );
```

# Predicates #
Some functional frameworks provide a special class/interface to represent functions with a boolean return type.  In Guava, this is called a `Predicate`.  Funcito provides the ability to wrap boolean-returning methods (either primitive `boolean` or the wrapper class `Boolean`) as predicates:

```
// In Guava:
Predicate<MyClass> positive = predicateFor( MY_CLASS.positive() );

// or in Jedi:
Filter<MyClass> positive = filterFor( MY_CLASS.positive() );

// There is no predicate-like class in Functional Java
```

In addition, there is an alternate form of the predicate producer for Guava.  Since Guava's `Predicate.apply(T t)` returns a primitive `boolean`, there can be a problem if you wrap a method that returns a `Boolean` wrapper object rather than a `boolean` primitive value.  A `Boolean` object reference value may be null, and auto-unboxing that value results in a `NullPointerException` when the predicate is evaluated.  This is true with or without Funcito.  As a convenience, Funcito has added an overloaded form, `predicateFor(Boolean proxiedReturnVal, boolean defaultForNull)`.  When used as follows:

```
Predicate<MyClass> safePositive = predicateFor( MY_CLASS.positive(), false );
```

it ensures that null return values from the wrapped method will not result in a `NullPointerException` but rather return the default value, `false`.  Jedi's `Filter` class already returns the `Boolean` wrapper type and so there is no need for a null-safe overloaded form of `filterFor()`.

# Commands #
Some functional frameworks provide a class or interface to represent functors without a captured return-type.  These functors are used simply to produce a side-effect rather than a return value.  Some examples from among the Funcito-supported APIs are the Jedi `Command`, the Functional Java `Effect`, and the collections-generic `Closure`.

In the easiest to use case in Funcito, you can wrap method calls in the same way as functions or predicates, as long as the method call returns a non-void type (example is for a Jedi `Command`):

```
Command<Grows> superTypeRet = commandFor(callsTo(Grows.class).incAndReturn());
```

This syntax can be supported whenever the method being wrapped has a return value, but that return value is being ignored.  But the `commandFor()` factory method in Funcito and the factory methods like it, require an argument which means the return type of the method
cannot be void.  So to support wrapping void-methods as command-functors, there needed to be an alternate (and more verbose) syntax:

```
prepareVoid(callsTo(Grows.class)).inc(); // captures the method call
Command<Grows> normalCall = voidCommand(); // extracts the above into a Command
```

# Advanced Usage #

For usages not found below, check [additional code examples here](http://code.google.com/p/funcito/source/browse/#git%2Fexamples%2Forg%2Ffuncito%2Fexample).

## Methods With Arguments ##

Up until now, we have only shown how to wrap methods that have no arguments.  But what if you wanted to wrap the method `isLengthGreaterThan(int lower)`?  How and when would you provide the argument value of `lower` for evaluating that method?  In Funcito, you can statically bind argument values to methods that you are wrapping, just by providing them in the function definition as you would normally pass an argument to a method:

```
// creates a predicate that evaluates whether the `myString` length is greater than 4
Predicate<MyClass> isLengthGreaterThan4 = predicateFor(MY_CLASS.isLengthGreaterThan(4));
```

In the above definition, the value 4 is statically and permanently bound to the Predicate.

## Method Chaining ##

Let's say you wanted to create a function that first called `doubled()` to create another `MyClass` and then extracted the String portion of that.  Without Funcito, you would probably just create a function-object that chained the results (below is an example in Guava):

```
Function<MyClass,String> doubledString = new Function<MyClass,String>() {
    String apply(MyClass src) {
        return src.doubled().getMyString();
    }
}
```

We want to get rid of that boilerplate with Funcito.  Your first thought might be to make two functions with Funcito and compose them together as one function using the your functional-API's built-in support, such as:

```
Function<MyClass,MyClass> doubled = functionFor(MY_CLASS.doubled());
Function<MyClass,String> getMyString = functionFor(MY_CLASS.getMyString());
Function<MyClass,String> doubledString = Functions.compose(getMyString,doubled);
```

But that's arguably much uglier and harder to understand than the first non-Funcito way.  But starting with Funcito 1.1.0, you can now directly compose chained method calls as a Function:

```
Function<MyClass,String> doubledString = functionFor(MY_CLASS.doubled().getMyString());
```

## Safe Navigation and other Modes ##

Method chaining is great, but it can result in a null pointer if any intermediate value in the chain returns null.  That's true even outside of Funcito and functional programming.  Outside of functional programming one would normally not explicitly chain at all if there were the risk of intermediate nulls.

Given the following interfaces:

```
interface A {
    public B getB();
}

interface B {
    public C getC();
}

interface C {}
```

...you might ordinarily have to code chaining something very inelegant such as:

```
if (myA!=null && myA.getB()!=null) {
   return myA.getB().getC();
}
return null; // default if any intermediate value is null
```

This gets even uglier very quickly as the chain grows.  And then try wrapping the above in an anonymous inner class to implement a 3rd party command functor object -- Lots o' yuck.

Enter Funcito Modes and in particular the `SafeNav` mode.  In the Groovy language, the above gets simplified with something called the safe navigation operator:

```
return myA?.getB()?.getC();
```

Since we couldn't add operators to Java, we tried to do the next best thing using the `SafeNav` Mode.

```
Function<A,C> func = functionFor(callsTo(A.class).getB().getC(), safeNav());
```

The above safeNav() comes from a static import of `org.funcito.mode.Modes.*` along with other modes, and this mode automatically provides safe-navigation on all intermediate calls, with a default value of null.  Note that you don't even have to keep repeating safeNav() for each call, unlike the Groovy operator.  It automatically applies to every intermediate call.  And in another feature that goes beyond the Groovy operator, there is an alternate form of `safeNav` that can provide a default value other than null:

```
Function<A,C> func = functionFor(callsTo(A.class).getB().getC(), safeNav(SOME_OTHER_DEFAULT_A));
```

But `safeNav` is not the only Mode available. `noOp()` is the default mode of every ordinary Funcito-produced functor that does not explicitly declare a mode. `tailDefault` is sort of the complement to `safeNav`, as it allows a different value to be substituted if the final ("tail") method in a chain has a result of null.  And users can write and use their own Modes or TypedModes to perform other more specific validations or execution-altering behaviors.