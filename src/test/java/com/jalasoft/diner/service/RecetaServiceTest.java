package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Ingrediente;
import com.jalasoft.diner.models.Receta;

public class RecetaServiceTest {

	private RecetaService recetaService;
	private IngredienteService ingredienteService;
	private ApplicationContext applicationContext;
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		ingredienteService = (IngredienteService)applicationContext.getBean("ingredienteService");
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
		Ingrediente ingrediente = ingredienteService.findByName("Leche");
		
		Receta receta = new Receta();
		receta.setNombre("Pancake");
		receta.addIngrediente(ingrediente);
		
		Integer recetaId = recetaService.save(receta);
		
		assertNotNull(recetaId);
	}
}
