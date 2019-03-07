import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.collection.spi.PersistentCollection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hibernate515300000_0 implements Type {

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names,
                              SessionImplementor session, Object owner) throws SQLException {
        return get(rs, names, session, owner);
    }

    public Object nullSafeGet(ResultSet rs, String[] names,
                              Object session, Object owner) throws SQLException {
        return get(rs, names, session, owner);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SessionImplementor session) throws SQLException {
        set(st, clazz.cast(value), index, session);
    }

    protected abstract T get(ResultSet rs, String[] names,
                             SessionImplementor session, Object owner) throws SQLException;

    protected abstract void set(PreparedStatement st, T value, int index,
                                SessionImplementor session) throws SQLException;

    @Override
    public boolean isModified(Object oldState, Object currentState, boolean[] checkable, SessionImplementor session) {
        return false;
    }

    @Override
    public boolean isDirty(Object oldState, Object currentState, boolean[] checkable, SessionImplementor session) {
        return false;
    }

    @Override
    public boolean isDirty(Object oldState, Object currentState, SessionImplementor session) {
        return false;
    }

    @Override
    public Serializable disassemble(Object o, SessionImplementor session, Object owner) {
        return (Serializable) o;
    }

    @Override
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) {
        return cached;
    }

    @Override
    public Object beforeAssemble(Serializable cached, SessionImplementor session) {
        return cached;
    }

    @Override
    public Object hydrate(ResultSet rs, String[] names, SessionImplementor session, Object owner) {
        return null;
    }

    @Override
    public Object replace(Object o, Object target, SessionImplementor session, Object owner, Map copyCache) {
        return o;
    }

    @Override
    public Object replace(Object o, Object target, SessionImplementor session, Object owner, Map copyCache,  ForeignKeyDirection foreignKeyDirection) {
        return o;
    }

    @Override
    public Object resolve(Object value, SessionImplementor session, Object owner) {
        return value;
    }

    @Override
    public Object semiResolve(Object value, SessionImplementor session, Object owner) {
        return value;
    }
}
