import java.io.Serializable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.CascadeStyles;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.PropertyAccessStrategyResolver;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tuple.ValueGeneration;
import org.hibernate.type.CompositeType;
import org.hibernate.type.Type;
//import org.hibernate.query.QueryParameter;
import org.hibernate.engine.query.spi.NamedParameterDescriptor;

//https://docs.jboss.org/hibernate/orm/5.2/javadocs/org/hibernate/query/QueryParameter.html
public class Hibernate515301200 {
    private String name;
    private Value value;
    private Type type;
    private String cascade;
    private boolean updateable = true;
    private boolean insertable = true;
    private boolean selectable = true;
    private boolean optimisticLocked = true;
    private java.util.Map metaAttributes;

    public String aMethod(String queryString, SharedSessionContractImplementor session) {
        final Dialect dialect = session.getFactory().getServiceRegistry().getService(JdbcServices.class).getJdbcEnvironment().getDialect();
        final int inExprLimit = dialect.getInExpressionCountLimit();
        for (Map.Entry<QueryParameter, QueryParameterListBinding> entry : parameterListBindingMap.entrySet()) {
            final NamedParameterDescriptor sourceParam = (NamedParameterDescriptor) entry.getKey();
            final Collection bindValues = entry.getValue().getBindValues();
            if (inExprLimit > 0 && bindValues.size() > inExprLimit) {
                log.tooManyInExpressions(dialect.getClass().getName(), inExprLimit, sourceParam.getName(), bindValues.size());
            }
            final boolean isJpaPositionalParam = sourceParam.isJpaPositionalParameter();
            final String paramPrefix = isJpaPositionalParam ? "?" : ParserHelper.HQL_VARIABLE_PREFIX;
            final String placeholder = paramPrefix + sourceParam.getName();
            final int loc = queryString.indexOf(placeholder);
            if (loc < 0) {
                continue;
            }
            final String beforePlaceholder = queryString.substring(0, loc);
            final String afterPlaceholder = queryString.substring(loc + placeholder.length());
            // check if placeholder is already immediately enclosed in parentheses
            // (ignoring whitespace)
            boolean isEnclosedInParens = StringHelper.getLastNonWhitespaceCharacter(beforePlaceholder) == '(' && StringHelper.getFirstNonWhitespaceCharacter(afterPlaceholder) == ')';
            if (bindValues.size() == 1 && isEnclosedInParens) {
                // short-circuit for performance when only 1 value and the
                // placeholder is already enclosed in parentheses...
                final QueryParameterBinding syntheticBinding = makeBinding(entry.getValue().getBindType());
                syntheticBinding.setBindValue(bindValues.iterator().next());
                parameterBindingMap.put(sourceParam, syntheticBinding);
                continue;
            }
            StringBuilder expansionList = new StringBuilder();
            int i = 0;
            for (Object bindValue : entry.getValue().getBindValues()) {
                // for each value in the bound list-of-values we:
                //		1) create a synthetic named parameter
                //		2) expand the queryString to include each synthetic named param in place of the original
                //		3) create a new synthetic binding for just that single value under the synthetic name
                final String syntheticName = (isJpaPositionalParam ? 'x' : "") + sourceParam.getName() + '_' + i;
                if (i > 0) {
                    expansionList.append(", ");
                }
                expansionList.append(ParserHelper.HQL_VARIABLE_PREFIX).append(syntheticName);
                final QueryParameter syntheticParam = new QueryParameterNamedImpl<>(syntheticName, sourceParam.getSourceLocations(), sourceParam.isJpaPositionalParameter(), sourceParam.getType());
                final QueryParameterBinding syntheticBinding = makeBinding(entry.getValue().getBindType());
                syntheticBinding.setBindValue(bindValue);
                parameterBindingMap.put(syntheticParam, syntheticBinding);
                i++;
            }
            queryString = StringHelper.replace(beforePlaceholder, afterPlaceholder, placeholder, expansionList.toString(), true, true);

            return queryString;
        }
    }
}
