package com.jalasoft.diner.service;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelReaderService {

	void readRecetasFromSheet(XSSFSheet sheet);
	
	void readFileWithName(String filename);
	
	Integer saveInsumo(String insumoNombre, String unidadMedida);
	
	void collectAllInsumos(XSSFSheet sheet);
	
	boolean saveInsumos(Map<String, String> insumos);
}
