import java.io.FileInputStream;
import java.io.InputStreamReader;
 
public class InputStreamReaderExample 
{
  public static void main(String[] args) 
  {
    // Creates an array of character
    char[] array = new char[50];
 
    try (InputStreamReader input 
        = new InputStreamReader(new FileInputStream("demo.txt"))) {
 
      // Reads characters from the file
      input.read(array);
 
      System.out.println(array);
    }
 
    catch (Exception e) {
      e.getStackTrace();
    }
  }
}