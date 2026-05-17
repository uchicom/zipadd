// (C) 2026 uchicom
package com.uchicom.zipadd.exception;

public class ZipAddException extends Exception {
  public ZipAddException(String message) {
    super(message);
  }

  public ZipAddException(String message, Throwable cause) {
    super(message, cause);
  }

  public ZipAddException(Throwable t) {
    super(t);
  }
}
