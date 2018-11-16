package io.seanbailey.sqladapter;

/**
 * A class for configuring the operation of the SQLAdapter library.
 */
public class SQLAdapter {

  private static Verbosity verbosity = Verbosity.SILENT;

  public static Verbosity getVerbosity() {
    return verbosity;
  }

  public static void setVerbosity(Verbosity verbosity) {
    SQLAdapter.verbosity = verbosity;
  }
}
