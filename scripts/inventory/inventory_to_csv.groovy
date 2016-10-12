// This script requires that you install Groovy. 
// It creates a rules report CSV file.
// Run it from the root of the windup-rulesets directory:
//     groovy scripts/inventory/inventory_to_csv.groovy
@Grab(group = 'net.sf.opencsv', module = 'opencsv', version = '2.3')
import au.com.bytecode.opencsv.*

def rules = new XmlSlurper().parse('inventory_all_rules.xml');

String printAllElements(groovy.util.slurpersupport.NodeChild element) {
    String result = ""
    if (element.children().size() > 0) {
        element.children().each{ child ->
            result += "[" +child.name()

            if (child.attributes().size() > 0) {
                result += " ->Attributes:[" + child.attributes().toString() + "]";
            }

            if(child.children().size() > 0) {
                //recursive call
                def nested = printAllElements(child);
                if (nested != null)
                    result += " Nested elements:[" + printAllElements(child) +"]"
            } else {
                if (child.text() != null && child.text().length() > 0)
                    result += " = " + child.text()+"]"
            }
        }
    }
    return result
}

maxLocationCount = 0;

rules.rule.each { rule ->
    locationCount = rule.column.findAll { it.@name == "location" }.size();
    if (locationCount > maxLocationCount)
        maxLocationCount = locationCount;
}

// make the header
List header = new ArrayList();
header.addAll("Rule ID", "Quickfix", "Condition Type", "Condition xml");

for (int i = 0; i < maxLocationCount; i++)
    header.add("Location");

header.addAll("Classification", "Hint Title", "Hint");


Writer writer = new StringWriter()
// make a CSV writer that writes to a string
//char delimiter = ';'
def w = new CSVWriter(writer)//,delimiter)
w.writeNext(header.toArray(new String[0]));

for (rule in rules.rule) {
    List columns = new ArrayList();

    columns.add(rule.column.find { it.@name == "id" }.toString());
    columns.add(rule.column.find { it.@name == "quickfix" }.toString());
    
    // type of condition
    columns.add(rule.column.find { it.@name == "condition" }.toString());

    // printing condition_content column as text from xml fragment
    groovy.util.slurpersupport.FilteredNodeChildren condition_columns = rule.column.findAll { column -> column.@name == "condition-content" }
    String strCondition = "";
    condition_columns.each {
        strCondition += printAllElements(it)
    }
    columns.add(strCondition)

    // adding all locations as columns
    locationCount = rule.column.findAll { it.@name == "location" }.size();
    locations = rule.column.findAll {it.@name == "location" }
    locations.each { location ->
        columns.add(location.text());
    }

    // add max count of header columns for locations
    if (maxLocationCount > locationCount) {
       for (int i = locationCount - 1; i < maxLocationCount-1; i++)
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

