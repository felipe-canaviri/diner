package com.jalasoft.diner.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExcelReaderServiceTest {

	private ApplicationContext applicationContext;
	private ExcelReaderService excelReaderService;
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		excelReaderService = (ExcelReaderService)applicationContext.getBean("excelReaderService");
	}
	
	@After
	public void tearDown() {
		if (applicationContext != null) {
			((ConfigurableApplicationContext)applicationContext).close();
		}
	}
	
	@Test
	public void testReadFileWithName() {
		try {
			excelReaderService.readFileWithName("RECETAS_Y_PRODUCCIONES_AGOSTO_2014.xlsx");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
