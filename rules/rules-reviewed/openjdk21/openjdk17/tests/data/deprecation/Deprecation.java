import javax.security.auth.Subject;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicScrollPaneUI.PropertyChangeHandler;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;
import javax.swing.plaf.basic.BasicScrollPaneUI.ViewportChangeHandler;
import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.net.URL;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import javax.management.loading.MLet;
import javax.management.loading.MLetContent;
import javax.management.loading.MLetMBean;
import javax.management.loading.PrivateMLet;
import javax.lang.ThreadDeath;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class FinalizationDeprecation extends BasicMenuItemUI, BasicDirectoryModel {

    public static void main(String[] args) {
        MouseInputHandler mouseInputHandler = new BasicMenuItemUI.MouseInputHandler();

        BasicDirectoryModel basicDirectoryModel = new BasicDirectoryModel(null);
        basicDirectoryModel.intervalAdded(null);
        basicDirectoryModel.intervalRemoved(null);
        basicDirectoryModel.lt(null, null);
        
        BasicToolBarUI basicToolBarUI = new BasicToolBarUI();
        basicToolBarUI.createFloatingFrame(null);

        Subject.doAs(new Subject(), new PrivilegedActionImpl());
        Subject.doAs(new Subject(), new PrivilegedExceptionAction());

        SynthLookAndFeel synthLookAndFeel = new SynthLookAndFeel();
        URL url = new URL("hey");
        synthLookAndFeel.load(url);
        
    }
    
    public class PrivilegedActionImpl implements PrivilegedAction<String> {
        @Override
        public String run() {

        }
    }
    
}