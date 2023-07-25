import java.awt.color.ICC_Profile;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.lang.Compiler;
import javax.management.remote.rmi.RMIIIOPServerImpl;

public class FinalizationDeprecation extends Object {
    
    public static void main(String[] args) {
        ICC_Profile profile = ICC_Profile.getInstance(1);
        profile.finalize();

        ColorModel colorModel = ColorModel.getRGBdefault();
        colorModel.finalize();

        IndexColorModel indexColorModel = IndexColorModel.getRGBdefault();
        indexColorModel.finalize();

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.allowThreadSuspension();
    }

}