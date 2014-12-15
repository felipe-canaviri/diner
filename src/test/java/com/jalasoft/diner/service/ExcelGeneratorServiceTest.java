package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Ingrediente;
import com.jalasoft.diner.models.IngredientePK;
import com.jalasoft.diner.models.Insumo;
import com.jalasoft.diner.models.Receta;

public class ExcelGeneratorServiceTest {

	private ApplicationContext applicationContext;
	private ExcelGeneratorService excelGeneratorService;
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		excelGeneratorService = (ExcelGeneratorService)applicationContext.getBean("excelGeneratorService"); 
	}
	
	@After
	public void tearDown() {
		if (applicationContext != null) {
			((ConfigurableApplicationContext)applicationContext).close();
		}
	}
	
	@Test
	public void testListAvailableRecetas() {
		List<Receta> recetas = excelGeneratorService.listAvailableRecetas();
		
		assertNotNull(recetas);
		assertTrue(recetas.size() >= 63);
	}
	
	@Test
	public void testCreateExcelWithNameAndRecetas() {
		
		List<Receta> recetas = Arrays.asList(new Receta("AA", createIngredientes("AA")));
		
		boolean result = excelGeneratorService.createExcelWithNameAndRecetasAndDate("TestA.xlsx", recetas, new DateTime());
		
		assertTrue(result);
	}
	
	private Set<Ingrediente> createIngredientes(String prefix) {
		Set<Ingrediente> ingredientes = new HashSet<>();
		
		IngredientePK idA = new IngredientePK();
		idA.setInsumoId(new Insumo(prefix + "aaa", "Kg", 1.0));
		Ingrediente ingredienteA = new Ingrediente();
		ingredienteA.setId(idA);
		
		IngredientePK idB = new IngredientePK();
		idB.setInsumoId(new Insumo(prefix + "bbb", "Lt", 1.5));
		Ingrediente ingredienteB = new Ingrediente();
		ingredienteB.setId(idB);
		
		ingredientes.add(ingredienteA);
		ingredientes.add(ingredienteB);
		
		return ingredientes;
	}
}
