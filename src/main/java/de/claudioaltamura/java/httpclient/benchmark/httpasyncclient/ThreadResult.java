package de.claudioaltamura.java.httpclient.benchmark.httpasyncclient;

import java.util.Objects;

public class ThreadResult {

  private final int statusCode;

  private final String cookieId;

  public ThreadResult(int statusCode, String cookieId) {
    this.statusCode = statusCode;
    this.cookieId = cookieId;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getCookieId() {
    return cookieId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cookieId, statusCode);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ThreadResult other = (ThreadResult) obj;
    return Objects.equals(cookieId, other.cookieId) && statusCode == other.statusCode;
  }

  @Override
  public String toString() {
    return "ThreadResult [statusCode=" + statusCode + ", cookieId=" + cookieId + "]";
  }
}
