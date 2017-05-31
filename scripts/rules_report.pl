#!/usr/bin/perl -w

# This script creates a rules report HTML file.
# Run it from the root of the windup-rulesets directory:
#     scripts/rules_report.pl
#

use strict;
use warnings;
use File::Basename;

my @xmlRules = `find rules-reviewed/ -name "*.windup.xml"`;

unlink('scripts/output-data/all_rules.html');

`echo '<html><head><title>Rules Report</title></head><body>' > scripts/output-data/all_rules.html`;

for my $xmlRule (@xmlRules) {
	chomp $xmlRule;
    my $filename = fileparse($xmlRule);
	print `xsltproc --timing --stringparam filename $xmlRule scripts/rules_report.xslt $xmlRule >> scripts/output-data/all_rules.html\n`;
}

`echo '</body></html>' >> scripts/output-data/all_rules.html`;
