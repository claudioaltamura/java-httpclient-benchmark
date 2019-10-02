package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient.conventional;

import de.claudioaltamura.java.httpclient.benchmark.Message;
import de.claudioaltamura.java.httpclient.benchmark.MessageResult;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServiceConventional {

  private final ExecutorService executor;

  public ServiceConventional() {
    executor = Executors.newCachedThreadPool();
  }

  /**
   * @see
   *     https://stackoverflow.com/questions/53664417/performance-apache-httpasyncclient-vs-multi-threaded-urlconnection
   * @param message
   * @return
   */
  public Future<MessageResult> send(Message message) {
    return executor.submit(
        () -> {
          URL requestUrl = new URL(message.getEndpoint());
          HttpURLConnection httpUrlConnection = (HttpURLConnection) requestUrl.openConnection();
          // httpUrlConnection.setReadTimeout(60 * 1000);
          // httpUrlConnection.setConnectTimeout(60 * 1000);
          httpUrlConnection.setRequestProperty("TTL", String.valueOf(message.getTtl()));
          httpUrlConnection.setRequestProperty("Content-Type", "application/octet-stream");
          httpUrlConnection.setRequestProperty("accept", "application/json");
          httpUrlConnection.setDoOutput(true);
          httpUrlConnection.setRequestMethod("POST");
          OutputStream os = httpUrlConnection.getOutputStream();
          os.write(message.getPayload());
          os.close();
          httpUrlConnection.connect();
          return new MessageResult(httpUrlConnection.getResponseCode(), message.getCookieId());
        });
  }
}
