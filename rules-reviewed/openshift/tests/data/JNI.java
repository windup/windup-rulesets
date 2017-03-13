import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.DWORD;


public class JNI {

    public interface MyLibrary extends Library {
    }

    public static void main(String argv[]) {
		String libraryName = "foo.jar";
		System.load(libraryName);
		System.loadLibrary(libraryName);
		System.mapLibraryName(libraryName);	
		
		Runtime.getRuntime().load(libraryName);
		Runtime.getRuntime().loadLibrary(libraryName);
		
		DWORD dword = new DWORD(0x12345678);
		Native.loadLibrary(MyLibrary.class);

    }

}
