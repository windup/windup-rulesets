package camel2.org.apache.camel.component.rest;

import java.util.Map;
import java.util.Set;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.NoFactoryAvailableException;
import org.apache.camel.NoSuchBeanException;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.FactoryFinder;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spi.RestConsumerFactory;
import org.apache.camel.spi.RestProducerFactory;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.util.HostUtils;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.spi.RestProducerFactoryHelper;

/**
 * The rest component is used for either hosting REST services (consumer) or calling external REST services (producer).
 */
@UriEndpoint(firstVersion = "2.14.0", scheme = "rest", title = "REST", syntax = "rest:method:path:uriTemplate", label = "core,rest", lenientProperties = true)
public class RestEndpoint extends FoobarEndpoint {

    @Override
    public Producer createProducer() throws Exception {
        if (ObjectHelper.isEmpty(host)) {
            // hostname must be provided
            throw new IllegalArgumentException("Hostname must be configured on either restConfiguration"
                    + " or in the rest endpoint uri as a query parameter with name host, eg rest:" + method + ":" + path + "?host=someserver");
        }

        RestProducerFactory apiDocFactory = null;
        RestProducerFactory factory = null;

        if (apiDoc != null) {
            LOG.debug("Discovering camel-swagger-java on classpath for using api-doc: {}", apiDoc);
            // lookup on classpath using factory finder to automatic find it (just add camel-swagger-java to classpath etc)
            try {
                FactoryFinder finder = getCamelContext().getFactoryFinder(RESOURCE_PATH);
                Object instance = finder.newInstance(DEFAULT_API_COMPONENT_NAME);
                if (instance instanceof RestProducerFactory) {
                    // this factory from camel-swagger-java will facade the http component in use
                    apiDocFactory = (RestProducerFactory) instance;
                }
                parameters.put("apiDoc", apiDoc);
            } catch (NoFactoryAvailableException e) {
                throw new IllegalStateException("Cannot find camel-swagger-java on classpath to use with api-doc: " + apiDoc);
            }
        }

        String cname = getComponentName();
        if (cname != null) {
            Object comp = getCamelContext().getRegistry().lookupByName(getComponentName());
            if (comp instanceof RestProducerFactory) {
                factory = (RestProducerFactory) comp;
            } else {
                comp = RestProducerFactoryHelper.setupComponent(getComponentName(), getCamelContext(), (Map<String, Object>) parameters.get("component"));
                if (comp instanceof RestProducerFactory) {
                    factory = (RestProducerFactory) comp;
                }
            }

            if (factory == null) {
                if (comp != null) {
                    throw new IllegalArgumentException("Component " + getComponentName() + " is not a RestProducerFactory");
                } else {
                    throw new NoSuchBeanException(getComponentName(), RestProducerFactory.class.getName());
                }
            }
            cname = getComponentName();
        }

        // try all components
        if (factory == null) {
            for (String name : getCamelContext().getComponentNames()) {
                Component comp = setupComponent(name, getCamelContext(), (Map<String, Object>) parameters.get("component"));
                if (comp instanceof RestProducerFactory) {
                    factory = (RestProducerFactory) comp;
                    cname = name;
                    break;
                }
            }
        }

        parameters.put("componentName", cname);

        // lookup in registry
        if (factory == null) {
            Set<RestProducerFactory> factories = getCamelContext().getRegistry().findByType(RestProducerFactory.class);
            if (factories != null && factories.size() == 1) {
                factory = factories.iterator().next();
            }
        }

        // no explicit factory found then try to see if we can find any of the default rest consumer components
        // and there must only be exactly one so we safely can pick this one
        if (factory == null) {
            RestProducerFactory found = null;
            String foundName = null;
            for (String name : DEFAULT_REST_PRODUCER_COMPONENTS) {
                Object comp = setupComponent(getComponentName(), getCamelContext(), (Map<String, Object>) parameters.get("component"));
                if (comp instanceof RestProducerFactory) {
                    if (found == null) {
                        found = (RestProducerFactory) comp;
                        foundName = name;
                    } else {
                        throw new IllegalArgumentException("Multiple RestProducerFactory found on classpath. Configure explicit which component to use");
                    }
                }
            }
            if (found != null) {
                LOG.debug("Auto discovered {} as RestProducerFactory", foundName);
                factory = found;
            }
        }

        if (factory != null) {
            LOG.debug("Using RestProducerFactory: {}", factory);

            RestConfiguration config = getCamelContext().getRestConfiguration(cname, true);

            Producer producer;
            if (apiDocFactory != null) {
                // wrap the factory using the api doc factory which will use the factory
                parameters.put("restProducerFactory", factory);
                producer = apiDocFactory.createProducer(getCamelContext(), host, method, path, uriTemplate, queryParameters, consumes, produces, config, parameters);
            } else {
                producer = factory.createProducer(getCamelContext(), host, method, path, uriTemplate, queryParameters, consumes, produces, config, parameters);
            }

            RestProducer answer = new RestProducer(this, producer, config);
            answer.setOutType(outType);
            answer.setType(inType);
            answer.setBindingMode(bindingMode);

            return answer;
        } else {
            throw new IllegalStateException("Cannot find RestProducerFactory in Registry or as a Component to use");
        }
    }

}
