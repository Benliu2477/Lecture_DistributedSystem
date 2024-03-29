package staff;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JMeterBookingClientWrapper extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
//        String serverAddress = context.getParameter("serverAddress");
//        String port = context.getParameter("port");

        String serverAddress = "dsgtteam6.eastus.cloudapp.azure.com";
        int port = 8082;

        SampleResult result = new SampleResult();
        result.sampleStart(); // 开始计时
        try {
            BookingClient client = new BookingClient(serverAddress, port);
            client.run(); // 调用你原始 client.jar 中的方法
            result.setSuccessful(true);
            result.setResponseMessage("Success");
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            // e.printStackTrace();
        } finally {
            result.sampleEnd(); // 结束计时
        }
        return result;
    }
}
