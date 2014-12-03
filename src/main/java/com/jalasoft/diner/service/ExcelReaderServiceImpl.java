package com.jalasoft.diner.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
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

	public void readRecetasFromSheet(XSSFSheet sheet) {

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		Map<String, Double> ingredientes = new HashMap<String, Double>();
		String productoActual = "";

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String nombreProducto = row.getCell(4).getStringCellValue();
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

			Cell insumoCell = row.getCell(5);
			Cell usedQuantityCell = row.getCell(6);
			//Cell unitMeasurementCell = row.getCell(7);

			//saveInsumo(insumoCell.getStringCellValue(), unitMeasurementCell.getStringCellValue());

			ingredientes.put(insumoCell.getStringCellValue(), usedQuantityCell.getNumericCellValue());
			// ingredientes.add(new Ingrediente(insumo,
			// usedQuantityCell.getNumericCellValue()));

		}
	}
	
	@Transactional
	public void collectAllInsumos(XSSFSheet sheet) {
		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String nombreProducto = row.getCell(4).getStringCellValue();
			if (nombreProducto.equals("Producto")) {
				// no estamos interesados en almacenar el header de la columna
				// de productos
				continue;
			}

			Cell insumoCell = row.getCell(5);
			Cell unitMeasurementCell = row.getCell(7);

			saveInsumo(insumoCell.getStringCellValue(), unitMeasurementCell.getStringCellValue());
		}
	}

	@Transactional
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
			return -1;
		}

		Set<Ingrediente> ingredientes = asIngredientes(insumoCantidades);

		Receta receta = new Receta(nombreReceta, ingredientes);

		return recetaDao.save(receta);
	}

	private Set<Ingrediente> asIngredientes(Map<String, Double> insumoCantidades) {
		if (insumoCantidades == null || insumoCantidades.size() <= 0) {
			return new HashSet<Ingrediente>();
		}

		Set<Ingrediente> ingredientes = new HashSet<>();
		for (Map.Entry<String, Double> entry : insumoCantidades.entrySet()) {
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

	private Integer saveInsumo(String insumoNombre, String unidadMedida) {
		try {
			Insumo insumo = new Insumo();
			insumo.setNombre(insumoNombre);
			insumo.setUnidadDeMedida(unidadMedida);
			return insumoDao.save(insumo);
		} catch (ConstraintViolationException e) {
			LOGGER.warn(String.format("Already exists an Insumo with name '%s'. Ignore the current one. ", insumoNombre));
		} catch (Exception e) {
			LOGGER.error(String.format("Error when trying to store Insumo with name '%s' and unidadMedida '%s'", insumoNombre, unidadMedida));
			throw e;
		}

		return -1;
	}
}
