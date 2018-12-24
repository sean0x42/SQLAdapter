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
        throw new IllegalArgumentException("Unknown case: " + target.toString());
    } 
  }

  /**
   * Converts a string to snake_case.
   * @param string String to convert to snake_case.
   * @return A snake_case string.
   */
  public static String toSnakeCase(String string) {
    string = string.replaceAll("(.)([A-Z])([a-z])", "$1_$2$3");
    string = string.replaceAll("[ -]", "_");
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
