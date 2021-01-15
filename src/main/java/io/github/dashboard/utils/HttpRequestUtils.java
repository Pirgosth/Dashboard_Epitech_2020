package io.github.dashboard.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class HttpRequestUtils {

    public static String makeRequest(String host, String path, Map<String, String> parameters) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost(host)
                .setPath(path);
        Set<String> set = parameters.keySet();
        for (String parameter : set)
            builder.setParameter(parameter, parameters.get(parameter));
        URI uri = builder.build();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpget);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

}
