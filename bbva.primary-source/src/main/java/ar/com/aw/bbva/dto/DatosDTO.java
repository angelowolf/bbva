package ar.com.aw.bbva.dto;

import java.util.ArrayList;
import java.util.List;

public class DatosDTO {

	List<String> labels = new ArrayList<>();
	List<DataSetDTO> datasets = new ArrayList<>();

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<DataSetDTO> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSetDTO> datasets) {
		this.datasets = datasets;
	}

}
