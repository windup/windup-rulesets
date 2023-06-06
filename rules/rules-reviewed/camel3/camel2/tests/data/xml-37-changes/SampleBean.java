/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.cluster.FooBarCluster;
import org.apache.camel.processor.interceptor.BreakpointSupport;
import org.apache.camel.impl.validator.ProcessorValidator;
import org.apache.camel.impl.transformer.ProcessorTransformer;
import org.apache.camel.impl.transformer.DataFormatTransformer;
import org.apache.camel.impl.validator.ValidatorKey;
import org.apache.camel.impl.DefaultExecutorServiceManager;
import org.apache.camel.impl.transformer.TransformerKey;
import org.apache.camel.processor.ConvertBodyProcessor;
import org.apache.camel.impl.engine.DefaultClaimCheckRepository;
import org.apache.camel.impl.engine.DefaultProducerCache;
import org.apache.camel.impl.engine.DefaultConsumerCache;
import org.apache.camel.impl.engine.EmptyProducerCache;
import org.apache.camel.impl.engine.ServicePool;
import org.apache.camel.impl.engine.ProducerServicePool;
import org.apache.camel.impl.engine.PollingConsumerServicePool;
import org.apache.camel.impl.engine.EventNotifierCallback;
import org.apache.camel.impl.saga.InMemorySagaService;
import org.apache.camel.impl.saga.InMemorySagaCoordinator;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Component("myBean")
public class SampleBean {

    @Value("${greeting}")
    private String say;

    public String saySomething() {
	HttpOperationFailedException hofe = new HttpOperationFailedException();
        return say;
    }

}
