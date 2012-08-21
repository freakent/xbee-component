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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;

/**
 * The XBee consumer.
 */
public class XBeeConsumer extends DefaultConsumer implements PacketListener {
    private final XBeeEndpoint endpoint;
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeConsumer.class);

    public XBeeConsumer(XBeeEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }
    
    protected void doStart() throws Exception {
    	super.doStart();
    	LOG.info("***** Starting XBeeConsumer *****");
    	this.endpoint.getXBee().addPacketListener(this);

    }
    
    protected void doStop() throws Exception {
    	super.doStop();
    	LOG.info("***** Stopping XBeeConsumer *****");
    	this.endpoint.getXBee().removePacketListener(this);

    }

	@Override
	public void processResponse(XBeeResponse response) {
		if (response instanceof ZNetRxResponse) {
			respond((ZNetRxResponse)response);
		}

		/*   if (response.getApiId() == ApiId.ZNET_RX_RESPONSE ) {
        	ZNetRxResponse znrr = (ZNetRxResponse) response; 
    		int[] in = znrr.getData();
    		String in_str = ByteUtils.toString(in).trim();
    		XBeeAddress64 sender = znrr.getRemoteAddress64();
    		LOG.info(sender.toString() + "  " + in_str + "  " + in_str.length());
    		processExchange(sender.toString() + "  " + in_str + "  " + in_str.length());
        } else {
        	RemoteAtResponse rar = (RemoteAtResponse)response;
        	String value_str = ByteUtils.toString(rar.getValue()).trim();
        	LOG.info(rar.toString());
        	LOG.info(value_str);
        	processExchange(value_str);
        }
		// TODO Auto-generated method stub
    */    
    }

	public void respond(ZNetRxResponse response) {
		int[] in = response.getData();
		LOG.info("Response:"+response.toString());
		String in_str = ByteUtils.toString(in).trim();
		XBeeAddress64 sender = response.getRemoteAddress64();
		LOG.info("Message [{}]", in_str);
		
		Map<String, Object> headers = new HashMap<String,Object>();
		headers.put("xbee.sender", XBeeUtil.XBeeName(sender));
		headers.put("xbee.receivedAt", new Date());
		LOG.info("Headers {}", headers);
	
		
		processExchange(in_str, headers);
	
	}

    private void processExchange(String body, Map<String, Object> headers) {
        Exchange exchange = endpoint.createExchange();
        exchange.getIn().setBody(body);
        exchange.getIn().setHeaders(headers);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
        } catch (Exception e) {
        	LOG.error("Error whilst processing exchange.", e);
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
    

	
}
