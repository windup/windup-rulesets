package camelinaction;

package org.apache.camel.language.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.camel.language.LanguageAnnotation;

/**
 * Used to inject a simple expression into a field, property, method or parameter when using
 * <a href="http://camel.apache.org/bean-integration.html">Bean Integration</a>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@LanguageAnnotation(language = "simple")
public @interface Simple {
    String value();
}
