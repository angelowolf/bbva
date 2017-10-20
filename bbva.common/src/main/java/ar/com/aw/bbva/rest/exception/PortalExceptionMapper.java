package ar.com.aw.bbva.rest.exception;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.aw.bbva.business.exception.AccessTokenExeption;
import ar.com.aw.bbva.business.exception.AccionNoAutorizadaException;
import ar.com.aw.bbva.business.exception.AuthenticationCreadentialException;
import ar.com.aw.bbva.business.exception.BusinessException;

@Provider
public class PortalExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Log	log	= LogFactory.getLog(PortalExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable throwable) {
		Map<String,Object> responseBodyError = new HashMap<>();
		responseBodyError.put("error", throwable.getMessage());
		String errorHeader = throwable.getMessage();
		
		ResponseBuilder responseBuilder = Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(responseBodyError);
		
		if(throwable instanceof AuthenticationCreadentialException) {
			responseBodyError.put("code", Response.Status.BAD_REQUEST);
		} else if(throwable instanceof ExpiredJwtException) {
			responseBodyError.put("code", Response.Status.UNAUTHORIZED);
			errorHeader = "JWT_EXPIRADO";
		} else if(throwable instanceof AccessTokenExeption) {
			responseBodyError.put("code", Response.Status.UNAUTHORIZED);
			errorHeader = "JWT_INVALIDO";
		} else if(throwable instanceof AccionNoAutorizadaException) {
			responseBodyError.put("code", Response.Status.FORBIDDEN);
			errorHeader = "NO_PERMITIDO";
		}else if(throwable != null && throwable.getMessage() != null && (throwable.getMessage().contains("ORABPEL-30044") || throwable.getMessage().contains("ORA-30514"))) {
			responseBodyError.put("code", Response.Status.UNAUTHORIZED);
			errorHeader = "BPM_TOKEN_EXPIRADO";
		} else if(throwable instanceof BusinessException) {
			responseBodyError.put("code", Response.Status.INTERNAL_SERVER_ERROR);
			errorHeader = "BUSINESS_EXCEPTION";
		} else if(throwable instanceof WebApplicationException) {
			responseBodyError.put("code", ((WebApplicationException)throwable).getResponse().getStatusInfo());
		} else {
			responseBodyError.put("code", Response.Status.INTERNAL_SERVER_ERROR);
		}
//		if(throwable instanceof RuntimeException)
//			throwable.printStackTrace();
		log.debug("PortalExceptionMapping:",throwable);
		return responseBuilder.status((Response.Status)responseBodyError.get("code")).header("X-ERROR", errorHeader).build();
	}

}