package ar.com.aw.bbva.business;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.aw.bbva.domain.Archivo;
import ar.com.aw.bbva.domain.Fondo;
import ar.com.aw.bbva.persistence.ArchivoDao;
import ar.com.aw.bbva.persistence.FondoDao;
import ar.com.aw.bbva.service.ArchivoDefinition;

@Service
@Transactional("transactionManagerBbva")
public class ArchivoBiz implements ArchivoDefinition {

	private static final Log LOG = LogFactory.getLog(ArchivoBiz.class);

	private static final String PATH = "C:/bbva";

	@Autowired
	private ArchivoDao dao;
	@Autowired
	private FondoDao daoF;

	@SuppressWarnings("deprecation")
	public void cargarArchivos() {
		File folder = new File(PATH);
		File[] listOfFiles = folder.listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		List<Archivo> archivos = new ArrayList<>();

		for (File listOfFile : listOfFiles) {
			if (listOfFile.isFile()) {
				System.out.println("Cargando archivo" + listOfFile.getName());
				Archivo a = new Archivo();
				a.setNombreArchivo(listOfFile.getName());
				List<Archivo> archivosBD = dao.getAll(a, (String[]) null);
				if (archivosBD.isEmpty()) {
					try {
						FileInputStream excelFile = new FileInputStream(new File(PATH + "/" + listOfFile.getName()));
						Workbook workbook = new HSSFWorkbook(excelFile);
						Sheet datatypeSheet = workbook.getSheetAt(0);
						String[] nombreArchivo = listOfFile.getName().split("_");
						String fechaDatos = nombreArchivo[3];
						Date dateFechaDatos = sdf.parse(fechaDatos);
						for (int i = 3; i < datatypeSheet.getLastRowNum() - 2; i++) {
							Row row = datatypeSheet.getRow(i);
							Cell especie = row.getCell(0);
							Cell cntDisponible = row.getCell(2);
							Cell precioMercado = row.getCell(5);
							Cell fecha = row.getCell(8);

							Fondo f = new Fondo();
							f.setFechaDatos(dateFechaDatos);
							f.setEspecie(this.handleCell(especie, especie.getCellTypeEnum()));
							f.setCantidadDisponible(Double
									.parseDouble(this.handleCell(cntDisponible, cntDisponible.getCellTypeEnum())));
							f.setPrecioMercado(Double.parseDouble(
									this.handleCell(precioMercado, precioMercado.getCellTypeEnum()).replace(",", ".")));
							Date d = sdf.parse(this.handleCell(fecha, fecha.getCellTypeEnum()));
							f.setFecha(d);
							f.setArchivo(a);
							a.agregarFondo(f);
						}
						workbook.close();
						archivos.add(a);
					} catch (Exception e) {
						LOG.error("Error al procesar archivo", e);
					}
				}
			}
		}
		for (Archivo archivo : archivos) {
			dao.insert(archivo);
			for (Fondo ff : archivo.getFondos()) {
				daoF.insert(ff);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private String handleCell(Cell cell, CellType cellType) {
		if (null != cellType) {
			switch (cellType) {
			case NUMERIC:
				return (long) cell.getNumericCellValue() + "";
			case STRING:
				return cell.getRichStringCellValue() + "";
			case BOOLEAN:
				return cell.getBooleanCellValue() + "";
			case BLANK:
				return "";
			case FORMULA:
				return handleCell(cell, cell.getCachedFormulaResultTypeEnum());
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public Archivo insert(Archivo Archivo) {
		return dao.insert(Archivo);
	}

	@Override
	public Archivo update(Archivo Archivo) {
		return dao.update(Archivo);
	}

	@Override
	public void delete(Archivo Archivo) {
		dao.delete(Archivo);
	}

	@Override
	public Archivo get(java.io.Serializable ArchivoId, String... falseLazy) {
		return dao.get(ArchivoId, falseLazy);
	}

	@Override
	public List<Archivo> getAll(Archivo Archivo, String... falseLazy) {
		return dao.getAll(Archivo, falseLazy);
	}
}
