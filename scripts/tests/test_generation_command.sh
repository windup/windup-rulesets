#!/bin/bash

# Just an example, modify to suit your environment
java -jar ~/javadevtools/saxon/saxon9he.jar \
 -s:./rules-reviewed/technology-usage/3rd-party.windup.xml \
 -xsl:./scripts/tests/create-tests.xslt \
 testFileStub=rules-reviewed/eap7/tests/data/embedded-framework-libraries/jboss-seam-2.jar \
 outputDirectory=./target/testdata  \
 ruleIDPattern=.* \
  -o:test.xml

