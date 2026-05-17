// (C) 2021 uchicom
package com.uchicom.zipadd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ZipAddressConverterTest {

  ZipAddressConverter zipAddressConverter;
  String html;

  AutoCloseable closeable;

  @BeforeEach
  public void openMocks() {
    closeable = MockitoAnnotations.openMocks(this);
    zipAddressConverter = Mockito.spy(ZipAddressConverter.class);
  }

  @AfterEach
  public void releaseMocks() throws Exception {
    closeable.close();
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

  private void mock4411324() throws Exception {
    mock("./src/test/resources/4411324.html");
  }

  private void mock4980000() throws Exception {
    mock("./src/test/resources/4980000.html");
  }

  private void mock6180000() throws Exception {
    mock("./src/test/resources/6180000.html");
  }

  private void mock8710000() throws Exception {
    mock("./src/test/resources/8710000.html");
  }

  private void mock(String path) throws Exception {
    try (FileInputStream fis = new FileInputStream(new File(path))) {
      html = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
      Mockito.doReturn(html).when(zipAddressConverter).getHtml(Mockito.any());
    }
  }

  @Test
  public void convertAddress() throws Exception {
    assertThat(zipAddressConverter.convertAddress("2510025")).isEqualTo("神奈川県藤沢市鵠沼石上");
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
  public void convertAddress4411324() throws Exception {
    mock4411324();
    assertThat(zipAddressConverter.convertAddress("4411324")).isEqualTo("愛知県新城市井道");
  }

  @Test
  public void convertAddresses4411324() throws Exception {
    mock4411324();
    assertThat(zipAddressConverter.convertAddresses("4411324")[0]).isEqualTo("愛知県新城市井道");
    assertThat(zipAddressConverter.convertAddresses("4411324")[1]).isEqualTo("愛知県新城市奥井道");
    assertThat(zipAddressConverter.convertAddresses("4411324")[2]).isEqualTo("愛知県新城市内井道");
  }

  @Test
  public void convertAddress4980000() throws Exception {
    mock4980000();
    assertThat(zipAddressConverter.convertAddress("4980000")).isEqualTo("三重県桑名郡木曽岬町");
  }

  @Test
  public void convertAddresses4980000() throws Exception {
    mock4980000();
    assertThat(zipAddressConverter.convertAddresses("4980000")[0]).isEqualTo("三重県桑名郡木曽岬町");
    assertThat(zipAddressConverter.convertAddresses("4980000")[1]).isEqualTo("愛知県弥富市");
  }

  @Test
  public void convertAddress6180000() throws Exception {
    mock6180000();
    assertThat(zipAddressConverter.convertAddress("6180000")).isEqualTo("京都府乙訓郡大山崎町");
  }

  @Test
  public void convertAddresses6180000() throws Exception {
    mock6180000();
    assertThat(zipAddressConverter.convertAddresses("6180000")[0]).isEqualTo("京都府乙訓郡大山崎町");
    assertThat(zipAddressConverter.convertAddresses("6180000")[1]).isEqualTo("大阪府三島郡島本町");
  }

  @Test
  public void convertAddress8710000() throws Exception {
    mock8710000();
    assertThat(zipAddressConverter.convertAddress("8710000")).isEqualTo("大分県中津市");
  }

  @Test
  public void convertAddresses8710000() throws Exception {
    mock8710000();
    assertThat(zipAddressConverter.convertAddresses("8710000")[0]).isEqualTo("大分県中津市");
    assertThat(zipAddressConverter.convertAddresses("8710000")[1]).isEqualTo("福岡県築上郡吉富町");
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
    var list = zipAddressConverter.getAddress("2510025");
    assertThat(list).hasSize(1);
    assertThat(list.get(0).prefecture).isEqualTo("神奈川県");
    assertThat(list.get(0).city).isEqualTo("藤沢市");
    assertThat(list.get(0).area).isEqualTo("鵠沼石上");
  }
}
