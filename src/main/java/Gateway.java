

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Gateway {

	private Main main;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Gateway gateway = new Gateway();
		gateway.boot();
	}
	
	public void boot() throws Exception {
		main = new Main();
		main.enableHangupSupport();
		main.addRouteBuilder(new GatewayRouteBuilder());
		System.out.println("Starting Camel");
		main.run();
	}
	
	private static class GatewayRouteBuilder extends RouteBuilder {
		
		@Override
		public void configure() {
			from("xbee:///dev/tty.usbserial-A900UE4F")
				.filter(header("xbee.sender").isEqualTo("0013A200406FBB7A"))
				.choice()
					.when(body().startsWith("PING"))
						.to("log:gateway")
				        .process(new Processor() {
				        	public void process(Exchange exchange) {
				        		if (!exchange.getIn().getBody().toString().startsWith("PING")) {
				        			System.out.println("Error:" + exchange.toString());
				        		}
				        		System.out.println("Processing XBee:" + exchange.toString());
				        		System.out.println("Headers:" + exchange.getIn().getHeaders().toString());
				        	}
				})
					.otherwise()
						.to("log:gateway");
		}
	}
	

}
