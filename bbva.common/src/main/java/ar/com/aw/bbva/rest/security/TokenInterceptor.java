package ar.com.aw.bbva.rest.security;

import ar.com.aw.bbva.business.exception.AccionNoAutorizadaException;
import ar.com.aw.bbva.context.ThreadContext;
import ar.com.aw.bbva.context.UserContext;
import ar.com.aw.bbva.security.jwt.TokenManager;

import java.io.IOException;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

	@Inject
	private TokenManager tokenManager;
	
	@Context
    private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getMethod().equals(HttpMethod.OPTIONS) 
		|| requestContext.getMethod().equals(HttpMethod.HEAD) 
		|| resourceInfo.getResourceMethod().getAnnotation(PermitAll.class) != null)
			return;
		
		String token = requestContext.getHeaderString(TokenManager.TOKEN_HEADER_NAME);
		if(token == null) throw new WebApplicationException(Status.UNAUTHORIZED);
		UserContext ctx = tokenManager.getUserContextByToken(token);
		if(ctx==null || ctx.hasExpired()) throw new WebApplicationException(Status.UNAUTHORIZED);
		ThreadContext.set(ctx);
		
		ModuloPermisosPermitidos moduloPermisosPermitidos = resourceInfo.getResourceMethod().getAnnotation(ModuloPermisosPermitidos.class);
		if(moduloPermisosPermitidos != null) {
			switch(moduloPermisosPermitidos.modPermiso()) {
				case INS: if(ctx.getUserModulesInsPermission().contains(moduloPermisosPermitidos.modNombre())) return;
				case UPD: if(ctx.getUserModulesUpdPermission().contains(moduloPermisosPermitidos.modNombre())) return;
				case DEL: if(ctx.getUserModulesDelPermission().contains(moduloPermisosPermitidos.modNombre())) return;
				case VER: if(ctx.getUserModulesVerPermission().contains(moduloPermisosPermitidos.modNombre())) return;
				default: break;
			}
			throw new AccionNoAutorizadaException();
		}
		ModulosPermisosPermitidos modulosPermisosPermitidos = resourceInfo.getResourceMethod().getAnnotation(ModulosPermisosPermitidos.class);
		if(modulosPermisosPermitidos != null) {
			for (ModuloPermisosPermitidos moduloPermisosPermitidosItem : modulosPermisosPermitidos.modulosPermisos()) {
				switch(moduloPermisosPermitidosItem.modPermiso()) {
					case INS: if(ctx.getUserModulesInsPermission().contains(moduloPermisosPermitidosItem.modNombre())) return;
					case UPD: if(ctx.getUserModulesUpdPermission().contains(moduloPermisosPermitidosItem.modNombre())) return;
					case DEL: if(ctx.getUserModulesDelPermission().contains(moduloPermisosPermitidosItem.modNombre())) return;
					case VER: if(ctx.getUserModulesVerPermission().contains(moduloPermisosPermitidosItem.modNombre())) return;
					default: continue;
				}
			}
			throw new AccionNoAutorizadaException();
		}
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		UserContext userContext = ThreadContext.get();
		if(userContext != null) {
			String token = userContext.getToken();
			if (userContext.almostExpires()) // renovamos el jwt para que no expire y haya que loguearse nuevamente
				ThreadContext.set(tokenManager.getUserContextByToken(
					token = tokenManager.createNewToken(
								userContext.getUserName(), 
								userContext.getBpmToken(), 
								userContext.getUserGroups(),
								userContext.getUserModulesInsPermission(),
								userContext.getUserModulesUpdPermission(),
								userContext.getUserModulesDelPermission(),
								userContext.getUserModulesVerPermission()
							)
				));
			responseContext.getHeaders().putSingle(TokenManager.TOKEN_HEADER_NAME, token);
		}
	}
}