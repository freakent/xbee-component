/**
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
package freakent.camel.component;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the component that manages {@link XBeeEndpoint}.
 */
public class XBeeComponent extends DefaultComponent {
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeComponent.class);
    
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        LOG.info("Creating XBeeEndpoint for {}, {}", uri, parameters);
    	Endpoint endpoint = new XBeeEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
    
    protected void doStart() throws Exception {
    	super.doStart();
    	LOG.info("***** Starting XBeeComponent *****");
		LOG.info("java.vm.name = {}", System.getProperty("java.vm.name"));
		LOG.info("java.library.path = {}", System.getProperty("java.library.path"));
		LOG.info("System Properties: {}", System.getProperties());
    }
    
    protected void doStop() throws Exception {
    	super.doStop();
    	LOG.info("***** Stopping XBeeComponent *****");
    }
    
}
