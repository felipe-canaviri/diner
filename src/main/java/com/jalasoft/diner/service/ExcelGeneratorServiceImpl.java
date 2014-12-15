package com.jalasoft.diner.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.RecetaDao;
import com.jalasoft.diner.models.Receta;

@Service("excelGeneratorService")
public class ExcelGeneratorServiceImpl implements ExcelGeneratorService {

	 
	@Autowired
	RecetaDao recetaDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelGeneratorServiceImpl.class);
	
	@Override
	@Transactional
	public List<Receta> listAvailableRecetas() {
		return recetaDao.findAll();
	}

	@Override
	public boolean createExcelWithNameAndRecetasAndDate(String filename, List<Receta> recetas, DateTime date) {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet("RECETAS");

		DateTimeFormatter format = DateTimeFormat.forPattern("dd-MMM-yyyy");
		
		
		int rowNumber = 0;
		for (Receta receta : recetas) {
			Row row = sheet.createRow(rowNumber ++);
			int cellNumber = 0;
			
			Cell cellDate = row.createCell(cellNumber ++);
			
			cellDate.setCellValue(format.print(date));
			
			Cell cellCantidadProducida = row.createCell(cellNumber ++);
			cellCantidadProducida.setCellValue(5);
			
			Cell cellProducto = row.createCell(cellNumber ++);
			cellProducto.setCellValue(receta.getNombre());
		}
		
		sheet.autoSizeColumn((short) (0));
		
		try {
			FileOutputStream outputStream = new FileOutputStream(new File(filename));
			workbook.write(outputStream);
			outputStream.close();
			LOGGER.info(String .format("Successfully written the file '%s'", filename));
			
			return true;
		} catch (Exception e) {
			LOGGER.error("Error when creatinf XLSX file", e);
		}
		
		return false;
	}


}
