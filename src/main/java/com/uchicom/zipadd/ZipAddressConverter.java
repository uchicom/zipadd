// (C) 2021 uchicom
package com.uchicom.zipadd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
   * @throws IOException
   */
  public String[] convertSplitAddress(String zipCode) throws IOException {
    var result = getAddress(zipCode);
    if (result != null) {
      return result.split("  ");
    }
    return null;
  }

  public String getAddress(String zipCode) throws IOException {
    var url = new URL("https://www.post.japanpost.jp/kt/zip/e2.cgi?z=" + zipCode + "&xr=1");
    var html = getHtml(url);
    var pattern = Pattern.compile("<BR>.*<BR>(.*)<BR><BR><font");
    var matcher = pattern.matcher(html);
    if (matcher.find() && matcher.groupCount() == 1) {
      return matcher.group(1);
    }
    return null;
  }

  public String getHtml(URL url) throws IOException {
    URLConnection con = url.openConnection();
    con.setRequestProperty("Accept-Charset", "Shift_JIS,*;q=0.5");
    con.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
    con.setRequestProperty("User-Agent", "zipadd/0.0.1");

    try (InputStream is = con.getInputStream()) {
      return new String(is.readAllBytes(), "MS932");
    }
  }
}
