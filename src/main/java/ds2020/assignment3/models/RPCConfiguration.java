package ds2020.assignment3.models;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.googlecode.jsonrpc4j.ProxyUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RPCConfiguration {
    //private static final String endpoint = "http://localhost:8080/medi";
    private static final String endpoint = "https://assignment3backend.herokuapp.com/medi";

    @Bean
    public JsonRpcHttpClient jsonRpcHttpClient() {
        URL url = null;
        Map<String, String> map = new HashMap<>();
        try {
            url = new URL(RPCConfiguration.endpoint);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new JsonRpcHttpClient(url, map);
    }

    @Bean
    public MedicationPlanRMI exampleClientAPI(JsonRpcHttpClient jsonRpcHttpClient) {
        return ProxyUtil.createClientProxy(getClass().getClassLoader(), MedicationPlanRMI.class, jsonRpcHttpClient);
    }
}