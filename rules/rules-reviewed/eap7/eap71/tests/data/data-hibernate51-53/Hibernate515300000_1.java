import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.type.AbstractStandardBasicType;

public class Hibernate515300000_1 implements CompositeUserType, ParameterizedType {

    @Override
    public void setParameterValues(Properties parameters) { }

    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
                            final Object value, final int property,
                            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        preparedStatement.setNull(property, Hibernate.INTEGER.sqlType());
        preparedStatement.setNull(property + 1, Hibernate.TIMESTAMP.sqlType());
        preparedStatement.setNull(property + 2, Hibernate.INTEGER.sqlType());
        preparedStatement.setNull(property + 3, Hibernate.TIMESTAMP.sqlType());
    }
}
