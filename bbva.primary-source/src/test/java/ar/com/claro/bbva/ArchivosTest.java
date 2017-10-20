/**
 * 
 */
package ar.com.claro.bbva;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.aw.bbva.domain.Archivo;
import ar.com.aw.bbva.domain.Fondo;
import ar.com.aw.bbva.service.ArchivoDefinition;
import ar.com.aw.bbva.service.FondoDefinition;


/**
 * @author ang_2
 * @created 5 oct. 2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/application-context-test-bbva.xml" })
public class ArchivosTest {

	@Autowired
	private ArchivoDefinition biz;
	@Autowired
	private FondoDefinition bizF;

	@Test
	public void selectArchivoTest() {
		List<Archivo> archivos = biz.getAll(new Archivo(), (String[])null);
		Assert.assertNotNull(archivos);
	}
	
	@Test
	public void selectFondoTest() {
		List<Fondo> archivos = bizF.getAll(new Fondo(), (String[])null);
		Assert.assertNotNull(archivos);
	}
	
	@Test
	public void CargaArchivoTest() {
		biz.cargarArchivos();
	}
}
