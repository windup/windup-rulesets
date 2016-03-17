import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;

public class CustomGenerator implements PersistentIdentifierGenerator, Configurable
{

    private String tableName;

    @Override
    public Serializable generate(SessionImplementor sessionImplemetor, Object object) throws HibernateException
    {

        return getNextNumber(sessionImplemetor);

    }

    @Override
    public synchronized void configure(Type type, Properties params, Dialect d) throws MappingException
    {
        tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
    }

    private Long getNextNumber(SessionImplementor session)
    {
        String sql = "{call stored procedure name}";
        Long nextValue = null;
        try
        {
            PreparedStatement st =
                        session.getBatcher().prepareSelectStatement(sql);
            st.setString(1, tableName);
            try
            {
                ResultSet rs = st.executeQuery();
                try
                {
                    while (rs.next())
                    {
                        nextValue = Long.parseLong(rs.getString(1));
                    }
                }
                finally
                {
                    rs.close();
                }
            }
            finally
            {
                session.getBatcher().closeStatement(st);
            }
        }
        catch (SQLException sqle)
        {
            throw JDBCExceptionHelper.convert(session.getFactory()
                        .getSQLExceptionConverter(), sqle,
                        "could not fetch initial value for increment generator",
                        sql);
        }
        return null;
    }

}