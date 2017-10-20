package ar.com.aw.bbva.service;

import ar.com.aw.bbva.domain.Fondo;
import ar.com.aw.bbva.dto.DatosDTO;

public interface FondoDefinition {

	public abstract Fondo insert(Fondo Fondo);
	public abstract Fondo update(Fondo Fondo);
	public abstract void delete(Fondo Fondo);
	public abstract Fondo get(java.io.Serializable FondoId, String... falseLazy);
	public abstract java.util.List<Fondo> getAll(Fondo Fondo, String... falseLazy);
	public abstract DatosDTO getDatos();
	public abstract DatosDTO getVariacionMercado();
	public abstract DatosDTO getDisponible();
}
