## <font color='red'>NEW:</font> Version 1.3.1 Available in Maven Central ##
## News ##
  * **19-AUG-2014** Version 1.3.1 published in the [Maven Central Repository](http://search.maven.org/#artifactdetails|com.googlecode.funcito|funcito|1.3.1|jar), maintenance release for RxJava users
  * **03-NOV-2013** Version 1.3.0 published in the [Maven Central Repository](http://search.maven.org/#artifactdetails|com.googlecode.funcito|funcito|1.3.0|jar)
  * **30-OCT-2013** Version 1.3.0 released for [download here](http://code.google.com/p/funcito/downloads/list?can=3).  Features new support for [Command-like functors](http://code.google.com/p/funcito/wiki/UsageNotes#Commands), Netflix RxJava Framework, [Safe-Navigation Mode, and other Modes and Mode-API](http://code.google.com/p/funcito/wiki/UsageNotes#Safe_Navigation_and_other_Modes)
  * **26-OCT-2012** Version 1.2.0 released. Adds support for the Play! 2 Framework and collections-generic (generified Apache commons-collections)
  * **24-JUL-2012** Version 1.1.0 released. Features [method chaining & methods with parameters](http://code.google.com/p/funcito/wiki/UsageNotes#Advanced_Usage)
  * **02-APR-2012** Version 1.0.2 released.

<table><tr>
<td><img src='http://wiki.funcito.googlecode.com/git/images/Funcito_title_35pct.jpg' /></td>
<td>
<blockquote><wiki:gadget url="http://hosting.gmodules.com/ig/gadgets/file/110509162544058635853/steegle-google-sites-twitter-tweet-button.xml" height="39" border="0"/><br>
<br />
<br>
<wiki:gadget url="http://www.ohloh.net/p/597289/widgets/project_users_logo.xml" height="60" border="0"/><br>
</td>
</tr></table></blockquote>

# What Is _Funcito_? #
**Funcito** is a Java library that simplifies access to your [favorite functional programming APIs](#Supported_Functional_Programming_Libraries.md) by wrapping Java methods as the function-type objects defined in those libraries.

Rather than creating another functional programming API, Funcito makes it easy to define function-type objects that wrap existing methods.  As a result, your code has less noise and clutter of anonymous inner classes, annotations, etc.  And your code remains safe for automated-refactoring, since there is no String-based reflection or forced naming conventions.

## Using Funcito ##
Suppose you have a simple POJO with several Getter methods:
```java

public class Worker {
private int badgeNum;
private String lastName;
private String firstName;
private boolean employee;

public Worker(int badgeNum) { this.badgeNum = badgeNum; }
// ... setters would be here too...

public int getBadgeNum { return badgeNum; }
public String getLastName; { return lastName; }
public String getFirstName { return firstName; }
public boolean isEmployee { return employee; }
}
```

If you wanted to filter a collection of Worker objects to get the Workers who are employees, you could use the functional capabilities of Google Guava (examples for the other supported functional APIs are found in the [Usage Notes](UsageNotes.md)):

```java

Collection<Worker> employees = Collection2.filter(allWorkers, isEmployeePredicate);
```

But first you need to create the `isEmployeePredicate` object.  Implementing the Predicate interface normally requires an anonymous inner class:

```java

Predicate<Worker> isEmployeePredicate = new Predicate<Worker> () {
@Override
public boolean apply(Worker worker) {
return worker.isEmployee();
}
}
```

That's six lines of code, lots of indenting -- basically a lot of noise.  Funcito allows you to obtain an equivalent Predicate in just a single clear line:

```java

Predicate<Worker> isEmployeePredicate = predicateFor( callsTo(Worker.class).isEmployee() );
```

Similarly, Funcito can create a Guava Function to get the last names of all employees:

```java

Function<Worker,String> lastNamesFunc = functionFor( callsTo(Worker.class).getLastName() );
List<String> lastNames = Lists.transform(allWorkers, lastNamesFunc);
```

As you start using methods as Functions or Predicates, you will end up redefining them in multiple places.  And you may also find yourself functionally wrapping several properties from the same class.  Why not put related reusable Function/Predicate declarations together as static declarations in a new class?  Then you can statically import your Functions or Predicates at will.

```java

public class Workers {
private static final Worker CALLS_TO = callsTo(Worker.class); // a proxy stub for Function creation

public static final Function<Worker,Integer> GET_BADGE_NUM  = functionFor(CALLS_TO.getBadgeNum());
public static final Function<Worker,String>  GET_LAST_NAME  = functionFor(CALLS_TO.getLastName());
public static final Function<Worker,String>  GET_FIRST_NAME = functionFor(CALLS_TO.getFirstName());
public static final Predicate<Worker>        IS_EMPLOYEE    = predicateFor(CALLS_TO.isEmployee());
}
```
## Capabilites ##
### Supported Functional Programming Libraries ###
Funcito currently supports the following libraries that have functional programming capabilities:
  * [Google Guava](http://code.google.com/p/guava-libraries/) **`Function`** and **`Predicate`** interfaces
  * [Functional Java](http://functionaljava.org/) the **`F`** (function) interface and **Effect**<font color='red'>(new for 1.3!)</font>*** [Jedi-core](http://jedi.codehaus.org)**`Functor`**and**`Filter`**interfaces  and**Command <font color='red'>(new for 1.3!)</font>*** [Play! Framework 2](http://www.playframework.org/)**`F.Function`**interface and**Callback <font color='red'>(new for 1.3!)</font>*** [Collection-generic](https://github.com/megamattron/collections-generic)**`Transformer`**and**`Predicate`**interfaces and**Closure <font color='red'>(new for 1.3!)</font>*** [RxJava](https://github.com/Netflix/RxJava)**`Function1`**and**`Action1`**interfaces**<font color='red'>(all new for 1.3!)</font>

In future releases, we hope to add the following interfaces:
  * Plain access of Methods for those who want to work directly with Java Method objects, without using String-based reflection
  * J2SE zero-argument functional interfaces such as Runnable or Callable
  * Your favorite library with functional programming support that defines its functional-types as Java interfaces.  Contact us.

### Supported Code Generation Libraries ###
The Funcito syntax for generating the functional-type objects is enabled by the use of [code generation libraries](CodeGenerationAndProxies.md).  Any of your projects that use the Hibernate or Spring frameworks are already using one of the two supported code-generation frameworks.  You can use either one, as you prefer:
  * [CGLib](http://cglib.sourceforge.net/)
  * [Javassist](http://www.javassist.org/)

If you do not want to depend on external Code-gen libraries and don't mind being restricted to wrapping methods on interfaces only, Funcito will revert to using Java dynamic Proxies as an alternate "code-generation library".  Instructions on explicitly configuring a code proxying mechanism are found in ["Configuring a Proxy Provider"](CodeGenerationAndProxies#Configuring_a_Proxy_Provider.md) if you need to override Funcito's internal search and selection rules.

## License ##
**Funcito** is released under the [Apache 2.0 open source license](http://www.apache.org/licenses/LICENSE-2.0.html).

## Continuous Integration ##
Continuous Integration services are generously provided in the cloud by [CloudBees](http://www.cloudbees.com/) through their free FOSS program.  You may find our nightly builds, including unit test coverage reports on [our CI Server page](https://funcito.ci.cloudbees.com/) at their site.

[![](http://web-static-cloudfront.s3.amazonaws.com/images/badges/BuiltOnDEV.png)](http://www.cloudbees.com/)

# **[Your Support of Funcito is Welcome](Support.md)** #