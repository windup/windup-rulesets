package org.apache.camel.component.fhir.dataformat.spring;

import org.apache.camel.CamelContext;
import org.apache.camel.model.HystrixConfigurationDefinition;
import org.apache.camel.model.HystrixDefinition;

public class ModelInterface51Test extends CamelSpringTestSupport {

    private FhirDataFormat getDataformat(String name) {
        CamelContext camelContext = context();
        HystrixConfigurationDefinition def = new HystrixConfigurationDefinition();
        def.setGroupKey("global-group-key");
        def.setThreadPoolKey("global-thread-key");
        def.setCorePoolSize(10);

        HystrixConfigurationDefinition ref = new HystrixConfigurationDefinition();
        ref.setGroupKey("ref-group-key");
        ref.setCorePoolSize(5);

        camelContext.setHystrixConfiguration(def);
        camelContext.addHystrixConfiguration("ref-hystrix", ref);
        CustomTransformerDefinition ctd = new CustomTransformerDefinition();
        ctd.setScheme("custom");
        ctd.setClassName(MyTransformer.class.getName());
        camelContext.getTransformers().add(ctd);
        return (FhirDataFormat) ((FhirDataformat) camelContext.getDataFormats().get(name)).getDataFormat();
    }

