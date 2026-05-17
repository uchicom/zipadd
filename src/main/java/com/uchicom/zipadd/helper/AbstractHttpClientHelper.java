// (C) 2026 uchicom
package com.uchicom.zipadd.helper;

import com.uchicom.zipadd.exception.ZipAddException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

public abstract class AbstractHttpClientHelper {
  private final HttpClient httpClient;
  private final int successStatusCode;

  AbstractHttpClientHelper(HttpClient httpClient, int successStatusCode) {
    this.httpClient = httpClient;
    this.successStatusCode = successStatusCode;
  }

  <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> handler)
      throws IOException, InterruptedException, ZipAddException {
    return send(request, handler, null);
  }

  <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> handler, Integer successStatusCode)
      throws ZipAddException {

    try {
      var response = httpClient.send(request, handler);
      if (response.statusCode() == 301 || response.statusCode() == 302) {
        response = redirect(request, response, handler);
      }
      checkStatus(response, successStatusCode);
      return response;
    } catch (IOException | InterruptedException e) {
      throw new ZipAddException(e);
    }
  }

  <T> HttpResponse<T> redirect(
      HttpRequest request, HttpResponse<T> response, BodyHandler<T> handler)
      throws IOException, InterruptedException {
    var location = response.headers().firstValue("Location").orElseThrow();
    var newRequest =
        HttpRequest.newBuilder()
            .uri(
                location.contains("://")
                    ? createUri(location)
                    : createUri(request.uri().getHost(), location))
            .GET()
            .build();
    return httpClient.send(newRequest, handler);
  }

  <T> void checkStatus(HttpResponse<T> response, Integer successStatusCode) throws ZipAddException {
    if (successStatusCode == null) {
      successStatusCode = this.successStatusCode;
    }
    if (response.statusCode() != successStatusCode) {
      throw new ZipAddException(
          "Unexpected status code: " + response.statusCode() + "," + response.body());
    }
  }

  URI createUri(String host, String path) {
    return URI.create("https://" + host + path);
  }

  URI createUri(String url) {
    return URI.create(url);
  }
}
