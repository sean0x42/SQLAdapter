package io.seanbailey.adapter.annotation;

import io.seanbailey.adapter.Model;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that denotes a primary key. Used in update operations.
 *
 * @author Sean Bailey
 * @see Model
 * @since 2018-05-14
 */
@SuppressWarnings("unused")
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey { }
