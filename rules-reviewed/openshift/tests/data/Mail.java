import java.util.Properties;
import java.util.Date;
import javax.mail.MimeMessage;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;

public class Mail {
	
	private class MyMessagingException extends MessagingException{}
	
	public static void main(String argv[]) {
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "my-mail-server");
	    Session session = Session.getInstance(props, null);

	    try {
	        MimeMessage msg = new MimeMessage(session);
	        msg.setFrom("me@example.com");
	        msg.setRecipients(Message.RecipientType.TO, "you@example.com");
	        msg.setSubject("JavaMail hello world example");
	        msg.setSentDate(new Date());
	        msg.setText("Hello, world!\n");
	        Transport.send(msg, "me@example.com", "my-password");
	    } catch (MyMessagingException mex) {
	        System.out.println("send failed, exception: " + mex);
	    }
	}
}