import javax.imageio.stream.FileImageOutputStream;
import java.io.File;

public class FinalizationDeprecation extends Object {
    
    public static void main(String[] args) {
        FinalizationDeprecation o1 = new FinalizationDeprecation();
        o1.finalize();

        Runtime runtime = Runtime.getRuntime();
        runtime.runFinalization();
        
        System.runFinalization();

        FileImageOutputStream stream = new FileImageOutputStream(new File("hey"));
        stream.finalize();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}