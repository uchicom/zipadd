// (C) 2021 uchicom
package com.uchicom.zipadd;

import com.uchicom.zipadd.dto.AddressDto;
import com.uchicom.zipadd.exception.ZipAddException;
import com.uchicom.zipadd.helper.JapanpostHelper;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 郵便番号住所変換.<br>
 * 日本郵便のWEBを読み込みます.
 */
public class ZipAddressConverter {

  public ZipAddressConverter() {}

  /**
   * [都道府県][市区町村][町域]を連結した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 連結した住所
   * @throws ZipAddException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String convertAddress(String zipCode) throws ZipAddException {
    var addressList = getAddress(zipCode);
    if (addressList.isEmpty()) {
      return null;
    }
    var address = addressList.get(0);
    return address.toString();
  }

  public String[] convertAddresses(String zipCode) throws ZipAddException {
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
   * @throws ZipAddException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String[] convertSplitAddress(String zipCode) throws ZipAddException {
    var addressList = getAddress(zipCode);
    if (addressList.isEmpty()) {
      return null;
    }
    var address = addressList.get(0);
    if (address.area == null || address.area.isBlank()) {
      return new String[] {address.prefecture, address.city};
    }
    return new String[] {address.prefecture, address.city, address.area};
  }

  /**
   * 日本郵便のWEBサイトから抽出した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 日本郵便のWEBサイトから抽出した住所
   * @throws ZipAddException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public List<AddressDto> getAddress(String zipCode) throws ZipAddException {
    var html = getHtml(zipCode);
    var pattern =
        Pattern.compile(
            "window.__RESULT__\\s*=\\s*\\{\\s*zip:\\s*\".*\",\\s*pref:\\s*\"(.*)\",\\s*city:\\s*\"(.*)\",\\s*addr: \"(.*)\",\\s*addr_kana:\\s*\"(.*)\"\\s*\\};");
    var matcher = pattern.matcher(html);
    var list = new ArrayList<AddressDto>(5);
    if (matcher.find()) {
      var prefecture = matcher.group(1);
      var city = matcher.group(2);
      var area = getArea(matcher.group(3)).replace("掲載がない場合", "");
      for (var areaName : area.split("、")) {
        var address = new AddressDto();
        address.prefecture = prefecture;
        address.city = city;
        address.area = areaName;
        list.add(address);
      }
    } else {
      var pattern2 = Pattern.compile(">\\s*([^<]+[都道府県])([^<]+[市区町村])([^<]*)\\s*<");
      var matcher2 = pattern2.matcher(html);
      while (matcher2.find()) {
        var address = new AddressDto();
        address.prefecture = matcher2.group(1);
        address.city = matcher2.group(2);
        address.area = matcher2.group(3);
        list.add(address);
      }
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
   * @param zipCode 郵便番号
   * @return 日本郵便のWEBサイトのテキストデータ
   * @throws ZipAddException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String getHtml(String zipCode) throws ZipAddException {
    try (var httpClient = HttpClient.newHttpClient()) {
      var helper = new JapanpostHelper(httpClient);
      return helper.getAddress(zipCode);
    }
  }
}
