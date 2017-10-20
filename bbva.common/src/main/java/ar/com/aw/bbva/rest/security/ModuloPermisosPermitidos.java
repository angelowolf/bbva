package ar.com.aw.bbva.rest.security;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({})
@Retention(RUNTIME)
public @interface ModuloPermisosPermitidos {
	
	String modNombre();
	ModuloPermiso modPermiso();
}
