import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
 
public class WalletTest {
 
    private PoolDataSource pds = null;
 
    public WalletTest() throws SQLException {
        // Create pool-enabled data source instance.
        pds = PoolDataSourceFactory.getPoolDataSource();
 
        // PoolDataSource and UCP configuration
        // set the connection properties on the data source and pool properties
        // pds.setUser("scott");
        // pds.setPassword("tiger");
        pds.setURL("jdbc:oracle:thin:/@WALLET_TEST_CONNECTION_ID");  // (1)
        pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pds.setInitialPoolSize(10);
        pds.setMinPoolSize(10);
        pds.setMaxPoolSize(20);
        pds.setConnectionProperty("oracle.jdbc.ReadTimeout", "5000");
 
        System.setProperty("oracle.net.wallet_location",
            "(SOURCE=(METHOD=file)(METHOD_DATA=(DIRECTORY=/home/scorwin/swdev/WalletExp/wallet)))");  // (2)
    }
 
    public void run() throws SQLException {
        List<Connection> connList = new ArrayList<Connection>();
 
        for (int i = 0; i < 5; i++) {
            // Get a database connection from the datasource.
            Connection conn = pds.getConnection();
            connList.add(conn);
        }
 
        Connection conn = connList.get(0);
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select sysdate from dual");
        while (rset.next()){
            System.out.println("sysdate from dual: " + rset.getString(1) + " as of "
                    + new Date());
        }
        rset.close();
        stmt.close();
 
        // close all connections
        for (int j = 0; j < connList.size(); j++) {
            ((Connection) connList.get(j)).close();
        }

    }
 
    public static void main(String[] args) {
        System.out.println("Started at " + new Date());
 
        try {
            WalletTest basicDemo = new WalletTest();
            basicDemo.run();
        } catch (SQLException e) {
            System.out.println("SQLException occurred: " + e.getMessage());
            e.printStackTrace();
        }
 
        System.out.println("Ended at " + new Date());
    }
}