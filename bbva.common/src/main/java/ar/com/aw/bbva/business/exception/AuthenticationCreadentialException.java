package ar.com.aw.bbva.business.exception;

public class AuthenticationCreadentialException extends BusinessException {

	private static final long serialVersionUID = 2081909929773174023L;

	public AuthenticationCreadentialException() {
		super("Nombre de usuario y/o contrase�a incorrectas");
	}

	public AuthenticationCreadentialException(String message) {
		super(message);
	}
	
}
