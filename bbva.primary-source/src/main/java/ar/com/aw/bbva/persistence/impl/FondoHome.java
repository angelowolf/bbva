package ar.com.aw.bbva.persistence.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.aw.bbva.dao.PaginatedDAOObject;
import ar.com.aw.bbva.domain.Fondo;
import ar.com.aw.bbva.persistence.FondoDao;

@Repository
public class FondoHome extends PaginatedDAOObject<Fondo> implements FondoDao {

	private static final Log log = LogFactory.getLog(FondoHome.class);

	@Override
	public Fondo insert(Fondo Fondo) {
		log.debug("persisting Fondo instance");
		try {
			Fondo result = super.save(Fondo);
			log.debug("persist successful");
			return result;
		} catch (Exception re) {
			log.error("persist failed", re);
			throw new RuntimeException(re);
		}
	}

	@Override
	public Fondo update(Fondo Fondo) {
		log.debug("persisting Fondo instance");
		try {
			Fondo result = super.update(Fondo);
			log.debug("persist successful");
			return result;
		} catch (Exception re) {
			log.error("persist failed", re);
			throw new RuntimeException(re);
		}
	}

	@Override
	public void delete(Fondo Fondo) {
		log.debug("deleting Fondo instance");
		try {
			super.delete(Fondo);
			log.debug("delete successful");
		} catch (Exception re) {
			log.error("delete failed", re);
			throw new RuntimeException(re);
		}
	}

	@Override
	public Fondo get(java.io.Serializable FondoId, String... falseLazy) {
		log.debug("getting Fondo instance with id: " + FondoId);
		try {
			Criteria cri = getSession().createCriteria(Fondo.class);
			cri.add(Restrictions.idEq(FondoId));

			for (int i = 0; falseLazy != null && i < falseLazy.length; cri.setFetchMode(falseLazy[i++], FetchMode.JOIN))
				;

			Fondo instance = (Fondo) cri.uniqueResult();

			log.debug("get successful," + (instance == null ? " no" : "") + " instance found");

			return instance;
		} catch (Exception re) {
			log.error("get failed", re);
			throw new RuntimeException(re);
		}
	}

	@Override
	public List<Fondo> getAll(Fondo example, String... falseLazy) {
		log.debug("finding Fondo instance by example");
		try {
			Criteria cri = getSession().createCriteria(Fondo.class);
			cri.add(Example.create(example).enableLike().ignoreCase());

			addDependenciesFilters(example, cri);
			for (int i = 0; falseLazy != null && i < falseLazy.length; cri.setFetchMode(falseLazy[i++], FetchMode.JOIN))
				;
			cri.addOrder(Order.desc("fechaDatos"));
			@SuppressWarnings("unchecked")
			List<Fondo> results = cri.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (Exception re) {
			log.error("find by example failed", re);
			throw new RuntimeException(re);
		}
	}

}
