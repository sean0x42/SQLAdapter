package io.seanbailey.sqladapter;

import io.seanbailey.sqladapter.config.Case;

/**
 * A utility class for adapting models into SQL and back again.
 */
class Adapter {

  /**
   * Infers the table name of a given class.
   * The resulting name will conform to the configured table name case
   * convention.
   * @param clazz Class to infer name of.
   * @return Name of the class's table.
   */
  static String inferTableName(Class<? extends Model> clazz) {
    return inferTableName(clazz.getSimpleName());
  }

  /**
   * Infers the table name of a given class.
   * The resulting name will conform to the configured table name case
   * convention.
   * @param name Name of the class. Should be formatted as camel case.
   * @return Name of the table.
   */
  static String inferTableName(String name) {
    Case caseFormat = SQLAdapter.getTableNamingConvention();
    switch (caseFormat) {
      case CAMEL:
        return name; // Class names are already in CamelCase
      default:
        return Case.convertTo(caseFormat, name);
    }
  }
}
