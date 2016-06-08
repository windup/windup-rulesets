echo "Run this script from the root of the windup-rulesets directory: "
echo "    scritps/buildRulesReports.sh                                "
echo "                                                                "
echo "It executes other scripts to report on the rules using the      "
echo "rules metadata:                                                 "
echo "    scripts/rules_report.pl: This generates an HTML report file "   
echo "    scripts/rules_to_bigxml.pl: This generates an XML file      "   
echo "    scripts/bigxml_to_csv.groovy: This script requires that you "
echo "           install Groovy. It generates a CSV file from the     "
echo "           generated XML file, which can later be imported into "
echo "           a spreadsheet for analysis                           "
echo ""
scripts/rules_report.pl
scripts/rules_to_bigxml.pl 
groovy scripts/bigxml_to_csv.groovy >> scripts/output-data/all_rules.csv
echo ""
echo "The report files an be reviewed in the output-data directory: "
echo "    scripts/output-data/all_rules.html: HTML report file      "
echo "    scripts/output-data/all_rules.XML : XML report file       "
echo "    scripts/output-data/all_rules.csv : CSV report file       "
echo ""

