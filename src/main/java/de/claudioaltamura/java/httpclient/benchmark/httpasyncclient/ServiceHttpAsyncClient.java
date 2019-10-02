package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHeader;
import de.claudioaltamura.java.httpclient.benchmark.Message;

public class ServiceHttpAsyncClient {

  private CloseableHttpAsyncClient httpClient;

  public ServiceHttpAsyncClient(CloseableHttpAsyncClient httpClient) {
    this.httpClient = httpClient;
  }

  public Future<HttpResponse> sendHttpAsyncClientWithPoolableClientAndFutureCallback(
      Message message, FutureCallback<HttpResponse> futureCallback) throws IOException {
    HttpPost httpPost = preparePost(message);
    return httpClient.execute(httpPost, HttpClientContext.create(), futureCallback);
  }

  public List<ThreadResult> sendHttpAsyncClientWithPoolableClientAndThreads(List<Message> messages)
      throws InterruptedException {
    HttpPostThread[] threads = new HttpPostThread[messages.size()];
    for (int i = 0; i < threads.length; i++) {
      Message message = messages.get(i);
      HttpPost request = preparePost(messages.get(i));
      threads[i] = new HttpPostThread(httpClient, request, message.getCookieId());
    }

    for (HttpPostThread thread : threads) {
      thread.start();
    }

    List<ThreadResult> threadResults = new ArrayList<>();
    for (HttpPostThread thread : threads) {
      thread.join();
      threadResults.add(new ThreadResult(thread.getStatusCode(), thread.getCookieId()));
    }

    return threadResults;
  }

  private HttpPost preparePost(Message message) {
    HttpPost httpPost = new HttpPost(message.getEndpoint());
    httpPost.addHeader("TTL", String.valueOf(message.getTtl()));

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/octet-stream");
    headers.put("accept", "application/json");
    httpPost.setEntity(new ByteArrayEntity(message.getPayload()));

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
    }

    return httpPost;
  }
}
