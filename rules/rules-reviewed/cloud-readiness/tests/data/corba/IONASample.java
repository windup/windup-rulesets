import com.iona.corba.util.SystemExceptionDisplayHelper; 

import java.io.*; 
import java.util.*; 
 
public class Client  
{ 
   
  public static void main(String args[]) 
  { 
    java.util.Properties props = System.getProperties(); 
    props.put("org.omg.CORBA.ORBClass", "com.iona.corba.art.artimpl.ORBImpl"); 
    props.put("org.omg.CORBA.ORBSingletonClass", "com.iona.corba.art.artimpl.ORBSingleton"); 
 
    ORB orb = ORB.init(args, props); 
     
    System.out.println("initializing ORB"); 
     
    try  
    { 
      orb23 = (org.omg.CORBA_2_3.ORB)orb; 
       
      if (!readCommandLineArguments(args)) 
      { 
        usage(); 
        return; 
      } 
       
      System.out.println("retrieving exported Bank reference"); 
 
      String ior = null; 
      Bank bank = null; 
       
      System.out.println("reading stringified object reference from file"); 
 
 
      InputStreamReader osr=new InputStreamReader(fis); 
      BufferedReader br=new BufferedReader(osr); 
      ior = br.readLine(); 
      br.close(); 
 
      System.out.println("calling ORB::string_to_object"); 
      System.out.println("IOR is :"+ior); 
      org.omg.CORBA.Object o = orb.string_to_object(ior); 
 
      System.out.println("narrowing CORBA::Object"); 
      bank = BankHelper.narrow(o); 
 
      BankMenu bank_menu = new BankMenu(bank, sm_is_interactive); 
      bank_menu.run(); 
       
      try 
      { 
        if (!sm_is_interactive) 
        { 
          for (int i = 0; i < BankMenu.sm_account_names.length; i++) 
          { 
            // Remove database files 
            // 
          } 
        } 
      } 
      finally 
      { 
        orb.shutdown(true); 
      } 
    } 
    catch (SystemException ex) { 
      System.err.println("Exception during string_to_object() or narrow()"); 
      System.out.println(SystemExceptionDisplayHelper.toString(ex)); 
      System.exit(0); 
    } 
    catch (java.io.IOException io_exc) 
    { 
      System.err.println("IOException during string_to_object() or narrow()"); 
      io_exc.printStackTrace(); 
      System.exit(0); 
    } 
  } 
   
}