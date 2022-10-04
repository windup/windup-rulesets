import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.collection.spi.PersistentCollection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hibernate515300001_1 {

    Object aMethod(Object collection, CollectionType collectionType)
            throws HibernateException {
        if ( collection!=null && (collection instanceof PersistentCollection) ) {
            final SessionImplementor session = getSession();
            PersistentCollection coll = (PersistentCollection) collection;
            if ( coll.setCurrentSession(session) ) {
                reattachCollection( coll, collectionType );
            }
            return null;
        }
        else {
            return processArrayOrNewCollection(collection, collectionType);
        }
    }
}
