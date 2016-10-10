#!/usr/bin/perl -w
# This script creates a rules report XML file.
# Run it from the root of the windup-rulesets directory:
#     scripts/inventory/inventory-rules_to_bigxml.pl
#

use strict;
use warnings;

my @xmlRules = `find rules-reviewed/ -name "*.windup.xml"`;

unlink('inventory_all_rules.xml');

`echo '<root>' > inventory_all_rules.xml`;

for my $xmlRule (@xmlRules) {
	chomp $xmlRule;
	print `xsltproc --timing scripts/inventory/inventory-rules_to_bigxml.xslt $xmlRule >> inventory_all_rules.xml\n`;
}

`echo '</root>' >> inventory_all_rules.xml`;