package com.cybercom.logging.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * The Interface Logged may be used to mark that particular should not be logged if by default all public methods are
 * logged or to particular method parameter to exclude from logging
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface NotLogged {

}
