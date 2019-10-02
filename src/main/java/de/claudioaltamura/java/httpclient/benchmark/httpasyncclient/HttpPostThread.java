package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient;

import java.util.concurrent.Future;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.HttpContext;

public class HttpPostThread extends Thread {
  private CloseableHttpAsyncClient client;
  private HttpContext context;
  private HttpPost request;
  private String cookieId;
  private int statusCode;

  public HttpPostThread(CloseableHttpAsyncClient client, HttpPost request, String cookieId) {
    this.client = client;
    context = HttpClientContext.create();
    this.request = request;
    this.cookieId = cookieId;
  }

  @Override
  public void run() {
    try {
      Future<HttpResponse> future = client.execute(request, context, null);
      HttpResponse response = future.get();
      statusCode = response.getStatusLine().getStatusCode();
    } catch (Exception ex) {
      System.out.println(ex.getLocalizedMessage());
    }
  }

  public String getCookieId() {
    return cookieId;
  }

  public int getStatusCode() {
    return statusCode;
  }

}
