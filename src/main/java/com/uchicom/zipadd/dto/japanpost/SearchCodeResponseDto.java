// (C) 2026 uchicom
package com.uchicom.zipadd.dto.japanpost;

import java.util.List;

public class SearchCodeResponseDto {
  public Integer page;

  public Integer limit;

  public Integer count;

  public String searchtype;

  public List<AddressDto> addresses;

  @Override
  public String toString() {
    return "SearchCodeResponseDto [page="
        + page
        + ", limit="
        + limit
        + ", count="
        + count
        + ", searchtype="
        + searchtype
        + ", addresses="
        + addresses
        + "]";
  }
}
