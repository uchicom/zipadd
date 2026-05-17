// (C) 2026 uchicom
package com.uchicom.zipadd.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uchicom.zipadd.dto.japanpost.SearchCodeResponseDto;
import com.uchicom.zipadd.dto.japanpost.TokenRequestDto;
import com.uchicom.zipadd.dto.japanpost.TokenResponseDto;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class JapanpostApiHelper extends AbstractHttpClientHelper {
  private final ObjectMapper objectMapper;
  private final String host;
  private final String clientId;
  private final String secretKey;
  private final String fromIp;

  public JapanpostApiHelper(
      HttpClient httpClient,
      ObjectMapper objectMapper,
      String host,
      String clientId,
      String secretKey,
      String fromIp) {
    super(httpClient, 200);
    this.host = host;
    this.objectMapper = objectMapper;
    this.clientId = clientId;
    this.secretKey = secretKey;
    this.fromIp = fromIp;
  }

  public String getToken() throws Exception {
    var tokenGetDto = new TokenRequestDto();
    tokenGetDto.client_id = clientId;
    tokenGetDto.secret_key = secretKey;
    var newRequest =
        HttpRequest.newBuilder()
            .uri(createUri(host, "/api/v2/j/token"))
            .header("x-forwarded-for", fromIp)
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(tokenGetDto)))
            .build();
    return objectMapper.readValue(
            send(newRequest, BodyHandlers.ofString()).body(), TokenResponseDto.class)
        .token;
  }

  public SearchCodeResponseDto getAddress(String token, String searchCode) throws Exception {
    var newRequest =
        HttpRequest.newBuilder()
            .uri(createUri(host, "/api/v2/searchcode/" + searchCode))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
    return objectMapper.readValue(
        send(newRequest, BodyHandlers.ofString()).body(), SearchCodeResponseDto.class);
  }
}
