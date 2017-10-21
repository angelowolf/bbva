package ar.com.aw.bbva.business;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.aw.bbva.domain.Fondo;
import ar.com.aw.bbva.dto.DataSetDTO;
import ar.com.aw.bbva.dto.DatosDTO;
import ar.com.aw.bbva.persistence.FondoDao;
import ar.com.aw.bbva.service.FondoDefinition;

@Service
@Transactional("transactionManagerBbva")
public class FondoBiz implements FondoDefinition{
	
	@Autowired
	private FondoDao dao;

    @Override
    public Fondo insert(Fondo Fondo) {
        return dao.insert(Fondo);
    }

    @Override
    public Fondo update(Fondo Fondo) {
        return dao.update(Fondo);
    }

    @Override
    public void delete(Fondo Fondo) {
        dao.delete(Fondo);
    }

    @Override
    public Fondo get(java.io.Serializable FondoId, String... falseLazy) {
        return dao.get(FondoId, falseLazy);
    }

    @Override
    public List<Fondo> getAll(Fondo Fondo, String... falseLazy) {
        return dao.getAll(Fondo, falseLazy);
    }
    
    public DatosDTO getDatos(){
    	DatosDTO datos = new DatosDTO ();
    	List<Fondo> fondos = this.getAll(new Fondo(),(String[]) null);
    	Map<Date, Date> labels = new TreeMap<>();
    	Map<String, List<Fondo>> fondosByFondo = new TreeMap<>();
    	
    	for (Fondo f : fondos) {
			labels.put(f.getFechaDatos(), f.getFechaDatos());
			if(fondosByFondo.containsKey(f.getEspecie())) {
				List<Fondo> susFondos = fondosByFondo.get(f.getEspecie());
				susFondos.add(f);
			} else {
				List<Fondo> susFondos = new ArrayList<>();
				susFondos.add(f);
				fondosByFondo.put(f.getEspecie(), susFondos);
			}
		}
    	
    	
    	List<DataSetDTO> datasets = new ArrayList<>();
    	List<String> labelsFechas = new ArrayList<>();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	for (Date fecha : labels.values()) {
    		labelsFechas.add(sdf.format(fecha));
		}
    	
    	int cantidadMaximaDatos = 0;
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    List<Fondo> value = entry.getValue();
    	    if (cantidadMaximaDatos < value.size()) {
    	    	cantidadMaximaDatos = value.size();
    	    } 
    	}    
    	
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    String key = entry.getKey();
    	    List<Fondo> value = entry.getValue();
    	    Collections.sort(value);
    	    DataSetDTO ds = new DataSetDTO();
    	    ds.setLabel(key);
    	    Random rand = new Random();
    	    int newColor = rand.nextInt(0x1000000);
            String asd = String.format("#%06X", newColor);
    	    ds.setBackgroundColor(asd);
    	    List<Double> valores = new ArrayList<>();
    	    if(value.size() < cantidadMaximaDatos) {
    	    	for (int i = 0; i < cantidadMaximaDatos - value.size(); i++) {
					valores.add(null);
				}
    	    }
    	    for (Fondo fondo : value) {
    	    	valores.add(fondo.getCantidadDisponible() * fondo.getPrecioMercado());
			}
    	    ds.setData(valores);
    	    datasets.add(ds);
    	}    	
    	datos.setDatasets(datasets);
    	datos.setLabels(labelsFechas);
    	return datos;
    }
    
    public DatosDTO getVariacionMercado(){
    	DatosDTO datos = new DatosDTO ();
    	List<Fondo> fondos = this.getAll(new Fondo(),(String[]) null);
    	Map<Date, Date> labels = new TreeMap<>();
    	Map<String, List<Fondo>> fondosByFondo = new TreeMap<>();
    	
    	for (Fondo f : fondos) {
			labels.put(f.getFechaDatos(), f.getFechaDatos());
			if(fondosByFondo.containsKey(f.getEspecie())) {
				List<Fondo> susFondos = fondosByFondo.get(f.getEspecie());
				susFondos.add(f);
			} else {
				List<Fondo> susFondos = new ArrayList<>();
				susFondos.add(f);
				fondosByFondo.put(f.getEspecie(), susFondos);
			}
		}
    	
    	List<DataSetDTO> datasets = new ArrayList<>();
    	List<String> labelsFechas = new ArrayList<>();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	for (Date fecha : labels.values()) {
    		labelsFechas.add(sdf.format(fecha));
		}
    	
    	int cantidadMaximaDatos = 0;
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    List<Fondo> value = entry.getValue();
    	    if (cantidadMaximaDatos < value.size()) {
    	    	cantidadMaximaDatos = value.size();
    	    } 
    	}   
    	
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    String key = entry.getKey();
    	    List<Fondo> value = entry.getValue();
    	    Collections.sort(value);
    	    DataSetDTO ds = new DataSetDTO();
    	    ds.setLabel(key);
    	    Random rand = new Random();
    	    int newColor = rand.nextInt(0x1000000);
            String asd = String.format("#%06X", newColor);
    	    ds.setBackgroundColor(asd);
    	    List<Double> valores = new ArrayList<>();
    	    if(value.size() < cantidadMaximaDatos) {
    	    	for (int i = 0; i < cantidadMaximaDatos - value.size(); i++) {
					valores.add(null);
				}
    	    }
    	    for (Fondo fondo : value) {
    	    	valores.add(fondo.getPrecioMercado());
			}
    	    ds.setData(valores);
    	    datasets.add(ds);
    	}    	
    	datos.setDatasets(datasets);
    	datos.setLabels(labelsFechas);
    	return datos;
    }
    
    public DatosDTO getDisponible(){
    	DatosDTO datos = new DatosDTO ();
    	List<Fondo> fondos = this.getAll(new Fondo(),(String[]) null);
    	Map<Date, Date> labels = new TreeMap<>();
    	Map<String, List<Fondo>> fondosByFondo = new TreeMap<>();
    	
    	for (Fondo f : fondos) {
			labels.put(f.getFechaDatos(), f.getFechaDatos());
			if(fondosByFondo.containsKey(f.getEspecie())) {
				List<Fondo> susFondos = fondosByFondo.get(f.getEspecie());
				susFondos.add(f);
			} else {
				List<Fondo> susFondos = new ArrayList<>();
				susFondos.add(f);
				fondosByFondo.put(f.getEspecie(), susFondos);
			}
		}
    	
    	List<DataSetDTO> datasets = new ArrayList<>();
    	List<String> labelsFechas = new ArrayList<>();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	for (Date fecha : labels.values()) {
    		labelsFechas.add(sdf.format(fecha));
		}
    	
    	int cantidadMaximaDatos = 0;
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    List<Fondo> value = entry.getValue();
    	    if (cantidadMaximaDatos < value.size()) {
    	    	cantidadMaximaDatos = value.size();
    	    } 
    	}   
    	
    	for (Map.Entry<String, List<Fondo>> entry : fondosByFondo.entrySet()) {
    	    String key = entry.getKey();
    	    List<Fondo> value = entry.getValue();
    	    Collections.sort(value);
    	    DataSetDTO ds = new DataSetDTO();
    	    ds.setLabel(key);
    	    Random rand = new Random();
    	    int newColor = rand.nextInt(0x1000000);
            String asd = String.format("#%06X", newColor);
    	    ds.setBackgroundColor(asd);
    	    List<Double> valores = new ArrayList<>();
    	    if(value.size() < cantidadMaximaDatos) {
    	    	for (int i = 0; i < cantidadMaximaDatos - value.size(); i++) {
					valores.add(null);
				}
    	    }
    	    for (Fondo fondo : value) {
    	    	valores.add(fondo.getCantidadDisponible());
			}
    	    ds.setData(valores);
    	    datasets.add(ds);
    	}    	
    	datos.setDatasets(datasets);
    	datos.setLabels(labelsFechas);
    	return datos;
    }
}
