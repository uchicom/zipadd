// (C) 2021 uchicom
package com.uchicom.zipadd;

import com.uchicom.zipadd.dto.AddressDto;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * 郵便番号住所変換.<br>
 * 日本郵便のWEBを読み込みます.
 */
public class ZipAddressConverter {

  @Inject
  public ZipAddressConverter() {}

  /**
   * [都道府県][市区町村][町域]を連結した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 連結した住所
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String convertAddress(String zipCode) throws IOException {
    var addressList = getAddress(zipCode);
    if (addressList.isEmpty()) {
      return null;
    }
    var address = addressList.get(0);
    return address.toString();
  }

  public String[] convertAddresses(String zipCode) throws IOException {
    var addressList = getAddress(zipCode);
    if (addressList.isEmpty()) {
      return new String[0];
    }
    return (String[])
        addressList.stream()
            .map(address -> address.toString())
            .collect(Collectors.toList())
            .toArray(new String[0]);
  }

  /**
   * [都道府県]と[市区町村]と[町域]を配列にした住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 都道府県と市区町村と町域で分割した配列
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String[] convertSplitAddress(String zipCode) throws IOException {
    var addressList = getAddress(zipCode);
    if (addressList.isEmpty()) {
      return null;
    }
    var address = addressList.get(0);
    if (address.area == null) {
      return new String[] {address.prefecture, address.city};
    }
    return new String[] {address.prefecture, address.city, address.area};
  }

  /**
   * 日本郵便のWEBサイトから抽出した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 日本郵便のWEBサイトから抽出した住所
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public List<AddressDto> getAddress(String zipCode) throws IOException {
    var url =
        URI.create("https://www.post.japanpost.jp/cgi-zip/zipcode.php?zip=" + zipCode).toURL();
    var html = getHtml(url);
    var pattern =
        Pattern.compile(
            "<small>"
                + zipCode.substring(0, 3)
                + "-"
                + zipCode.substring(3)
                + "</small></td>\\s+<td class=\"data\"><small>(.+)</small></td>\\s+<td class=\"data\"><small>(.+)</small></td>\\s+<td>\\s+<div class=\"data\">\\s+<p><small><a class=\"line\" href=\"zipcode.php\\?pref=([0-9]+)&city=([0-9]+)+&id=([0-9]+)&merge=\">(.+)</a></small></p>");
    var matcher = pattern.matcher(html);
    var list = new ArrayList<AddressDto>(5);
    while (matcher.find()) {
      var address = new AddressDto();
      address.prefecture = matcher.group(1);
      address.city = matcher.group(2);
      address.area = getArea(matcher.group(6));
      list.add(address);
    }
    return list;
  }

  String getArea(String area) {
    if (area.contains("以下に掲載がない場合")) {
      return null;
    }
    var index = area.indexOf("（次のビルを除く");
    if (index > -1) {
      return area.substring(0, index);
    }
    return area;
  }

  /**
   * 日本郵便のWEBサイトのテキストデータを取得します.
   *
   * @param url 日本郵便のWEBサイトURL
   * @return 日本郵便のWEBサイトのテキストデータ
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String getHtml(URL url) throws IOException {
    var con = url.openConnection();
    con.setRequestProperty("accept-encoding", "gzip, deflate");
    con.setRequestProperty("Accept-Language", "ja,en-US;q=0.9,en;q=0.8");
    con.setRequestProperty("User-Agent", "zipadd/0.0.1");

    try (var is = con.getInputStream()) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
