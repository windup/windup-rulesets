import javax.activation.DataHandler;
import java.io.IOException;
import java.io.OutputStream;

public class TestMOTMRule {
	public static void main(String[] args) {
		DataHandler testHandler = new DataHandler();
		OutputStream test = new OutputStream();
		testHandler.writeTo(test);
	}
}
