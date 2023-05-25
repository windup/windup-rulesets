Windup Rulesets
===============

Rules and rulesets contributed by community.

Follow the [Get Involved](https://github.com/windup/windup/wiki/Get-Involved) in Windup wiki.


Directory structure
-----------

The `rules` module repository is organized in subdirectories:

* `rules-reviewed`: this is where you can make contribution and add new rules 
* `rules-generated`: these rules are automatically generated (from the Quarkus main git repo) and you should not add new rules here

Testing the rules
-----------

To limit the XML-based tests (`windup.test.xml`) which are run, use `-DrunTestsMatching=...`. 
The value can be any part of the test filename to match. Like, `-DrunTestsMatching=hsearch`.


Building from source
-----------

Ensure you use the provided Maven Wrapper utility to build the project, as Windup requires Maven 3.8.8:

        ./mvnw verify