package ar.com.aw.bbva.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import ar.com.aw.bbva.dto.DatosDTO;
import ar.com.aw.bbva.rest.security.ModulosPermisosPermitidos;
import ar.com.aw.bbva.service.FondoDefinition;

/**
 * @created 15-May-2017 18:42:50 PM
 * @author awolf
 * @version 1.0
 */
@Path("fondo")
public class FondoResource {

    @Autowired
    private FondoDefinition biz;

    @Path("/total")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ModulosPermisosPermitidos(modulosPermisos = { // TODO Definir Permisos para consultar el webservice 
    // @ModuloPermisosPermitidos(modNombre="NOMBRE_MODULO", modPermiso=ModuloPermiso.VER)
    })
    public Response get() throws Exception {
    	DatosDTO dto  = biz.getDatos();
        ResponseBuilder response = Response.status(Response.Status.OK)
                .entity(dto)
                .type(MediaType.APPLICATION_JSON);
        return response.build();
    }

    @Path("/mercado")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ModulosPermisosPermitidos(modulosPermisos = { // TODO Definir Permisos para consultar el webservice 
    // @ModuloPermisosPermitidos(modNombre="NOMBRE_MODULO", modPermiso=ModuloPermiso.VER)
    })
    public Response getMercado() throws Exception {
    	DatosDTO dto  = biz.getVariacionMercado();
        ResponseBuilder response = Response.status(Response.Status.OK)
                .entity(dto)
                .type(MediaType.APPLICATION_JSON);
        return response.build();
    }

    
    @Path("/disponible")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ModulosPermisosPermitidos(modulosPermisos = { // TODO Definir Permisos para consultar el webservice 
    // @ModuloPermisosPermitidos(modNombre="NOMBRE_MODULO", modPermiso=ModuloPermiso.VER)
    })
    public Response getDisponible() throws Exception {
    	DatosDTO dto  = biz.getDisponible();
        ResponseBuilder response = Response.status(Response.Status.OK)
                .entity(dto)
                .type(MediaType.APPLICATION_JSON);
        return response.build();
    }

    
    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ModulosPermisosPermitidos(modulosPermisos = { // TODO Definir Permisos para consultar el webservice 
    // @ModuloPermisosPermitidos(modNombre="NOMBRE_MODULO",modPermiso=ModuloPermiso.VER)
    })
    public Response get(@PathParam("id") Long id) throws Exception {
        ResponseBuilder response = Response.status(Response.Status.OK)
                .entity(biz.get(id))
                .type(MediaType.APPLICATION_JSON);
        return response.build();
    }
    
}
