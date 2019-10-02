package de.claudioaltamura.java.httpclient.benchmark;

import java.util.Arrays;
import java.util.Objects;

public class Message {

  private final String cookieId;

  private final String endpoint;

  private final byte[] payload;

  private final int ttl;

  public Message(String cookieId, String endpoint, byte[] payload, int ttl) {
    super();
    this.cookieId = cookieId;
    this.endpoint = endpoint;
    this.payload = payload;
    this.ttl = ttl;
  }

  public String getCookieId() {
    return cookieId;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public byte[] getPayload() {
    return payload;
  }

  public int getTtl() {
    return ttl;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(payload);
    result = prime * result + Objects.hash(cookieId, endpoint, ttl);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Message other = (Message) obj;
    return Objects.equals(cookieId, other.cookieId)
        && Objects.equals(endpoint, other.endpoint)
        && Arrays.equals(payload, other.payload)
        && ttl == other.ttl;
  }

  @Override
  public String toString() {
    return "Message [cookieId="
        + cookieId
        + ", endpoint="
        + endpoint
        + ", payload="
        + Arrays.toString(payload)
        + ", ttl="
        + ttl
        + "]";
  }
}
