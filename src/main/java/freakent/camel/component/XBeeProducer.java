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

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.zigbee.ZNetTxRequest;
import com.rapplogic.xbee.util.ByteUtils;

/**
 * The XBee producer.
 */
public class XBeeProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeProducer.class);
    //private XBeeEndpoint endpoint;
    private XBee xbee = null;

    public XBeeProducer(XBeeEndpoint endpoint) {
        super(endpoint);
        //this.endpoint = endpoint;
        this.xbee = endpoint.getXBee();
    }

    public void process(Exchange exchange) throws Exception {
    	LOG.debug("Processing exchange:{}", exchange);
    	String msg = exchange.getIn().getBody().toString();
    	ZNetTxRequest tx = new ZNetTxRequest(XBeeAddress64.BROADCAST, ByteUtils.stringToIntArray(msg));
    	xbee.sendRequest(tx);
        System.out.println(msg);    
    }

}
