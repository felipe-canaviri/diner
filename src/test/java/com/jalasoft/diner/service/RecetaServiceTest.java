package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Receta;

public class RecetaServiceTest {

	private RecetaService recetaService;
	private ApplicationContext applicationContext;
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		recetaService = (RecetaService)applicationContext.getBean("recetaService");
	}
	
	@After
	public void tearDown() {
		if (applicationContext != null) {
			((ConfigurableApplicationContext)applicationContext).close();
		}
	}
	
	@Test
	public void testSave() {
		Map<String, Double> ingredientes = new HashMap<String, Double>();
		ingredientes.put("Leche", 0.5);
		Integer recetaId = recetaService.save("Pancake", ingredientes);
		
		assertNotNull(recetaId);
	}
	
	@Test
	public void testFindByName() {
		Set<Receta> recetas  = recetaService.findByName("Llaju");
		
		assertNotNull(recetas);
		assertTrue(recetas.size() >= 1);
	}
}
