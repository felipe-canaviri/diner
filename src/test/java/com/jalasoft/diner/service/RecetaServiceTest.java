package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		
		Integer recetaId = recetaService.save("Pancake", "Leche", 0.5);
		
		assertNotNull(recetaId);
	}
}
