import java.notio.File;
import java.io.File;
import java.nio.File;
import java.notnio.File;
import org.apache.commons.io.File;
import org.apache.commons.notio.File;
import java.io.FileWriter;
import java.io.FileReader;

// Should not match the rule
import java.io.IOException;
import java.io.Closeable;
import java.io.Serializable;
import java.io.DefaultFileSystem;

public class FileSystem {

    String path = "/here/it/is";
    String relativePath = "./here/it/is";
    String relativePath2 = "../here/it/is";
    String homePath = "/home/it/is";
    String notHomePath = "/nothome/it/is";
 }
