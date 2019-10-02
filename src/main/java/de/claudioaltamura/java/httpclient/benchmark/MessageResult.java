package de.claudioaltamura.java.httpclient.benchmark;

import java.util.Objects;

public class MessageResult {

  private final int statusCode;

  private final String cookieId;

  public MessageResult(int statusCode, String cookieId) {
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
    MessageResult other = (MessageResult) obj;
    return Objects.equals(cookieId, other.cookieId) && statusCode == other.statusCode;
  }

  @Override
  public String toString() {
    return "MessageResult [statusCode=" + statusCode + ", cookieId=" + cookieId + "]";
  }
}
