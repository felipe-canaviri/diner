package com.jalasoft.diner.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.InsumoDao;
import com.jalasoft.diner.dao.RecetaDao;
import com.jalasoft.diner.models.Ingrediente;
import com.jalasoft.diner.models.IngredientePK;
import com.jalasoft.diner.models.Insumo;
import com.jalasoft.diner.models.Receta;

@Service("excelReaderService")
public class ExcelReaderServiceImpl implements ExcelReaderService {

	@Autowired
	private RecetaDao recetaDao;

	@Autowired
	private InsumoDao insumoDao;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReaderServiceImpl.class);

	@Transactional
	public void readRecetasFromSheet(XSSFSheet sheet) {

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		Map<String, Double> ingredientes = new HashMap<String, Double>();
		String productoActual = "";

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String nombreProducto = row.getCell(4).getStringCellValue().trim();
			if (nombreProducto.equals("Producto")) {
				// no estamos interesados en almacenar el header de la columna
				// de productos
				continue;
			}

			if (!productoActual.equals(nombreProducto)) {

				// recetario.addReceta(nombreProducto, ingredientes);
				saveRecetaWithIngredientes(productoActual, ingredientes);

				ingredientes = new HashMap<String, Double>();
				productoActual = nombreProducto;
			}

			String insumoCell = row.getCell(5).getStringCellValue().trim();
			Double usedQuantityCell = row.getCell(6).getNumericCellValue();
			//Cell unitMeasurementCell = row.getCell(7);

			if (insumoCell == null || "".equals(insumoCell)) {
				LOGGER.warn(String.format("The cell '%s' contains a null value for product", row.getRowNum()));
				continue;
			}
			
			//saveInsumo(insumoCell.getStringCellValue(), unitMeasurementCell.getStringCellValue());

			ingredientes.put(sanitizeName(insumoCell.toLowerCase()), usedQuantityCell);
			// ingredientes.add(new Ingrediente(insumo,
			// usedQuantityCell.getNumericCellValue()));

		}
	}
	
	@Transactional
	public void collectAllInsumos(XSSFSheet sheet) {
		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		Map<String, String> insumos = new HashMap<String, String>();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String nombreProducto = row.getCell(4).getStringCellValue();
			if (nombreProducto.equals("Producto")) {
				// no estamos interesados en almacenar el header de la columna
				// de productos
				continue;
			}

			String insumoCell = row.getCell(5).getStringCellValue().trim();
			String unitMeasurementCell = row.getCell(7).getStringCellValue().trim();

			if (insumoCell == null || "".equals(insumoCell)) {
				LOGGER.warn(String.format("The cell '%s' contains a null value for product", row.getRowNum()));
				continue;
			}
			
			insumos.put(sanitizeName(insumoCell.toLowerCase()), unitMeasurementCell);
		}
		
		saveInsumos(insumos);
		LOGGER.info("Closing the method..");
	}

	private String sanitizeName(String original) {
		if (original.contains("ñ")) {
			return original.replace("ñ", "ni");
		}
		
		return original;
	}
	
	public void readFileWithName(String filename) {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(filename));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheet("RECETAS");

			collectAllInsumos(sheet);
			//readRecetasFromSheet(sheet);

			fileInputStream.close();

			LOGGER.info("Successfully read the excel file.");

		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
	}

	private Integer saveRecetaWithIngredientes(String nombreReceta, Map<String, Double> insumoCantidades) {

		if (nombreReceta == null || nombreReceta.equals("")) {
			LOGGER.warn("It was tried to store a Receta with empty name. Ignore this request.");
			return -1;
		}

		Set<Ingrediente> ingredientes = asIngredientes(insumoCantidades);
		
		if (insumoCantidades.size() <= 0) {
			LOGGER.warn(String.format("It was tried to store a Receta '%s' with no ingredientes. Ignore this request.", nombreReceta));
			return -1;
		}

		Receta receta = new Receta(nombreReceta, ingredientes);
		
		LOGGER.info(String.format("Create Receta with name '%s' and ingredientes: %s", nombreReceta, ingredientes.toString()));

		return recetaDao.save(receta);
	}

	private Set<Ingrediente> asIngredientes(Map<String, Double> insumoCantidades) {
		if (insumoCantidades == null || insumoCantidades.size() <= 0) {
			return new HashSet<Ingrediente>();
		}

		Set<Ingrediente> ingredientes = new HashSet<>();
		for (Map.Entry<String, Double> entry : insumoCantidades.entrySet()) {
			LOGGER.info(String.format("Recover insumo with name '%s'",entry.getKey()));
			Insumo insumo = insumoDao.findByName(entry.getKey());
			if (insumo == null) {
				LOGGER.warn(String.format("Invalid '%s' as insumo. Ignore this item as ingrediente.", entry.getKey()));
				continue;
			}

			IngredientePK pk = new IngredientePK();
			pk.setInsumoId(insumo);
			Ingrediente ingrediente = new Ingrediente();
			ingrediente.setId(pk);
			ingrediente.setCantidadUsada(entry.getValue());

			ingredientes.add(ingrediente);
		}

		return ingredientes;
	}

	public boolean saveInsumos(Map<String, String> insumos) {
		
		for (Map.Entry<String, String> insumo : insumos.entrySet()) {
			saveInsumo(insumo.getKey(), insumo.getValue());
		}
		
		return true;
	}
	
	public Integer saveInsumo(String insumoNombre, String unidadMedida) {
		try {
			Insumo insumo = new Insumo();
			insumo.setNombre(insumoNombre.toLowerCase());
			insumo.setUnidadDeMedida(unidadMedida);
			Integer newId = insumoDao.save(insumo);
			
			if (newId == null) {
				LOGGER.error(String.format("Error when storing '%s' - '%s'", insumoNombre, unidadMedida));
				throw new RuntimeException();
			}
			
			return newId;
			
		} catch (ConstraintViolationException e) {
			LOGGER.warn(String.format("Already exists an Insumo with name '%s'. Ignore the current one. ", insumoNombre));
		} catch (Exception e) {
			LOGGER.error(String.format("Error when trying to store Insumo with name '%s' and unidadMedida '%s'", insumoNombre, unidadMedida));
			throw e;
		}

		return -1;
	}
	
	
}
