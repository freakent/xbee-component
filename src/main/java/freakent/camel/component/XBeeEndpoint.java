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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.FailedToStartRouteException;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;

/**
 * Represents a XBee endpoint.
 */
public class XBeeEndpoint extends DefaultEndpoint {
	
	private XBee xbee = null;
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeEndpoint.class);

    public XBeeEndpoint() {
    }

    public XBeeEndpoint(String uri, XBeeComponent component) {
        super(uri, component);
		LOG.info("Initialising Endpoint " + getEndpointUri());
    }
    
    protected void doStart() throws Exception {
    	super.doStart();
    	LOG.info("***** Starting XBee Endpoint *****");
        try {
    		LOG.info("Connecting to XBee on port " + uri2serial(getEndpointUri()));
			xbee = new XBee();
			xbee.open(uri2serial(getEndpointUri()), 9600);
		} catch (XBeeException e) { 
			if(e.getMessage().startsWith("AT command timed-out while attempt to set/read in API mode")) {
				LOG.error("Error trying to open XBee: {}", e.getMessage());
			} else {
				LOG.error("Failed to start XBee endpoint", e);
				throw new FailedToStartRouteException(e);
			}
			//Ignore "AT command timed-out while attempt to set/read in API mode.  The XBee radio must be in API mode (AP=2) to use with this library"
	
		}
    }
    
    protected void doStop() throws Exception {
    	super.doStop();
    	LOG.info("***** Stopping XBee Endpoint *****");
    	if(this.xbee.isConnected()) {
    		this.xbee.close();
    	}
    }

    public Producer createProducer() throws Exception {
        return new XBeeProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	XBeeConsumer consumer = new XBeeConsumer(this, processor);
        return consumer;
    }

    public boolean isSingleton() {
        return true;
    }
    
    public XBee getXBee() {
    	return this.xbee;
    }
    
    private String uri2serial(String uristr){
    	try {
			URI uri = new URI(uristr);
			return uri.getPath();
		} catch (URISyntaxException e) {
			LOG.error("URI provided is not a valid format: " + uristr );
			return "Unknown";
		}

    }
}
