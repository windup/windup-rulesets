import java.util.EnumMap;
import java.util.Locale;
import java.util.StringTokenizer;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;


     // Abstract base class for elements
private abstract class Element {
    public final Element parent;         

    @CallerSensitive
    public ClassLoader getClassLoader() {
            if (classLoader == null) {
                final SecurityManager sm = System.getSecurityManager();
                final Class caller = (sm != null) ?
                       Reflection.getCallerClass() :
                        null;
                return getDefaultClassLoader(caller);
            }        

     }
}