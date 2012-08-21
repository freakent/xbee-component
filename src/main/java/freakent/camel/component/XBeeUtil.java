/**
 * 
 */
package freakent.camel.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.XBeeAddress;

/**
 * @author martin
 *
 */
public class XBeeUtil {
    private static final transient Logger LOG = LoggerFactory.getLogger(XBeeUtil.class);

	static public String XBeeName(XBeeAddress xbeeAddress) {
		int[] address = xbeeAddress.getAddress();
		StringBuffer sb = new StringBuffer(address.length * 2);
		for (int x = 0; x < address.length; x++) {
			if (address[x] < 10 ) sb.append("0");
			sb.append(Integer.toHexString(address[x]).toUpperCase());
		}
		LOG.debug("XBee Address in HEX {} {}", sb.toString(), xbeeAddress);
		return sb.toString();
	}
}
