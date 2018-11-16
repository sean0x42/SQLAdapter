package io.seanbailey.sqladapter.util;

import io.seanbailey.sqladapter.Model;

/**
 * A set of utility functions for performing reflection.
 */
public class ReflectionUtils {

  /**
   * Attempts to infer the class of a calling model.
   * Note that this should always be called directly after calling a model
   * function.
   * @return The fully qualified class name of the calling model.
   */
  public static String inferClass() {
    // Retrieve stack trace
    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    for (StackTraceElement el : elements) {
      if (!el.getClassName().equals(ReflectionUtils.class.getName()) &&
          el.getClassName().indexOf("java.lang.Thread") != 0) {
        return el.getClassName();
      }
    }

    return null;
  }
}
