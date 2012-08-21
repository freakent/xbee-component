package freakent.xbee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;


public class XBeeReceiver {

	private XBee xbee = null;
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeReceiver.class);
    int pings=0;
    int dingdongs=0;
    int errors=0;

    public XBeeReceiver(String port) {
        try {
    		LOG.info("Connecting to XBee on port " + port);
			xbee = new XBee();
			xbee.open(port, 9600);
            while (true) {
            	// we wait here until a packet is received.
                XBeeResponse response = xbee.getResponse();
                        
                if (response.getApiId() == ApiId.ZNET_EXPLICIT_RX_RESPONSE || response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
                	ZNetRxResponse rx = (ZNetRxResponse) response;
                    LOG.info("received explicit packet response " + response.toString());
                	int[] data = rx.getData();
                	String message = ByteUtils.toString(data).trim();

                	if (message.startsWith("DINGDONG")) {
                		dingdongs++;
                        LOG.info("data: {}, message: {}", data, message);
                	} else if (message.startsWith("PING")) {
                		pings++;
                        LOG.info("data: {}, message: {}", data, message);
                	} else {
                		errors++;
                        LOG.error("data: {}, message: {}", data, message);
                	}
                    LOG.info("stats: pings:{} errors:{}", pings, errors);
                } else {
                    LOG.warn("received unexpected packet " + response.toString());
                }
            }
			
        } catch (XBeeException e) {
        	LOG.error("Failed to open XBee", e);
        } finally {
            xbee.close();
        }
    }
        
    /**
	 * @param args
	 */
	public static void main(String[] args) {
    	String port;
    	if (args.length == 0) {
    	  port = "/dev/tty.usbserial-A900UE4F";
    	} else {
          port = args[0];
    	}
		new XBeeReceiver(port);
	}

}
