package com.jalasoft.diner.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
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
	private static final String RECETAS = "RECETAS";
	private static final String INSUMOS = "INSUMOS";
	
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
	public void testReadInsumosFromSheet() {
		String filename = "RECETAS_Y_PRODUCCIONES_AGOSTO_2014.xlsx";
		try {
			readDataFromSheet(INSUMOS, filename);
		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
	}
	
	@Test
	public void testReadRecetasFromSheet() {
		String filename = "RECETAS_Y_PRODUCCIONES_AGOSTO_2014.xlsx";
		try {
			readDataFromSheet(RECETAS, filename);
		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
	}
	
	private void readDataFromSheet(String entityName, String filename) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File(filename));

		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet sheet = workbook.getSheet("RECETAS");

		if (entityName.equals(INSUMOS)) {
			excelReaderService.collectAllInsumos(sheet);
		} else {
			excelReaderService.readRecetasFromSheet(sheet);
		}

		fileInputStream.close();

		LOGGER.info("Successfully read the excel file.");
	}
	
	
}
