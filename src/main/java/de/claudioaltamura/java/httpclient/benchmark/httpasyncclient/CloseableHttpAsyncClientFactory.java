package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient;

import java.io.IOException;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;

public class CloseableHttpAsyncClientFactory {

  public static CloseableHttpAsyncClient createConfigurableClient() throws IOException {
    IOReactorConfig ioReactorConfig =
        IOReactorConfig.custom()
            .setIoThreadCount(Runtime.getRuntime().availableProcessors())
            .setConnectTimeout(30000)
            .setSoTimeout(30000)
            .build();
    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

    PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
    cm.setMaxTotal(200);
    cm.setDefaultMaxPerRoute(20);

    CloseableHttpAsyncClient httpClient =
        HttpAsyncClients.custom().setConnectionManager(cm).build();
    httpClient.start();
    return httpClient;
  }

  public static CloseableHttpAsyncClient createSystemClient() {
    CloseableHttpAsyncClient httpClient = HttpAsyncClients.createSystem();
    httpClient.start();
    return httpClient;
  }
}
