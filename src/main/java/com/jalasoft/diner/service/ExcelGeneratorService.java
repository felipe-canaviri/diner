package com.jalasoft.diner.service;

import java.util.List;

import org.joda.time.DateTime;

import com.jalasoft.diner.models.Receta;

public interface ExcelGeneratorService {

	List<Receta> listAvailableRecetas();

	boolean createExcelWithNameAndRecetasAndDate(String string, List<Receta> recetas, DateTime date);
}
