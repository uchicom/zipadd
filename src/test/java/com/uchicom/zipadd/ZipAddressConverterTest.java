// (C) 2021 uchicom
package com.uchicom.zipadd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ZipAddressConverterTest {

  ZipAddressConverter zipAddressConverter;
  String html;

  @BeforeEach
  public void setUp() throws Exception {

    zipAddressConverter = Mockito.spy(ZipAddressConverter.class);
  }

  private void mock2510025() throws Exception {
    mock("./src/test/resources/2510025.html");
  }

  private void mock2510000() throws Exception {
    mock("./src/test/resources/2510000.html");
  }

  private void mock1000005() throws Exception {
    mock("./src/test/resources/1000005.html");
  }

  private void mock(String path) throws Exception {
    try (FileInputStream fis = new FileInputStream(new File(path))) {
      html = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
      Mockito.doReturn(html).when(zipAddressConverter).getHtml(Mockito.any());
    }
  }

  @Test
  public void convertAddress2510025() throws Exception {
    mock2510025();
    assertThat(zipAddressConverter.convertAddress("2510025")).isEqualTo("神奈川県藤沢市鵠沼石上");
  }

  @Test
  public void convertAddress2510000() throws Exception {
    mock2510000();
    assertThat(zipAddressConverter.convertAddress("2510000")).isEqualTo("神奈川県藤沢市");
  }

  @Test
  public void convertAddress1000005() throws Exception {
    mock1000005();
    assertThat(zipAddressConverter.convertAddress("1000005")).isEqualTo("東京都千代田区丸の内");
  }

  @Test
  public void convertSplitAddress2510000() throws Exception {
    mock2510000();
    var result = zipAddressConverter.convertSplitAddress("2510000");
    assertThat(result).hasSize(2);
    assertThat(result[0]).isEqualTo("神奈川県");
    assertThat(result[1]).isEqualTo("藤沢市");
  }

  @Test
  public void convertSplitAddress2510025() throws Exception {
    mock2510025();
    var result = zipAddressConverter.convertSplitAddress("2510025");
    assertThat(result).hasSize(3);
    assertThat(result[0]).isEqualTo("神奈川県");
    assertThat(result[1]).isEqualTo("藤沢市");
    assertThat(result[2]).isEqualTo("鵠沼石上");
  }

  @Test
  public void getAddress() throws Exception {
    mock2510025();
    var address = zipAddressConverter.getAddress("2510025");
    assertThat(address.prefecture).isEqualTo("神奈川県");
    assertThat(address.city).isEqualTo("藤沢市");
    assertThat(address.area).isEqualTo("鵠沼石上");
  }

  @Test
  public void getHtml() throws Exception {
    mock2510025();
    var zipAddressConverter = new ZipAddressConverter();
    var url = Mockito.mock(URL.class);
    var urlConnection = Mockito.mock(URLConnection.class);
    Mockito.doReturn(urlConnection).when(url).openConnection();
    Mockito.doReturn(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)))
        .when(urlConnection)
        .getInputStream();

    assertThat(zipAddressConverter.getHtml(url)).isEqualTo(html);
  }
}
