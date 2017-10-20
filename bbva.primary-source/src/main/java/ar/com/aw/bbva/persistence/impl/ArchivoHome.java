package ar.com.aw.bbva.persistence.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ar.com.aw.bbva.dao.PaginatedDAOObject;
import ar.com.aw.bbva.domain.Archivo;
import ar.com.aw.bbva.persistence.ArchivoDao;

@Repository
public class ArchivoHome extends PaginatedDAOObject<Archivo> implements ArchivoDao{
	
	private static final Log log = LogFactory.getLog(ArchivoHome.class);

    @Override
    public Archivo insert(Archivo Archivo) {
        log.debug("persisting Archivo instance");
        try {
            Archivo result = super.save(Archivo);
            log.debug("persist successful");
            return result;
        } catch (Exception re) {
            log.error("persist failed", re);
            throw new RuntimeException(re);
        }
    }

    @Override
    public Archivo update(Archivo Archivo) {
        log.debug("persisting Archivo instance");
        try {
            Archivo result = super.update(Archivo);
            log.debug("persist successful");
            return result;
        } catch (Exception re) {
            log.error("persist failed", re);
            throw new RuntimeException(re);
        }
    }

    @Override
    public void delete(Archivo Archivo) {
        log.debug("deleting Archivo instance");
        try {
            super.delete(Archivo);
            log.debug("delete successful");
        } catch (Exception re) {
            log.error("delete failed", re);
            throw new RuntimeException(re);
        }
    }

    @Override
    public Archivo get(java.io.Serializable ArchivoId, String... falseLazy) {
        log.debug("getting Archivo instance with id: " + ArchivoId);
        try {
            Criteria cri = getSession().createCriteria(Archivo.class);
            cri.add(Restrictions.idEq(ArchivoId));

            for (int i = 0; falseLazy != null && i < falseLazy.length; cri.setFetchMode(falseLazy[i++], FetchMode.JOIN));

            Archivo instance = (Archivo) cri.uniqueResult();

            log.debug("get successful," + (instance == null ? " no" : "") + " instance found");

            return instance;
        } catch (Exception re) {
            log.error("get failed", re);
            throw new RuntimeException(re);
        }
    }

    @Override
    public List<Archivo> getAll(Archivo example, String... falseLazy) {
        log.debug("finding Archivo instance by example");
        try {
            Criteria cri = getSession().createCriteria(Archivo.class);
            cri.add(Example.create(example).enableLike().ignoreCase());

            addDependenciesFilters(example, cri);
            for (int i = 0; falseLazy != null && i < falseLazy.length; cri.setFetchMode(falseLazy[i++], FetchMode.JOIN));
            @SuppressWarnings("unchecked")
            List<Archivo> results = cri.list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (Exception re) {
            log.error("find by example failed", re);
            throw new RuntimeException(re);
        }
    }

}
