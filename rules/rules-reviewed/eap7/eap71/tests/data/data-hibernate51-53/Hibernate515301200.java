import org.hibernate.query.internal.QueryParameterNamedImpl;
import org.hibernate.engine.query.spi.NamedParameterDescriptor;
import org.hibernate.query.QueryParameter;

public class Hibernate515301200 {

    public void aMethod(String queryString) {
        for (Map.Entry<QueryParameter, QueryParameterListBinding> entry : parameterListBindingMap.entrySet()) {
            QueryParameter queryParameter = entry.getKey();
            queryParameter.getType();
            if (queryParameter instanceof NamedParameterDescriptor) {
                final NamedParameterDescriptor sourceParam = (NamedParameterDescriptor) queryParameter;
                sourceParam.getType();
            } else if (queryParameter instanceof QueryParameterNamedImpl) {
                final QueryParameterNamedImpl queryParameterNamed = (QueryParameterNamedImpl) queryParameter;
                queryParameterNamed.getType();
            }
        }
    }
}