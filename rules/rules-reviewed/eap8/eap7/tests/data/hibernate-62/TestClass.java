import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.LockMode;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.action.spi.Executable;
import java.lang.Object;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.cfg.AnnotatedClassType;
import org.hibernate.cfg.annotations.ArrayBinder;
import org.hibernate.loader.AbstractEntityJoinWalker;
import org.hibernate.loader.collection.BatchingCollectionInitializer;

public class TestClass {
    public static void main(String[] args) {
        EntityPersister persister = new EntityPersister();
//        persister.lock(new Object(), new Object(), LockMode.FORCE, new SharedSessionContractImplementor());
        Object o1 = new Object();
        Object oa = new Object[]{o1, o1};
        persister.multiload(o1, o1, oa);
        
        Executable executable = new Executable();
        executable.afterDeserialize(new SharedSessionContractImplementor());
        JdbcType type = new JdbcType();
        type.getJdbcRecommendedJavaTypeMapping();
    }
}