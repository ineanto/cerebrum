package net.artelnatif.cerebrum.command.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 28/02/2020
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String label();

    String[] alias() default {};
}
