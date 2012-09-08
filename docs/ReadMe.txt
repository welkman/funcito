Funcito

Version History:

1.2    08-Sep-2012  Support for Play! Framework 2 F.Function
                    Support for collections-generic (generified fork of Jakarta commons-collections) Transformer and Predicate
                    Upgraded to Gradle version 1.1
                    Added Gradle Wrapper scripts (can use "gradlew" instead of "gradle" for build commands)
                    Added support for IntelliJ project import
                    Removed org.funcito.examples from javadoc

1.1    23-Jul-2012  Support for chained method calls
                    Permit wrapping of methods with parameters (static binding of param values)
                    Warning messages now get logged with JUL rather than going to System.err
                    Vastly Improved diagnostic messages in runtime Exceptions
                    Internal functional-type classes renamed for the parent API in anticipation of name collisions
                        as future APIs are added.  MethodFunction->GuavaFunction, MethodF->FjF, etc.
                    JAR file no longer has empty Objenesis dir (workaround for JarJar bug)
                    Introduced development microbenchmarks, to improve performance while adding features
                    Increased unit test coverage
                    More complete example code across all current APIs (in funcito-src.jar)

1.0.2  02-Apr-2012  No API changes.
                    Removed runtime dependency on Google Guava for non-Guava users
                    Improved POM file to reflect runtime dependencies more accurately

1.0.1  04-Feb-2012  Mavenized project (including splitting src/doc jars), no functional or code changes.

1.0    31-Jan-2012  Original Version
