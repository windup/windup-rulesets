import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.hbm2ddl.SchemaValidator;

public class SchemaDDLGenerator {

    public static void execute(Dialect dialect, Class<?>... classes) {
        Configuration configuration = new Configuration();
        SchemaExport schemaExport = new SchemaExport(configuration);
        SchemaUpdate schemaUpdate = new SchemaUpdate(configuration);
        try {
            new SchemaValidator(configuration).validate();
        }
        catch (Exception e) {
            logger.error("Failed create the table: " + e.getMessage());
        }
    }

}