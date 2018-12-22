package io.seanbailey.sqladapter;

import io.seanbailey.sqladapter.config.Case;
import io.seanbailey.sqladapter.config.Verbosity;

/**
 * A class for configuring the operation of the SQLAdapter library.
 */
public class SQLAdapter {

  private static Verbosity verbosity = Verbosity.SILENT;
  private static Case tableNamingConvention = Case.SNAKE;
  private static Case columnNamingConvention = Case.SNAKE;

  public static Verbosity getVerbosity() {
    return verbosity;
  }

  /**
   * Sets the logging verbosity.
   *
   * <p>
   * This option configures how much information is printed to stdout, or your
   * configured output stream.
   * </p>
   *
   * <p>
   * In development environments, it is recommended to use at least
   * <code>Verbosity.SQL_ONLY</code>. For production environments, consider
   * using <code>Verbosity.SILENT</code>.
   * </p>
   *
   * @see io.seanbailey.sqladapter.Verbosity
   * @param verbosity Verbosity level.
   */
  public static void setVerbosity(Verbosity verbosity) {
    SQLAdapter.verbosity = verbosity;
  }

  public static Case getTableNamingConvention() {
    return tableNamingConvention;
  }

  /**
   * Sets the case convention used for naming SQL tables.
   *
   * <p>
   * This option configures how the adapter infers table names from the class
   * name of a model.
   * </p>
   *
   * @see io.seanbailey.sqladapter.Case
   * @param convention Table naming convention.
   */
  public static void setTableNamingConvention(Case convention) {
    SQLAdapter.tableNamingConvention = convention;
  }

  public static Case getColumnNamingConvention() {
    return columnNamingConvention;
  }

  /**
   * Sets the column naming convention.
   *
   * <p>
   * This option configures how the adapter infers column names based on the
   * names of attributes.
   * </p>
   *
   * @see io.seanbailey.sqladapter.Case
   * @param convention Column naming convention.
   */
  public static void setColumnNamingConvention(Case convention) {
    SQLAdapter.columnNamingConvention = convention;
  }
}
