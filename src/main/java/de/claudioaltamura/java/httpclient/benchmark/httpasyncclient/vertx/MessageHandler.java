package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;

public class MessageHandler implements Handler<AsyncResult<HttpResponse<Buffer>>> {
  @Override
  public void handle(AsyncResult<HttpResponse<Buffer>> event) {
    // TODO Auto-generated method stub
    if (event.succeeded()) {
      HttpResponse<Buffer> response = event.result();
      System.out.println("statusCode " + response.statusCode());
    }
  }
}
