package ar.com.aw.bbva.rest.security;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({METHOD})
@Retention(RUNTIME)
public @interface ModulosPermisosPermitidos {
	ModuloPermisosPermitidos[] modulosPermisos() default {};
}
