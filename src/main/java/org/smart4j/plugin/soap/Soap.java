package org.smart4j.plugin.soap;

/**
 * Created by shijiapeng on 17/1/9.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shijiapeng on 17/1/9.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Soap {
    String value() default "";
}
