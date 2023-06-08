Azure Rules overriding Windup Rules
===============

In this `rules-overridden-azure` directory, you will find the rules that are overriding the Windup rules (mostly the ones under `rules-reviewed`).

Directory structure
-----------

The `rules-overridden-azure` directory mimics the `rules-reviewed` directory structure.
This means that you will find the same subdirectories (`azure`, `camel`, `droolsjbpm`...) and the same files when a rule needs to be overriden.

Overriding an existing rule
-----------

Rules written in XML or Groovy can be overridden either in XML or Groovy.
It doesn't matter if the original rule is written in XML or Groovy, you can override it in either language.

Testing an overriden rule
-----------

To limit the XML-based tests (`windup.test.xml`) which are run, use `-DrunTestsMatching=...`. 
The value can be any part of the test filename to match. Like, `-DrunTestsMatching=hsearch`.
