import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerClaz {
    String driverName = "com.mysql.jdbc.Driver";
    Connection con = null;
    public DriverManagerClaz() {
        try {
            //Loading Driver for MySql
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
    public Connection createConnection() {
        try {
            String connectionUrl="jdbc:mysql://localhost:3306/student";
            String userName="root";
            String userPass="root";
            con = DriverManager
                    .getConnection(connectionUrl,userName,userPass);
            System.out.println("******* Connection created successfully........");
            } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return con;
    }
    public void closeConnection(){
        try{
            this.con.close();
        System.out.println("******* Connection closed Successfully.........");
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public static void main(String args[]){
        DriverManagerClaz con=new DriverManagerClaz();
        con.createConnection();
        con.closeConnection();
    }
}