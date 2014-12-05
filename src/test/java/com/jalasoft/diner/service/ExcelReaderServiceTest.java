package com.jalasoft.diner.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExcelReaderServiceTest {

	private ApplicationContext applicationContext;
	private ExcelReaderService excelReaderService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReaderServiceTest.class);
	
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
		String filename = "RECETAS_Y_PRODUCCIONES_AGOSTO_2014.xlsx";
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(filename));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheet("RECETAS");

			excelReaderService.collectAllInsumos(sheet);

			fileInputStream.close();

			LOGGER.info("Successfully read the excel file.");

		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
	}
	
	@Test
	public void testReadRecetasFromSheet() {
		String filename = "RECETAS_Y_PRODUCCIONES_AGOSTO_2014.xlsx";
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(filename));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheet("RECETAS");

			excelReaderService.readRecetasFromSheet(sheet);

			fileInputStream.close();

			LOGGER.info("Successfully read the excel file.");

		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
	}
	
	@Test
	@Ignore
	public void testSaveInsumos() {
		Map<String, String> insumos = new HashMap<String, String>();
		insumos.put("Leche", "Lt");
		insumos.put("Harina", "Kg");
		
		boolean result = excelReaderService.saveInsumos(insumos);
		
		assertTrue(result);
	}
}
