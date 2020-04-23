package camel2.org.apache.camel.loanbroker;

import org.apache.camel.spring.Main;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class to run the loan broker server from command line.
 */
public final class LoanBroker {

    private LoanBroker() {
    }

    public static void main(String... args) throws Exception {
        // create a new main which will boot the Spring XML file
        Main main = new Main();
        main.setApplicationContext(new ClassPathXmlApplicationContext("META-INF/spring/webServiceCamelContext.xml"));
        main.run();
    }

}
