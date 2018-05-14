package io.seanbailey.util;

import java.util.Arrays;

public class StringUtil {


  /**
   * Strings that cannot be pluralised.
   */
  private static final String[] UNCOUNTABLE = {
      "equipment", "information", "rice", "money", "species", "series", "fish", "sheep", "jeans",
      "bison", "milk", "wheat", "sunshine", "snow", "sleep"
  };


  /**
   * Pluralises the given string. E.g. cow becomes cows and sheep become sheep.
   *
   * @param singular The singular form of a word.
   * @return The plural form of the given word.
   */
  public static String toPluralForm(String singular) {

    // Check against list of uncountable strings
    if (Arrays.asList(UNCOUNTABLE).contains(singular)) {
      return singular;
    }

    // This could be way more sophisticated, but there is no need for this assignment.
    return singular + "s";

  }


  /**
   * Turns a column name into a getter.
   *
   * @param column Column name.
   * @return Name of the corresponding getter function.
   */
  public static String toGetter(String column) {
    return "get" + column.substring(0, 1).toUpperCase() + column.substring(1);
  }


  /**
   * Turns a column name into a setter.
   *
   * @param column Column name.
   * @return Name of the corresponding setter.
   */
  public static String toSetter (String column) {
    return "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
  }

}
