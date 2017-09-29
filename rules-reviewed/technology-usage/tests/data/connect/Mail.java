import java.util.Properties;
import java.util.Date;
import javax.mail.MimeMessage; //1
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import com.sun.mail.smtp.SMTPMessage;
import org.simplejavamail.email.Email;
import org.apache.commons.mail.Email; // 7

public class Mail {
	
	private class MyMessagingException extends MessagingException{} // 8
	
	public static void main(String argv[]) {
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "my-mail-server");
	    Session session = Session.getInstance(props, null); // 9, 10

	    try {
	        MimeMessage msg = new MimeMessage(session); // 11, 12,
	        msg.setFrom("me@example.com");
	        msg.setRecipients(Message.RecipientType.TO, "you@example.com"); // 13
	        msg.setSubject("JavaMail hello world example");
	        msg.setSentDate(new Date());
	        msg.setText("Hello, world!\n");
	        Transport.send(msg, "me@example.com", "my-password"); // 14, 15???
	    } catch (MyMessagingException mex) { // 16
	        System.out.println("send failed, exception: " + mex); // 17???
	    }
	}
}
