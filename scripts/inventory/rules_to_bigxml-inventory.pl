#!/usr/bin/perl -w
# This script creates a rules report XML file.
# Run it from the root of the windup-rulesets directory:
#     scripts/rules_to_bigxml.pl
#

use strict;
use warnings;

my @xmlRules = `find rules-reviewed/ -name "*.windup.xml"`;

unlink('all_rules.xml');

`echo '<root>' > all_rules.xml`;

for my $xmlRule (@xmlRules) {
	chomp $xmlRule;
	print `xsltproc --timing scripts/inventory/rules_to_bigxml-inventory.xslt $xmlRule >> all_rules.xml\n`;
}

`echo '</root>' >> all_rules.xml`;