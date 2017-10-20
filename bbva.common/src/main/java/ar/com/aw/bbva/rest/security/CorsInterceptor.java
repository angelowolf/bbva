package ar.com.aw.bbva.rest.security;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import ar.com.aw.bbva.security.jwt.TokenManager;

public class CorsInterceptor implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().putSingle("Access-Control-Allow-Headers","origin, content-type, accept-language, If-Modified-Since, accept, authorization, link, X-Total-Count, X-ERROR, X-ALERT, "+TokenManager.TOKEN_HEADER_NAME);
		responseContext.getHeaders().putSingle("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
//		responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		responseContext.getHeaders().putSingle("Access-Control-Expose-Headers",TokenManager.TOKEN_HEADER_NAME+", origin, If-Modified-Since, content-type, content-disposition, accept, authorization, link, X-Total-Count, X-ERROR, X-ALERT");
	}
}