package camel2.org.apache.camel.itest.doc;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CatalogCamelContext49Test extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testDocumentation() throws Exception {
        CamelContext context = new DefaultCamelContext();
        String json = context.getEipParameterJsonSchema("from");
        log.info(json);
        assertNotNull("Should have found json for from", json);

        assertTrue(json.contains("\"name\": \"from\""));
        assertTrue(json.contains("\"uri\": { \"kind\": \"attribute\""));
        assertTrue(json.contains("\"ref\": { \"kind\": \"attribute\""));
    }

    @Test
    public void testLanguageJsonSchema() throws Exception {
        CamelContext context = new DefaultCamelContext();
        String json = context.getLanguageParameterJsonSchema("groovy");
        assertNotNull("Should have found some auto-generated JSON", json);
        log.info(json);

        assertTrue(json.contains("\"name\": \"groovy\""));
        assertTrue(json.contains("\"modelName\": \"groovy\""));
    }

    @Test
    public void testDataFormatJsonSchema() throws Exception {
        CamelContext context = new DefaultCamelContext();
        String json = context.getDataFormatParameterJsonSchema("string");
        assertNotNull("Should have found some auto-generated JSON", json);
        log.info(json);

        assertTrue(json.contains("\"name\": \"string\""));
        assertTrue(json.contains("\"modelName\": \"string\""));
        assertTrue(json.contains("\"charset\": { \"kind\": \"attribute\", \"displayName\": \"Charset\", \"required\": false, \"type\": \"string\""));
    }

    @Test
    public void testComponentJsonSchema() throws Exception {
        CamelContext context = new DefaultCamelContext();
        String json = context.getComponentParameterJsonSchema("direct");
        assertNotNull("Should have found some auto-generated JSON", json);
    }

}
