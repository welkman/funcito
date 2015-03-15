## Frequently Asked Questions ##
  * [What is Funcito?](FAQ#What_is_Funcito?.md)
  * [What do you mean by 'functional objects'?](FAQ#What_do_you_mean_by_%27functional_objects%27_?.md)
  * [Why is it named Funcito?](FAQ#Why_is_it_named_Funcito?.md)
  * [What are the limitations of Funcito?](FAQ#What_are_the_limitations_of_Funcito?.md)
  * [Do you wrap methods on interfaces or classes?](FAQ#Do_you_wrap_methods_on_interfaces_or_classes?.md)
  * [Can I wrap private methods?](FAQ#Can_I_wrap_private_methods?.md)
  * [Do Funcito and non-Funcito created functional objects play together?](FAQ#Do_Funcito_and_non-Funcito_created_functional_objects_play_toget.md)
  * [Is Funcito thread-safe?](FAQ#Is_Funcito_thread-safe?.md)
  * [What do you consider to be "ordinary usage"?](FAQ#What_do_you_consider_to_be_%22ordinary_usage%22?.md)
  * [Why am I getting a FuncitoException?](FAQ#Why_am_I_getting_a_?.md)
  * [Can I wrap chained method calls?](FAQ#Can_I_wrap_chained_method_calls?.md)
  * [Can I wrap expressions that include operators?](FAQ#Can_I_wrap_expressions_that_include_operators?.md)
  * [What about Java 8 and Project Lambda?](FAQ#What_about_Java_8_and_Project_Lambda?.md)
  * [What are the dependencies for the project?](FAQ#What_are_the_dependencies_for_the_project?.md)
  * [What is the performance of Funcito compared to hand-coding?](FAQ#What_is_the_performance_of_Funcito_compared_to_hand-coding?.md)
  * [Can I have xxxx feature?](FAQ#Can_I_have_xxxx_feature?.md)
  * [Why doesn't this FAQ have answers to my questions?](FAQ#Why_doesn't_this_FAQ_have_answers_to_my_questions?.md)
#### What is Funcito? ####

**_Funcito_** is a framework for simplifying the creation of functional objects used in other functional libraries. Specifically, it aides in creating functional objects which wrap individual methods of your classes.

#### What do you mean by _'functional objects'_? ####

Functional objects implement the units of functional programming in several Java functional frameworks.  Funcito only supports functional frameworks that define functional objects in terms of interfaces or abstract classes.  Examples include `Function` and `Predicate` interfaces in Google Guava, `Functor`, `Filter`, and `Command` interfaces in Jedi-core, and the `F` and `Effect` abstract classes of Functional Java.

#### Why is it named Funcito? ####

The name Funcito (pronounced "_fun KEE toe_") is firstly supposed to indicate that it aides in functional programming.  Secondly, the underlying technology was inspired by the popular mocking framework, [Mockito](http://mockito.org) (check it out!), and a bit of code was even borrowed.  Lastly, Funcito vaguely sounds like "burrito" (use your imagination), which is itself a wrapper to hold your favorite delicious ingredients in a compact, tidy package.  In like fashion, Funcito takes _your_ favorite programming ingredients (your methods), and wraps them cleanly and simply, to help keep your code from getting messy.

#### What are the limitations of Funcito? ####

  * Needs a java 1.5+ JVM that is [supported by Objenesis](http://code.google.com/p/objenesis/wiki/ListOfCurrentlySupportedVMs). Don't worry, most 1.5+ JVMs should work just fine.
  * Cannot wrap methods on final classes**<sup>*</sup>** (this may be relaxed in a future release)
  * Cannot wrap final methods.
  * ~~Can only wrap methods with non-void return types~~ <font color='red'>New in 1.3: Command-functors wrap voids</font>
  * Cannot wrap static methods (may consider in future release)

**<sup>*</sup>** Funcito can create function-type objects on methods of final classes if the method is defined in a base class or interface.  You just currently need to define your function in terms of that interface or base class rather than the final class (e.g. CharacterSequence instead of String).

#### Do you wrap methods on interfaces or classes? ####

Both, including abstract classes.  What you choose depends on your needs.  The functional-libraries supported by Funcito define their functional-types with Java generics (e.g., `Function<YourClass, String>`).  The rules of type compatibility with generics can be tricky.  If you can avoid type-casting of your functional-objects, programming will be simpler for you.  But all ordinary rules of Java generic-type bounding are available as needed.

#### Can I wrap private methods? ####

Yes, but the Funcito wrapping-code must exist within the private context in order to compile.  The resulting functional-object _may_ be passed out of that context and used in contexts of greater visibility, but that is a questionable practice which should probably be avoided.  If a Method is defined private in the first place, why would you then  make the method more visible as a functional-object?

#### Do Funcito and non-Funcito created functional objects play together? ####

Yes, for instance you may mix and match Funcito-created Guava Functions with those that are created conventionally as anonymous inner classes.

#### Is Funcito thread-safe? ####

There are two phases of Funcito code execution: construction-phase and use-phase.  The use-phase is when you use those objects in the ordinary fashion for their defined framework.  The use-phase is 100% as thread-safe as the method that is being wrapped.

The construction-phase is when the Funcito API is used to create functional objects.  We have tried to ensure that "ordinary usage" of the construction phase should be free of threading issues.  If you see a threading problem in the code or experience a bug please let us know.  But, we have reason to believe this is unlikely, especially under conditions of "ordinary usage".

#### What do you consider to be "ordinary usage"? ####

Since methods themselves have "static" visibility, it would be unusual to expose them as non-static objects.  The recommended practice for functional object reuse is to declare and define your Funcito-created functional objects as static, though this is not strictly necessary.

#### Why am I getting a FuncitoException? ####

Funcito attempts to validate that you are using it correctly.  Also, there needed to be a common way to rethrow any potential problems as RuntimeErrors.  If you are getting a FuncitoException, try to follow the advice in the message of the exception.

#### Can I wrap chained method calls? ####

```
  Function<MyClass,Integer> func = functionFor(callsTo(MyClass.class).aCharSequenceReturnMethod().length());
```

Version 1.1 forward supports the above, with certain limitations, described in more detail in [Usage Notes](UsageNotes.md) (under construction).

#### Can I wrap expressions that include operators? ####

```
  Function<MyClass,String> plural = functionFor(callsTo(MyClass.class).someStringMethod() + "s");
```

No.  The above will compile and execute, AND unfortunately you will not get the expected result.  This kind of expression cannot be supported using the mechanisms that make Funcito's syntax possible.  Some functional frameworks include construction techniques that permit more flexible functional object construction that would support the above, but they bring in other "baggage" that we didn't want to have.  We do hope to increase the flexibility of Funcito in the future, but Funcito's syntax precludes the ability to use operators in expressions, as in the example above.

#### What about Java 8 and Project Lambda? ####

Java 8 promises to include some great features which should provide a much more straightforward route to functional programming in Java than the functional libraries in existence, and the syntax advancements will deprecate any need for function builders such as Funcito.  However, Java 8 is not scheduled for release until March 2014, the schedule could likely slip, and most active projects -- both new and legacy -- will probably not have the luxury of adopting Java 8 for months or years to follow.  Furthermore, with the addition of Modes such as Safe-Navigation, Funcito makes it easier to create advanced "lambdas" with trivial syntax.  In short, Funcito will have utility in augmenting existing functional libraries for years to come.

#### What are the dependencies for the project? ####

It depends (ba-dum, ching).  If you do not mind being restricted to wrapping methods on interfaces only, no external code-generation dependency is required.  If you also want to wrap methods on abstract and concrete classes, you need to choose **one** of the supported code generation libraries, either CGLIB (the standalone "-nodep" version, or else you will also need ASM) or Javassist, and include it in your classpath.  If you are using Spring or Hibernate you likely already have one or both of those available.  Our testing so far has been with:
  * cglib-nodep, version 2.2
  * javassist, version 3.14.0-GA

Also, more obviously, you need to choose which supported functional framework(s) you want to use.  You do not need to include jar files for all of the supported functional frameworks, but only for the ones which you are using.

Other than the choices that you make, there are no external dependencies. **NOTE:** Releases prior to 1.0.2 accidentally contained a runtime dependency on Guava (see [Issue 50](https://code.google.com/p/funcito/issues/detail?id=50)), which is fixed in current versions.

#### What is the performance of Funcito compared to hand-coding? ####

Functional objects are likely to be created once and reused many times, so all of the performance criteria in the design of Funcito is focused on keeping the execution overhead to the bare minimum feasible for the most common use cases.  Since there are so many variables that go into determining the "cost" of this overhead, there is no single answer to this question.  However it is pretty safe to say that for unchained method calls operating in a modern JVM on a modern CPU, the overhead is only likely to be in the nanoseconds per invocation.

The Funcito project produces and maintains a set of rudimentary internal benchmarks for every release (on a very old architecture CPU) to ensure that performance does not decrease with subsequent releases.

Using certain advanced features of Funcito such as method chaining or alternative Modes of operation such as safe-navigation necessarily have additional impacts on performance.

#### Can I have xxxx feature? ####
Please [send us](http://groups.google.com/group/funcito) an example where you need something more than Funcito currently offers.

#### Why doesn't this FAQ have answers to my questions? ####

Do you have any suggestions? Write to our [mailing list](http://groups.google.com/group/funcito).