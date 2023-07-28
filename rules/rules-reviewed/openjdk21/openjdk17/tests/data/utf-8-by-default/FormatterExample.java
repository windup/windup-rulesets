import java.util.Formatter;
import java.util.Locale;

class FormatterExample
{
    public static void main(String args[]) 
    {
        Formatter formatter = new Formatter(Locale.US);
        String name = "Phil";
        int age = 57;
        formatter.format("User name is %s and age is %d", name, age);
        System.out.println(formatter); 

    }
}