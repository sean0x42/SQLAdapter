package io.seanbailey.sql.util;

import io.seanbailey.util.StringUtil;
import io.seanbailey.sql.Model;

public class SQLUtil {

  /**
   * Determines the table name for a given model.
   *
   * @param clazz Model class.
   * @return Table name of the given model.
   */
  public static String getTableName(Class<? extends Model> clazz) {
    return StringUtil.toPluralForm(clazz.getSimpleName());
  }

}
