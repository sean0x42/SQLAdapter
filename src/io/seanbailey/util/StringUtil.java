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

}
