import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Locale;
//Java program to demonstrate PrintStream methods
class PrintStreamExample
{
    public static void main(String args[]) throws FileNotFoundException
    {
        FileOutputStream fout=new FileOutputStream("file.txt");
        PrintStream out=new PrintStream(fout);
        String s="Hello world";
  
        out.print (s);
        out.println();
        
        out.flush();
        
        out.close();
    }
}