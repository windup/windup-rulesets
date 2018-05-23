#!/bin/bash

usage()
{
  echo
  echo "Usage:"
  echo " test_generation_command.sh <path to new rules folder> <path to Saxon HE jar file>"
  echo
  exit 1
}

if [ $# -ne 2 ] ; then
    usage
fi
RULES_BASEDIR=$1
SAXONHE_PATH=$2
for rulesetfilepath in $(ls $RULES_BASEDIR*.{windup,rhamt}.xml 2>/dev/null); do
 if [ ! -f $RULES_BASEDIR"tests/$(basename -- "$rulesetfilepath" .xml).test.xml" ]; then
# Just an example, modify to suit your environment
java -jar $SAXONHE_PATH \
 -s:$rulesetfilepath \
 -xsl:./scripts/tests/create-tests.xslt \
 testFileStub=rules-reviewed/eap7/tests/data/embedded-framework-libraries/jboss-seam-2.jar \
 outputDirectory="$RULES_BASEDIR"tests \
 ruleIDPattern=.* \
 -o:"$RULES_BASEDIR"tests/$(basename -- "$rulesetfilepath" .xml).test.xml
 fi
done

