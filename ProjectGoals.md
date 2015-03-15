# Project Goals for Funcito #

## Motivation ##
I was starting to use Guava Functions and Predicates a little more seriously on a project I was on, and I noticed that I kept having to write boilerplate anonymous inner classes to create each Function or Predicate, even though many of them were doing nothing more than delegating to an existing method.  This seemed wasteful, especially in light of the Guava goal to be a "productivity multiplier".  The Guava team has even gone on record warning of the potential for Guava's functional idioms to lead to [more verbose or awkward code.](http://code.google.com/p/guava-libraries/wiki/FunctionalExplained#Caveats)

I decided to find a solution to simplify Guava Function and Predicate creation in the simplest cases.  Every solution has its tradeoffs, and as I explored the options that were available, I chose the following criteria.

## Goals ##
  * Reduce boilerplate noise in creating functional-objects
  * Limited scope: wrap existing methods as functional-objects
  * Support several Java functional programming (FP) frameworks
  * Extensible to additional FP frameworks
  * Concise and easy to read API
  * API must be safe for common refactoring capabilities
  * Small jar size and minimal dependencies other than FP library of choice
  * Simple drop-in usage, no pre-compile APT annotation processing steps
  * Minimal overhead in class space, memory and CPU.
  * Avoid forcing users into [obscure or controversial code techniques.](http://stackoverflow.com/questions/924285/efficiency-of-java-double-brace-initialization)