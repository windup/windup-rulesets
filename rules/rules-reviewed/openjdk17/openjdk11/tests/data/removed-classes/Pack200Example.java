import java.util.SortedMap;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.jar.Pack200.Unpacker;

public final class Pack200Example {

  public static final Packer createPacker(){
    Pack200.Packer p = Pack200.newPacker();
    SortedMap<String, String> props = p.properties();
//    props.put(Pack200.Packer.KEEP_FILE_ORDER, Pack200.Packer.TRUE);
    props.put(Pack200.Packer.MODIFICATION_TIME, Pack200.Packer.KEEP);
    props.put(Pack200.Packer.EFFORT, "0");
//    props.put(Pack200.Packer.CODE_ATTRIBUTE_PFX+"LocalVariableTable", Pack200.Packer.STRIP);
    return p;
  }

  public static final Unpacker createUnpacker(){
    Pack200.Unpacker p = Pack200.newUnpacker();
    SortedMap<String, String> props = p.properties();
//    props.put(Pack200.Packer.KEEP_FILE_ORDER, Pack200.Packer.TRUE);
    props.put(Pack200.Packer.MODIFICATION_TIME, Pack200.Packer.KEEP);
    props.put(Pack200.Packer.EFFORT, "0");
//    props.put(Pack200.Packer.CODE_ATTRIBUTE_PFX+"LocalVariableTable", Pack200.Packer.STRIP);
    return p;
  }

}