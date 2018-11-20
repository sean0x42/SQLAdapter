package io.seanbailey.sqladapter;

/**
 * A class for configuring the operation of the SQLAdapter library.
 */
public class SQLAdapter {

  private static Verbosity verbosity = Verbosity.SILENT;
  private static Convention tableNamingConvention = Convention.SNAKE_CASE;
  private static Convention columnNamingConvention = Convention.SNAKE_CASE;

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

  public static Convention getTableNamingConvention() {
    return tableNamingConvention;
  }

  /**
   * Sets the convention used for naming SQL tables.
   *
   * <p>
   * This option configures how the adapter infers table names from the class
   * name of a model.
   * </p>
   *
   * @see io.seanbailey.sqladapter.Convention
   * @param convention Table naming convention.
   */
  public static void setTableNamingConvention(Convention convention) {
    SQLAdapter.tableNamingConvention = convention;
  }

  public static Convention getColumnNamingConvention() {
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
   * @see io.seanbailey.sqladapter.Convention
   * @param convention Column naming convention.
   */
  public static void setColumnNamingConvention(Convention convention) {
    SQLAdapter.columnNamingConvention = convention;
  }
}
