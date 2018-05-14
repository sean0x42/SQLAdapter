package io.seanbailey.adapter.exception;

/**
 * An exception that was encountered whilst mapping from SQL to Java, or vice versa.
 *
 * @author Sean Bailey
 * @since 2018-05-13
 */
public class SQLAdapterException extends Throwable {

  /**
   * Constructs a new SQL Mapping Exception.
   *
   * @param message Error message.
   */
  public SQLAdapterException(String message) {
    super(message);
  }

}
