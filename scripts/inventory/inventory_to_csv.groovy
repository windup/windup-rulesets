// This script requires that you install Groovy. 
// It creates a rules report CSV file.
// Run it from the root of the windup-rulesets directory:
//     groovy scripts/inventory/inventory_to_csv.groovy
@Grab(group = 'net.sf.opencsv', module = 'opencsv', version = '2.3')
import au.com.bytecode.opencsv.*

def rules = new XmlSlurper().parse('all_rules.xml');

maxLocationCount = 0;

rules.rule.each { rule ->
    locationCount = rule.column.findAll { it.@name == "location" }.size();
    if (locationCount > maxLocationCount)
        maxLocationCount = locationCount;
}

// make the header
List header = new ArrayList();
header.addAll("Rule ID", "Condition Type", "Condition xml");

for (int i = 0; i < maxLocationCount; i++)
    header.add("Location");

header.addAll("Classification", "Hint Title", "Hint");


Writer writer = new StringWriter()
// make a CSV writer that writes to a string
def w = new CSVWriter(writer)
w.writeNext(header.toArray(new String[0]));

for (rule in rules.rule) {
    List columns = new ArrayList();

    columns.add(rule.column.find { it.@name == "id" }.toString());
    columns.add(rule.column.find { it.@name == "condition" }.toString());

    def xml_condition = rule.column.find { it.@name == "condition-content" };
    String condition_content;
    xml_condition.children().each { condition_content = it.name() + it.attributes().toString()}
    columns.add(condition_content);

    // adding all locations
    locationCount = rule.column.findAll { it.@name == "location" }.size();
    locations = rule.column.findAll {it.@name == "location" }
    locations.each { location ->
        columns.add(location.text());
    }

    // add max count of header columns for locations
    if (maxLocationCount > locationCount) {
       for (int i = locationCount - 1; i < maxLocationCount; i++)
           columns.add("");
    }

    columns.add(rule.column.find { it.@name == "classification" }.toString());
    columns.add(rule.column.find { it.@name == "hint-title" }.toString());
    columns.add(rule.column.find { it.@name == "hint-message" }.toString());

    String[] row = columns.toArray(new String[0]);
    w.writeNext(row);
}

writer.close()

println writer.toString()

