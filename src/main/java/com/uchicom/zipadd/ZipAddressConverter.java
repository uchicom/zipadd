// (C) 2021 uchicom
package com.uchicom.zipadd;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * 郵便番号住所変換.<br>
 * 日本郵便のWEBを読み込みます.
 */
public class ZipAddressConverter {

  /**
   * [都道府県][市区町村][町域]を連結した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 連結した住所
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String convertAddress(String zipCode) throws IOException {
    var address = getAddress(zipCode);
    if (address != null) {
      return address.replaceAll(" ", "");
    }
    return null;
  }

  /**
   * [都道府県][市区町村]と[町域]を配列にした住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 都道府県市区町村と町域で分割した配列
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String[] convertSplitAddress(String zipCode) throws IOException {
    var result = getAddress(zipCode);
    if (result != null) {
      return result.split("  ");
    }
    return null;
  }

  public String getAddress(String zipCode) throws IOException {
    var extractAddress = getExtractAddress(zipCode);
    if (extractAddress != null) {
      if (extractAddress.endsWith("  他に掲載がない場合")) {
        return extractAddress.substring(0, extractAddress.length() - 11);
      } else {
        return extractAddress;
      }
    }
    return null;
  }
  /**
   * 日本郵便のWEBサイトから抽出した住所を取得します.
   *
   * @param zipCode 郵便番号
   * @return 日本郵便のWEBサイトから抽出した住所
   * @throws IOException 日本郵便のWEBサイト参照時にエラーがあった場合
   */
  public String getExtractAddress(String zipCode) throws IOException {
    var url = new URL("https://www.post.japanpost.jp/kt/zip/e2.cgi?z=" + zipCode + "&xr=1");
    var html = getHtml(url);
    var pattern = Pattern.compile("<BR>.*<BR>(.*)<BR><BR><font");
    var matcher = pattern.matcher(html);
    if (matcher.find() && matcher.groupCount() == 1) {
      return matcher.group(1);
    }
    return null;
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
    con.setRequestProperty("Accept-Charset", "Shift_JIS,*;q=0.5");
    con.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
    con.setRequestProperty("User-Agent", "zipadd/0.0.1");

    try (var is = con.getInputStream()) {
      return new String(is.readAllBytes(), "MS932");
    }
  }
}
