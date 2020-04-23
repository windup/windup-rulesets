import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class Attacher implements Processor {

    private final String mimetype;

    public AttachmentAttacher(String mimetype) {
        this.mimetype = mimetype;
    }

    @Override
    public void process(Exchange exchange){
        Message in = exchange.getIn();
        byte[] file = in.getBody(byte[].class);
        String fileId = in.getHeader("CamelFileName",String.class);
        in.addAttachment(fileId, new DataHandler(new     ByteArrayDataSource(file, mimetype)));
    }
}
