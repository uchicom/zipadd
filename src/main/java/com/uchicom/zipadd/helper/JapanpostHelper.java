// (C) 2026 uchicom
package com.uchicom.zipadd.helper;

import com.uchicom.zipadd.exception.ZipAddException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class JapanpostHelper extends AbstractHttpClientHelper {

  public JapanpostHelper(HttpClient httpClient) {
    super(httpClient, 200);
  }

  public String getAddress(String zipCode) throws ZipAddException {
    var newRequest =
        HttpRequest.newBuilder()
            .uri(createUri("https://www.post.japanpost.jp/cgi-zip/zipcode.php?zip=" + zipCode))
            .GET()
            .build();
    try {
      return send(newRequest, BodyHandlers.ofString()).body();
    } catch (IOException | InterruptedException | ZipAddException e) {
      throw new ZipAddException(e);
    }
  }
}
