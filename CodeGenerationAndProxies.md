# Generating Function-objects through Class Proxying #

## The Magic Behind Funcito ##

What is happening behind the curtain in Funcito when you make a call like this?

```java

import static org.funcito.FuncitoGuava.*;
Function<MyClass,String> func = functionFor(callsTo(MyClass.class).myStringMethod());```

The "magic" is something you probably regularly use, if you utilize Spring, Hibernate, or certain unit test mock-object frameworks like Mockito.  It is class-proxying through code-generation. Invoking `callsTo(MyClass.class)` first creates a proxy-class to stand in for `MyClass`, and then it returns an instance of that proxy.  So when `myStringMethod()` is called at the tail of that expression, it is not the actual method that is being invoked, but rather a proxy handler  which simply registers that this method is about to be wrapped in a functional-object (in this case, a Guava `Function`).

Since the next thing to execute in this thread is the enclosing call to `functionFor()`, Funcito safely assumes that the last method registered is the one that you want to wrap.  Funcito creates a Function object with an action-method (`Function.apply()` to satisfy the Guava Function contract) that invokes the method using Java Reflection.

Class-proxying is used only in the construction of the `Function` object, but not in its execution.  This means the resulting `Function` is relatively efficient.  Use of the Reflection API `Method.invoke()` incurs some nominal overhead, but the `Method` handle to invoke is resolved once at construction time, rather than at every time the functional-object is invoked.

## Proxy Providers ##

Class proxying is ordinarily provided by one of two very popular code-generation libraries: CGLib or Javassist.  Chances are that your project is already using one of these libraries if you are using Spring or Hibernate, or one of several other common frameworks.  If either one is already included in your classpath, you are good to go.  If they are both in your classpath, Funcito chooses CGLib by default over Javassist.  But this can be configured to prefer Javassist as explained later.

If neither library is present, you can choose to add it to your classpath.  But if you would rather not, Funcito will revert back to the more limited capabilities of the J2SE dynamic `Proxy` as a provider, which can generate proxies only for Java interfaces.  You can also [configure Funcito](CodeGenerationAndProxies#Configuring_a_Proxy_Provider.md) to force it to use the J2SE dynamic `Proxy` provider even if one of the code-generation libraries is present.

## Proxy Provider Capabilities and Limitations ##

The J2SE dynamic `Proxy` provider is limited to proxying only interface-types.  Both CGLib and Javassist providers allow you to proxy not only interfaces but also abstract and concrete classes that are not `final`.  Attempts to proxy classes with the dynamic `Proxy` provider or to proxy final classes with the other providers will result in a run-time `FuncitoException`.  As described earlier, invoking a method on a Funcito proxy registers that method handle as an object to be wrapped.  Attempts to invoke/register a **final** method on a proxy produced by CGLib or Javassist will result in a `FuncitoException` at run-time.

## Configuring a Proxy Provider ##

The automated rules for selecting a proxy provider are described in [Proxy Providers](CodeGenerationAndProxies#Proxy_Providers.md).  However, you can override these rules to force Funcito to use an explicitly configured provider.  To do so, you need to set the value of a Java system property **funcito.proxy.provider** to one of the following values:
  * CGLIB
  * JAVASSIST
  * JAVAPROXY

A typical invocation to force the use of Javassist when both Javassist and CGLib are available might look something like:

```
java -Dfuncito.proxy.provider=JAVASSIST MyMainClass
```

Note that when the system property is defined, Funcito is forced to try and use only that provider.  If the configured provider is not present, proxying will fail with a run-time `FuncitoException`.