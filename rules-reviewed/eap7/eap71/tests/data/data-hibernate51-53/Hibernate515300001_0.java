import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.internal.SessionImpl;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hibernate515300001_0 {

    public void aMethod(Type type, Object owner, Serializable cached,
                        ResultSet rs, String names) {
        SessionImplementor session; 
            type.assemble(cached, session, owner);
            type.beforeAssemble(cached, session);
            type.disassemble(o, session, owner);
            type.hydrate(rs, names, session, owner);
            type.isDirty(oldState, currentState, session);
            type.isDirty(oldState, currentState, checkable, session);
            type.isModified(oldState, currentState, checkable, session);
            type.nullSafeGet(rs, names, session, owner);
            type.nullSafeSet(st, value, index, session);
            type.replace(o, target, session, owner, copyCache);
            type.replace(o, target, session, owner, copyCache, foreignKeyDirection);
            type.resolve(value, session, owner);
            type.semiResolve(value, session, owner);
            type.getHashCode(null);
            type.getHashCode(session);
        }

    public void getDeletes(CollectionPersister persister, boolean indexIsFormula) throws HibernateException {
        final Type indexType = persister.getIndexType();
        indexType.isDirty();
    }
}
