import javax.annotation.sql.DataSourceDefinition;

/**
 * @author <a href="mailto:dklingenberg@gmail.com">David Klingenberg</a>
 */
@DataSourceDefinition(
        name = "jdbc/single-DS",
        className="com.example.MyDataSource",
        portNumber=6689,
        serverName="example.com",
        user="lance",
        password="secret"
),
public class AnnotationSingleDs
{
}
