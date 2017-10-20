package ar.com.aw.bbva.rest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import ar.com.aw.bbva.resources.util.JSonProvider;
import ar.com.aw.bbva.rest.exception.BbvaExceptionMapper;
import ar.com.aw.bbva.rest.security.CorsInterceptor;

public abstract class AbstractBbvaApplication extends ResourceConfig {

	public AbstractBbvaApplication() {
		super();
		register(BbvaExceptionMapper.class);
//		register(TokenInterceptor.class);
		register(CorsInterceptor.class);
		register(JSonProvider.class);
		register(MultiPartFeature.class); 
		property("jersey.config.server.wadl.disableWadl", true);
	}
}
