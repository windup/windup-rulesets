import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Dimension;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class FrameView {
  JFrame frame;
  public void createFrame()
  {
    frame = new JFrame("CLOCK");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    JApplet applet = new ClockComponent();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setSize(new Dimension(400, 400));
    frame.setVisible(true);
  }
}