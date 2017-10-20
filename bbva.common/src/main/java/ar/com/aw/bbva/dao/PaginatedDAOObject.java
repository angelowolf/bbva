package ar.com.aw.bbva.dao;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Id;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ar.com.aw.bbva.rest.dto.PaginaDTO;
import ar.com.aw.bbva.rest.dto.PaginaOrdenDTO;

public abstract class PaginatedDAOObject<ENTITY> {

	@Autowired @Qualifier("sessionFactoryBbva")
    protected SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Agrega restricciones al citeria pasado por parámetro de acuerdo a los valores de los
	 * atributos de los objetos que tiene como atributos el objeto example pasado por parámetro simempre que
	 * estos tengas los metodos getters anotados con  javax.persistence.ManyToOne
	 * @param example objeto con los valores de las restricciones.
	 * @param cri Hibernate Criteria
	 * @throws Exception
	 */
	protected void addDependenciesFilters(Object example, Criteria cri) throws Exception {
		for (Method method : example.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(javax.persistence.ManyToOne.class)) {
				Object obj = method.invoke(example);
				Object objId = null;
				if (obj != null)
					objId = getValueOfIdField(obj);

				if (objId != null) {
					String claseSimpleName = method.getName();
					claseSimpleName = claseSimpleName.replaceAll("get", "");
					String fieldNameFirstLetter = claseSimpleName.toLowerCase().substring(0, 1);
					String fieldNameRest = claseSimpleName.substring(1);
					cri.createCriteria(fieldNameFirstLetter + fieldNameRest).add(Restrictions.idEq(objId));
				}
			}
		}
	}

	/**
	 * Obtiene el valor del atributo con getter anotado como Id de un objeto pasado por parámetro
	 * @param instancia
	 * @return
	 * @throws Exception
	 */
	public Object getValueOfIdField(Object instancia) throws Exception {
		for (Method method : instancia.getClass().getDeclaredMethods())
			if (method.isAnnotationPresent(Id.class))
				return method.invoke(instancia);

		return null;
	}

	/**
	 * Retorna una PaginaDTO de acuerdo a un Criteria, número de página, tamaño de página, ordenes y 
	 * propiedades del objeto que se desea como item de la paginación. Si los parámetros de paginación son nulos, 
	 * se toma por defecto tamanioPagina = 10 y nroPagina = 0
	 * @param cri Hibernate Criteria
	 * @param nroPagina número de página que se desea retornar
	 * @param tamanioPagina tamaño de la página
	 * @param paginaOrdenDto listado de criterios de orden para la consulta
	 * @param falseLazy listado de propiedades que se desea retornar en la consulta principal realizada por hibernate 
	 * @return un objeto PaginaDTO que representa una listado acotado del resultado de listar el Criteria pasado por parámetro 
	 */
	@SuppressWarnings("unchecked")
	public PaginaDTO<ENTITY> getPaginaBy(Criteria cri, Integer nroPagina, Integer tamanioPagina,
			Collection<PaginaOrdenDTO> paginaOrdenDto, String... falseLazy) {
		try {
			tamanioPagina = tamanioPagina == null || tamanioPagina <= 0? 10 : tamanioPagina;
			nroPagina = nroPagina == null || nroPagina <= 0? 0:nroPagina-1;
			
			cri.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
			
			Integer resultCount = ((Number) cri.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			cri.setProjection(null);

			if(falseLazy != null)
				for (int i = 0;  i < falseLazy.length; cri.setFetchMode(falseLazy[i++], FetchMode.JOIN));
			
			cri.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
			
			cri.setFirstResult(nroPagina * tamanioPagina);
			cri.setMaxResults(tamanioPagina);
			cri.setFetchSize(tamanioPagina);

			Set<PaginaOrdenDTO> paginaOrdenDtoOrdenado = paginaOrdenDto != null
					? new TreeSet<PaginaOrdenDTO>(paginaOrdenDto)
					: (Set<PaginaOrdenDTO>) Collections.EMPTY_SET;

			for (PaginaOrdenDTO ordenDto : paginaOrdenDtoOrdenado)
				cri.addOrder(ordenDto.isAsc()
						? Order.asc(ordenDto.getPropiedad())
						: Order.desc(ordenDto.getPropiedad()));
			
			return new PaginaDTO<ENTITY>(cri.list(),nroPagina,tamanioPagina,resultCount);
		} catch (Exception re) {
			throw new RuntimeException(re);
		}
	}

	protected ENTITY save(ENTITY object) {
		sessionFactory.getCurrentSession().save(object);
		return object;
	}
	
	protected ENTITY update(ENTITY object) {
		sessionFactory.getCurrentSession().update(object);
		return object;
	}
	
	protected void delete(ENTITY object) {
		sessionFactory.getCurrentSession().delete(object);
	}
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}

}