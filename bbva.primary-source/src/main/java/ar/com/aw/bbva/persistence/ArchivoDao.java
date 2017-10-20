package ar.com.aw.bbva.persistence;

import ar.com.aw.bbva.domain.Archivo;

public interface ArchivoDao {

	
    public abstract Archivo insert(Archivo Archivo);
    public abstract Archivo update(Archivo Archivo);
    public abstract void delete(Archivo Archivo);
    public abstract Archivo get(java.io.Serializable ArchivoId, String... falseLazy);
    public abstract java.util.List<Archivo> getAll(Archivo Archivo, String... falseLazy);

}
