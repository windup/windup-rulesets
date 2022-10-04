package camel2;
import org.apache.camel.language.simple.SimpleLanguage;
import org.apache.camel.LanguageTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class SimpleChangeFunctionTokensTest extends LanguageTestSupport {

    @Override
    protected String getLanguageName() {
        return "simple";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        SimpleLanguage.changeFunctionStartToken("[[");
        SimpleLanguage.changeFunctionEndToken("]]");
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();

        // replace old tokens
        SimpleLanguage.changeFunctionStartToken("${", "$simple{");
        SimpleLanguage.changeFunctionEndToken("}");
    }

    @Test
    public void testSimpleBody() throws Exception {
        assertExpression(exchange, "[[body]]", "<hello id='m123'>world!</hello>");

        // old tokens do no longer work
        assertExpression(exchange, "${body}", "${body}");
    }

    @Test
    public void testSimpleConstantAndBody() throws Exception {
        exchange.getIn().setBody("Camel");
        assertExpression(exchange, "Hi [[body]] how are you", "Hi Camel how are you");
        assertExpression(exchange, "'Hi '[[body]]' how are you'", "'Hi 'Camel' how are you'");

        // old tokens do no longer work
        assertExpression(exchange, "Hi ${body} how are you", "Hi ${body} how are you");
    }

    @Test
    public void testSimpleConstantAndBodyAndHeader() throws Exception {
        exchange.getIn().setBody("Camel");
        exchange.getIn().setHeader("foo", "Tiger");
        assertExpression(exchange, "Hi [[body]] how are [[header.foo]]", "Hi Camel how are Tiger");
    }

    @Test
    public void testSimpleEqOperator() throws Exception {
        exchange.getIn().setBody("Camel");
        assertPredicate(exchange, "[[body]] == 'Tiger'", false);
        assertPredicate(exchange, "[[body]] == 'Camel'", true);
        assertPredicate(exchange, "[[body]] == \"Tiger\"", false);
        assertPredicate(exchange, "[[body]] == \"Camel\"", true);
    }

}
