package com.jalasoft.diner.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelReaderService {

	void readRecetasFromSheet(XSSFSheet sheet);
	
	void readFileWithName(String filename);
}
