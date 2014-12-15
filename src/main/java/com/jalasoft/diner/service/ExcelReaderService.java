package com.jalasoft.diner.service;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.jalasoft.diner.models.Insumo;

public interface ExcelReaderService {

	void readRecetasFromSheet(XSSFSheet sheet);
	
	void readFileWithName(String filename);
	
	Integer saveInsumo(Insumo insumo);
	
	void collectAllInsumos(XSSFSheet sheet);
	
	boolean saveInsumos(Map<String, Insumo> insumos);
}
