import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.query.Query;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.usertype.CompositeUserType;

import org.hibernate.loader.collection.BasicCollectionLoader;
import org.hibernate.loader.custom.CustomLoader;
import org.hibernate.loader.entity.CascadeEntityJoinWalker;
import org.hibernate.loader.plan.build.spi.ExpandingQuerySpace;
import org.hibernate.sql.ordering.antlr.ColumnMapper;

import org.hibernate.jmx.internal.DisabledJmxServiceImpl;

import org.hibernate.criterion.Criterion;

@NamedNativeQuery(callable = true, name = "name", query = "query")
public class HibernateOrm6TestClass implements BasicType {
    @AnyMetaDef(metaType = "type")
    String hey;

    @ParamDef(name = "name", type = "type")
    String param;

    StandardBasicTypes types;

    JavaTypeDescriptor javaTypeDescriptor;

    SqlTypeDescriptor sqlTypeDescriptor;

    CompositeUserType compositeUserType;

    public static void main(String[] args) {
        org.hibernate.query.Query q;
        q.stream();
        jakarta.persistence.Query q2;
        q2.getResultStream();

        org.hibernate.secure.spi.JaccIntegrator integrator;

        org.hibernate.Interceptor interceptor;
        interceptor.onSave(new Object(), new Object(), new Object[]{}, new String[]{}, new Type[]{});

        org.hibernate.sql.ANSICaseFragment fragment;

        org.hibernate.query.Query q3;
        q3.iterate();
    }

}
