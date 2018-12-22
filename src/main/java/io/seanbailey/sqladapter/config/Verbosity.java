package io.seanbailey.sqladapter.config;

/**
 * An enum which represents the logging verbosity used by the library.
 */
public enum Verbosity {

  /**
   * Don't output anything to the console.
   */
  SILENT,

  /**
   * Only output generated SQL.
   */
  SQL_ONLY
}
