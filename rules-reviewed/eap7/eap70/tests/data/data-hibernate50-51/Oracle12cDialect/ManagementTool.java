import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;
import org.hibernate.tool.schema.spi.SchemaManagementTool;

public class ManagementTool {

    public static void execute() {
        SchemaManagementTool tool = new HibernateSchemaManagementTool();
    }

}