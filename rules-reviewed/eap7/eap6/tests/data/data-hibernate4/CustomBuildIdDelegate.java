
import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.jboss.logging.Logger;

import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.spi.TemporaryTableBulkIdStrategy;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.jdbc.AbstractWork;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.hql.spi.PersistentTableBulkIdStrategy;
import org.hibernate.id.Configurable;

/**
 * @author Steve Ebersole
 */
public class CustomBulkIdDelegate extends TemporaryTableBulkIdStrategy {
    private static final CoreMessageLogger log = Logger.getMessageLogger( CoreMessageLogger.class, CustomBulkIdDelegate.class.getName() );

    @Override
    protected void createTempTable(Queryable persister, SessionImplementor session) {
        TemporaryTableCreationWork work = new TemporaryTableCreationWork( persister );
        // here we always want to (a) create the tables in isolation and (b) do not start a new transaction for the creation
        session.getTransactionCoordinator()
                .getTransaction()
                .createIsolationDelegate()
                .delegateWork( work, false );
    }

    // todo make these protected on TemporaryTableBulkIdStrategy ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static SqlExceptionHelper.WarningHandler CREATION_WARNING_HANDLER = new SqlExceptionHelper.WarningHandlerLoggingSupport() {
        public boolean doProcess() {
            return log.isDebugEnabled();
        }

        public void prepare(SQLWarning warning) {
            log.warningsCreatingTempTable( warning );
        }

        @Override
        protected void logWarning(String description, String message) {
            log.debug( description );
            log.debug( message );
        }
    };

    private static class TemporaryTableCreationWork extends AbstractWork {
        private final Queryable persister;

        private TemporaryTableCreationWork(Queryable persister) {
            this.persister = persister;
        }

        @Override
        public void execute(Connection connection) {
            try {
                Statement statement = connection.createStatement();
                try {
                    statement.executeUpdate( persister.getTemporaryIdTableDDL() );
                    persister.getFactory()
                            .getServiceRegistry()
                            .getService( JdbcServices.class )
                            .getSqlExceptionHelper()
                            .handleAndClearWarnings( statement, CREATION_WARNING_HANDLER );
                }
                finally {
                    try {
                        statement.close();
                    }
                    catch( Throwable ignore ) {
                        // ignore
                    }
                }
            }
            catch( Exception e ) {
                log.debug( "unable to create temporary id table [" + e.getMessage() + "]" );
            }
        }
    }
    
    public IdentifierGenerator createIdentifierGenerator(String strategy, Type type, Properties config) {
        try {
            Class clazz = getIdentifierGeneratorClass( strategy );
            CustomGenerator identifierGenerator = ( CustomGenerator ) clazz.newInstance();
            if ( identifierGenerator instanceof Configurable ) {
                ( ( Configurable ) identifierGenerator ).configure( type, config, dialect );
            }
            return identifierGenerator;
        }
        catch ( Exception e ) {
            final String entityName = config.getProperty( IdentifierGenerator.ENTITY_NAME );
            throw new MappingException( String.format( "Could not instantiate id generator [entity-name=%s]", entityName ), e );
        
   }

}

    
    public class MyTestIdentifier implements Configurable extends IdentifierGenerator {
        
        @Override
        void configure(Type type, Properties params, Dialect dialect)
    }
}