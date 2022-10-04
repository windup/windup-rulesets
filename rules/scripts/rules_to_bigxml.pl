#!/usr/bin/perl -w
# This script creates a rules report XML file.
# Run it from the root of the windup-rulesets directory:
#     scripts/rules_to_bigxml.pl
#

use strict;
use warnings;

my @xmlRules = `find rules-reviewed/ -name "*.windup.xml"`;

unlink('scripts/output-data/all_rules.xml');

`echo '<root>' > scripts/output-data/all_rules.xml`;

for my $xmlRule (@xmlRules) {
	chomp $xmlRule;
	print `xsltproc --timing scripts/rules_to_bigxml.xslt $xmlRule >> scripts/output-data/all_rules.xml\n`;
}

`echo '</root>' >> scripts/output-data/all_rules.xml`;

`sed -i -e 's#<ROOT xmlns="http://windup.jboss.org/v1/xml">##g' scripts/output-data/all_rules.xml`;
`sed -i -e 's#</ROOT>##g' scripts/output-data/all_rules.xml`;
