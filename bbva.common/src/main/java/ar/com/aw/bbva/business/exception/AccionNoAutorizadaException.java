package ar.com.aw.bbva.business.exception;

public class AccionNoAutorizadaException extends BusinessException {

	private static final long serialVersionUID = 1444830481966705029L;

	public AccionNoAutorizadaException() {
		super("No tiene los permisos necesarios para ejecutar la acción que desea realizar.");
	}

}
