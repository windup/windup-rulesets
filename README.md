Windup Rulesets
===============

Rules and rulesets contributed by community.

Follow the [Get Involved](https://github.com/windup/windup/wiki/Get-Involved) in Windup wiki.


Directory structure
-----------

The `rules` module repository is organized in subdirectories:

* `rules-reviewed`: this is where you can make contribution and add new rules 
* `rules-generated`: these rules are automatically generated (from the Quarkus main git repo) and you should not add new rules here
* `rules-overridden-azure`: this is where you can override existing rules (mostly the ones under `rules-reviewed`)

Testing the rules
-----------

To limit the XML-based tests (`windup.test.xml`) which are run, use `-DrunTestsMatching=...`. 
The value can be any part of the test filename to match. Like, `-DrunTestsMatching=hsearch`.

By default, when executing the tests, the `href` attributes of the `<link>` tags are also tested. This means that there is an external request to each URL. 
If you want to avoid this, you can use the `-Dtest=WindupRulesTest` flag in conjunction with the `-f` flag:

```
mvn clean test -f rules -Dtest=WindupRulesTest -DrunTestsMatching=hsearch
```

Building from source
-----------

Ensure you use the provided Maven Wrapper utility to build the project, as Windup requires Maven 3.8.8:

        ./mvnw verify
