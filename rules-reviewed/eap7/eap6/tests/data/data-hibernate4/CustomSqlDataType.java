import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class CustomSqlDataType implements SqlTypeDescriptor
{

    private static final long serialVersionUID = 3014794392132293815L;

    @Override
    public boolean canBeRemapped()
    {
        return false;
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> arg0)
    {
        return null;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> arg0)
    {
        return null;
    }

    @Override
    public int getSqlType()
    {
        return 0;
    }
    

}
