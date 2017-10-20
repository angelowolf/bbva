package ar.com.aw.bbva.business.exception;

import io.jsonwebtoken.SignatureException;

public class AccessTokenExeption extends RuntimeException {

	private static final long serialVersionUID = 5951469241163204874L;

	public AccessTokenExeption(Throwable cause) {
		super("No se pudo obtener el token de autentificación del usuario",cause);
	}	
	
	public AccessTokenExeption(SignatureException cause) {
		super("El token utilizado no es válido.",cause);
	}
}
