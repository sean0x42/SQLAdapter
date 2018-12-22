package io.seanbailey.sqladapter.config;

import java.util.regex.Pattern;

/**
 * An enum representing various possible naming conventions.
 */
public enum Case {

  /**
   * snake_case
   */
  SNAKE,

  /**
   * CamelCase
   */
  CAMEL,

  /**
   * kebab-case
   */
  KEBAB;

  /**
   * Converts a string to the given case.
   * @param target Target case.
   * @param string String to convert.
   * @return Converted string.
   */
  public static String convertTo(Case target, String string) {
    switch (target) {
      case SNAKE:
        return toSnakeCase(string);
      case CAMEL:
        return toCamelCase(string);
      case KEBAB:
        return toKebabCase(string);
      default:
        return null;
    } 
  }

  /**
   * Converts a string to snake_case.
   * @param string String to convert to snake_case.
   * @return A snake_case string.
   */
  public static String toSnakeCase(String string) {
    string = Pattern.compile("(.)([A-Z][a-z]+)").matcher(string).replaceAll(match -> {
      return match.group(0) + "_" + match.group(1);
    });
    string = string.replaceAll(" -", "_");
    return string.toLowerCase();
  }

  /**
   * Converts a string to CamelCase.
   * @param string String to convert to CamelCase.
   * @return A CamelCase string.
   */
  public static String toCamelCase(String string) {
    return string;
  }

  /**
   * Converts a string to kebab-case.
   * @param string String to convert to kebab-case.
   * @return A kebab-case string.
   */
  public static String toKebabCase(String string) {
    string.replaceAll(" _", "-");
    return string.toLowerCase();
  }
}
