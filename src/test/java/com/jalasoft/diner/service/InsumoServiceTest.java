package com.jalasoft.diner.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Insumo;

public class InsumoServiceTest {

	private ApplicationContext applicationContext;
	private InsumoService service;
	private static final Logger LOGGER = LoggerFactory.getLogger(InsumoServiceTest.class);

	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		service = (InsumoService)applicationContext.getBean("insumoService");
	} 
	
	@After
	public void tearDown() {
		if (applicationContext != null) {
			((ConfigurableApplicationContext)applicationContext).close();
		}
	}
	
	@Test
	public void testSave() {
		Insumo insumo = new Insumo();
		insumo.setNombre("Leche");
		insumo.setUnidadDeMedida("Lt");
		
		Integer id = null;
				
		try {
			id = service.save(insumo);
			assertNotNull(id);
			assertTrue(id > 0);
		} catch (Exception e) {
			LOGGER.error("Error when storing an Insumo: " + insumo.toString(), e);
		}
	}
	
	@Test
	public void findByName() {
		Insumo insumo = service.findByName("tomate");
		
		assertNotNull(insumo);
		assertEquals("tomate", insumo.getNombre());
	}
	
	@Test 
	public void findByNameLength() {
		List<Insumo> insumos = service.findByNameLength();
		
		assertNotNull(insumos);
		assertTrue(insumos.size() > 0);
	}
}
