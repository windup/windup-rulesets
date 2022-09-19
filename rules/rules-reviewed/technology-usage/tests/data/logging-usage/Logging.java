import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.DailyRollingFileAppender;
import java.util.logging.FileHandler;
import java.util.logging.SocketHandler;
import ch.qos.logback.core.FileAppender;
import org.pmw.tinylog.writers.FileWriter;

public class Logging {
	
	public class MyFileAppender extends FileAppender{}
	public class MyRollingFileAppender extends RollingFileAppender{}
	public class MyDailyRollingFileAppender extends DailyRollingFileAppender{}
	public class MyFileHandler extends FileHandler{}
	public class MySocketHandler extends SocketHandler {}
	
	public static void main(String argv[]) {
		FileAppender fa = new FileAppender();
		RollingFileAppender rfa = new RollingFileAppender();
		DailyRollingFileAppender drfa = new DailyRollingFileAppender();
		FileHandler fh = new FileHandler();
		SocketHandler sh = new SocketHandler("host", 1);
	}

}
