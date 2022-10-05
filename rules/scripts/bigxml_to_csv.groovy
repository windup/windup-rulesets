// This script requires that you install Groovy. 
// It creates a rules report CSV file.
// Run it from the root of the windup-rulesets directory:
//     groovy scripts/bigxml_to_csv.groovy
@Grab(group = 'net.sf.opencsv', module = 'opencsv', version = '2.3')
import au.com.bytecode.opencsv.*

def rules = new XmlSlurper().parse('scripts/output-data/all_rules.xml');

maxLinkCount = 0;
maxContentsCount = 0;

rules.rule.each { rule ->
    linkCount = rule.column.findAll { it.@name == "link" }.size();
    if (linkCount > maxLinkCount)
        maxLinkCount = linkCount;
}

rules.rule.each { rule ->
    titleCount = rule.column.findAll { it.@name == "title" }.size();
    if (titleCount > maxContentsCount)
        maxContentsCount = titleCount;
}

//println("Max Links: " + maxLinkCount);
//println("Max Contents: " + maxContentsCount);


// make a List of String[]'s
List header = new ArrayList();
header.add("Rule ID");
for (int i = 0; i < maxLinkCount; i++)
    header.add("Link");

for (int i = 0; i < maxContentsCount; i++) {
    header.add("Title");
    header.add("Body");
}

Writer writer = new StringWriter()
// make a CSV writer that writes to a string
def w = new CSVWriter(writer)
//w.writeNext((String[]) ['Some Things', 'Some Stuff', 'Other Junk'])
w.writeNext(header.toArray(new String[0]));

rules.rule.each { rule ->
    List columns = new ArrayList();

    columns.add(rule.column.find {it.@name = 'id'}.toString());

    rule.column.findAll {it.@name == 'link'}.each{columns.add(it.toString())};

    def linkCount = rule.column.findAll {it.@name == 'link'}.size();
    if (linkCount < maxLinkCount) {
        for (def i = linkCount; i < maxLinkCount; i++)
            columns.add("");
    }

    def titleCount = rule.column.findAll { it.@name == "title"}.size();
    rule.column.findAll { it.@name == "title" || it.@name == "contents"}.each{
        columns.add(it.toString());
    };

    if (titleCount < maxContentsCount) {
        for (def i = titleCount; i < maxContentsCount; i++)
            columns.add("");
    }

    String[] row = columns.toArray(new String[0]);
    w.writeNext(row);
}

//for each booking
//bookings.each { booking ->
    // munge the data into a String[]
    //String[] line = booking
    //w.writeNext(line)

    // if more rooms for the booking (e.g. array within current list)
    //if (line[3] == '97') {
        //line = ['Another Name', '', '', '']
        //w.writeNext(line)
        //line = ['Yet Another Name', '', '', '']
        //w.writeNext(line)
    //}
//}
writer.close()

println writer.toString()

