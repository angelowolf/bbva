package ar.com.aw.bbva.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Provider
public class BbvaExceptionMapper extends PortalExceptionMapper {

	private static final Log	log	= LogFactory.getLog(BbvaExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable throwable) {
		log.error("bbvaExceptionMapping:",throwable);
		return super.toResponse(throwable);
	}

}
