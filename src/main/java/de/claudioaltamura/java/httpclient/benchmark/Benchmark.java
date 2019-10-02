package de.claudioaltamura.java.httpclient.benchmark;

import de.claudioaltamura.java.httpclient.benchmark.httpasyncclient.CloseableHttpAsyncClientFactory;
import de.claudioaltamura.java.httpclient.benchmark.httpasyncclient.ServiceHttpAsyncClient;
import de.claudioaltamura.java.httpclient.benchmark.httpasyncclient.conventional.ServiceConventional;
import de.claudioaltamura.java.httpclient.benchmark.httpclient.ServiceHttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class Benchmark {

  private long start;
  private int number = 1000;

  public static void main(String[] args)
      throws InterruptedException, ExecutionException, IOException {
    Benchmark benchmark = new Benchmark();

    benchmark.httpAsyncClientWithPoolableClientAndFutureCallback();
    benchmark.HttpAsyncClientWithPoolableClientAndThreads();
    benchmark.httpClientWithExecutorService();
    benchmark.conventionalHttpClientWithExecutorService();

    System.exit(0);
  }

  private void httpAsyncClientWithPoolableClientAndFutureCallback()
      throws InterruptedException, ExecutionException, IOException {
    before("httpAsyncClientWithPoolableClientAndFutureCallback");

    ServiceHttpAsyncClient service =
        new ServiceHttpAsyncClient(CloseableHttpAsyncClientFactory.createConfigurableClient());
    for (int i = 0; i < number; i++) {
      String payload = "helloworld=" + i;
      printProgress(i);
      Message message =
          new Message(randomCookieId(), "http://httpbin.org/post", payload.getBytes(), 500);
      service.sendHttpAsyncClientWithPoolableClientAndFutureCallback(
          message,
          new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse result) {}

            @Override
            public void failed(Exception ex) {}

            @Override
            public void cancelled() {}
          });
    }

    after();
  }

  private void HttpAsyncClientWithPoolableClientAndThreads()
      throws InterruptedException, ExecutionException, IOException {
    before("httpAsyncClientWithPoolableClientAndThreads");

    ServiceHttpAsyncClient service =
        new ServiceHttpAsyncClient(CloseableHttpAsyncClientFactory.createConfigurableClient());
    List<Message> messages = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      String payload = "helloworld=" + i;
      printProgress(i);
      Message message =
          new Message(randomCookieId(), "http://httpbin.org/post", payload.getBytes(), 500);
      messages.add(message);
    }
    service.sendHttpAsyncClientWithPoolableClientAndThreads(messages);

    after();
  }

  private void httpClientWithExecutorService() throws InterruptedException {
    before("httpClientWithExecutorService");

    ServiceHttpClient service = new ServiceHttpClient();
    List<CompletableFuture<java.net.http.HttpResponse<String>>> results = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      String payload = "helloworld=" + i;
      printProgress(i);
      Message message =
          new Message(randomCookieId(), "http://httpbin.org/post", payload.getBytes(), 500);
      results.add(service.send(message));
    }

    after();

    for (CompletableFuture<java.net.http.HttpResponse<String>> result : results)
      result
          .thenApply(java.net.http.HttpResponse::statusCode)
          .thenAccept(
              s -> {
                if (s != 200) System.out.println("statusCode: " + s);
              });
  }

  private void conventionalHttpClientWithExecutorService() throws InterruptedException {
    before("conventionalHttpClientWithExecutorService");

    ServiceConventional service = new ServiceConventional();
    List<Future<MessageResult>> messageResults = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      String payload = "helloworld=" + i;
      printProgress(i);
      Message message =
          new Message(randomCookieId(), "http://httpbin.org/post", payload.getBytes(), 500);
      messageResults.add(service.send(message));
    }

    after();

    for (Future<MessageResult> messageResult : messageResults) {
      if (messageResult.isDone()) {
        try {
          messageResult.get(100, TimeUnit.MILLISECONDS);
        } catch (final CancellationException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        } catch (TimeoutException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void before(String name) {
    System.out.println("\n");
    System.out.println("benchmark: " + name);
    start = System.currentTimeMillis();
  }

  private void after() {
    long duration = System.currentTimeMillis() - start;
    System.out.println("\n");
    System.out.println("number for request: " + number);
    System.out.println("duration per request: " + duration / number + " ms");
    float totalDurationSec = (float) duration / 1000;
    System.out.println("total duration: " + totalDurationSec + " sec");
    float totalDurationMinutes = (float) duration / 1000 / 60;
    System.out.println("total duration: " + totalDurationMinutes + " min");
    System.out.println("requests/sec: " + (float) (number / totalDurationSec));
  }

  private void printProgress(int i) {
    // if (i % (number / 10) == 0)
    // System.out.print("\n");
    // System.out.print(".");
  }

  private String randomCookieId() {
    return RandomStringUtils.random(32, true, true);
  }
}
