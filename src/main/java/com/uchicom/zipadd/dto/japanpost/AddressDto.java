// (C) 2026 uchicom
package com.uchicom.zipadd.dto.japanpost;

public class AddressDto {
  public String dgacode;

  public String zip_code;

  public String pref_code;

  public String pref_name;

  public String pref_kana;

  public String pref_roma;

  public String city_code;

  public String city_name;

  public String city_kana;

  public String city_roma;

  public String town_name;

  public String town_kana;

  public String town_roma;

  public String biz_name;

  public String biz_kana;

  public String biz_roma;

  public String block_name;

  public String other_name;

  public String corporate_number;

  public String business_name;

  public String business_name_kana;

  public String tel_number;

  public String url;

  public String address;

  public Double longitude;

  public Double latitude;

  @Override
  public String toString() {
    return "AddressDto [dgacode="
        + dgacode
        + ", zip_code="
        + zip_code
        + ", pref_code="
        + pref_code
        + ", pref_name="
        + pref_name
        + ", pref_kana="
        + pref_kana
        + ", pref_roma="
        + pref_roma
        + ", city_code="
        + city_code
        + ", city_name="
        + city_name
        + ", city_kana="
        + city_kana
        + ", city_roma="
        + city_roma
        + ", town_name="
        + town_name
        + ", town_kana="
        + town_kana
        + ", town_roma="
        + town_roma
        + ", biz_name="
        + biz_name
        + ", biz_kana="
        + biz_kana
        + ", biz_roma="
        + biz_roma
        + ", block_name="
        + block_name
        + ", other_name="
        + other_name
        + ", corporate_number="
        + corporate_number
        + ", business_name="
        + business_name
        + ", business_name_kana="
        + business_name_kana
        + ", tel_number="
        + tel_number
        + ", url="
        + url
        + ", address="
        + address
        + ", longitude="
        + longitude
        + ", latitude="
        + latitude
        + "]";
  }
}
