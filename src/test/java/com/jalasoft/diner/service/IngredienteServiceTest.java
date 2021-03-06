package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Ingrediente;

public class IngredienteServiceTest {

	private ApplicationContext applicationContext;
	private IngredienteService service;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IngredienteServiceTest.class);
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		service = (IngredienteService)applicationContext.getBean("ingredienteService");
	} 
	
	@After
	public void tearDown() {
		if (applicationContext != null) {
			((ConfigurableApplicationContext)applicationContext).close();
		}
	}
	
	@Test
	public void testFindByName() {
		
		Ingrediente ingrediente = service.findByName("Leche");
		
		assertNotNull(ingrediente);
	}
}
