package ar.com.aw.bbva.persistence;

import ar.com.aw.bbva.domain.Fondo;

public interface FondoDao {

	
    public abstract Fondo insert(Fondo Fondo);
    public abstract Fondo update(Fondo Fondo);
    public abstract void delete(Fondo Fondo);
    public abstract Fondo get(java.io.Serializable FondoId, String... falseLazy);
    public abstract java.util.List<Fondo> getAll(Fondo Fondo, String... falseLazy);

}
