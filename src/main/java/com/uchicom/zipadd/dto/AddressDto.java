// (C) 2025 uchicom
package com.uchicom.zipadd.dto;

public class AddressDto {
  public String prefecture;
  public String city;
  public String area;

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append(prefecture).append(city);
    if (area != null) {
      builder.append(area);
    }
    return builder.toString();
  }
}
