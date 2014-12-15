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

		Map<String, Double> insumos = new HashMap<String, Double>();
		Map<String, Receta> recetas = new HashMap<String, Receta>();
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

				LOGGER.info(String.format("Create a new Receta with name '%s'", productoActual));
				Set<Ingrediente> ingredientes = asIngredientes(insumos);
				if (ingredientes.size() <= 0) {
					LOGGER.info(String.format("It was tried to store a Receta '%s' with no ingredientes. Ignore this request.", productoActual));
					insumos = new HashMap<String, Double>();
					productoActual = nombreProducto;
					
					continue;
				}
				
				productoActual = productoActual.toLowerCase();
				recetas.put(productoActual, new Receta(productoActual, ingredientes));

				insumos = new HashMap<String, Double>();
				productoActual = nombreProducto;
			}

			String insumoCell = row.getCell(5).getStringCellValue().trim();
			Double usedQuantityCell = row.getCell(6).getNumericCellValue();

			if (insumoCell == null || "".equals(insumoCell)) {
				LOGGER.warn(String.format("The cell '%s' contains a null value for product", row.getRowNum()));
				continue;
			}
			
			insumos.put(insumoCell.toLowerCase(), usedQuantityCell);
		}
		
		saveRecetas(recetas);
	}
	
	private boolean saveRecetas(Map<String, Receta> recetas) {
		
		LOGGER.info(String.format("Batch Store of '%d' recetas.", recetas.size()));
		for (Map.Entry<String, Receta> entry : recetas.entrySet()) {
			recetaDao.save(entry.getValue());
		}
		
		return true;
	}

	@Transactional
	public void collectAllInsumos(XSSFSheet sheet) {
		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		//Map<String, String> insumos = new HashMap<String, String>();
		Map<String, Insumo> insumos = new HashMap<String, Insumo>();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String nombreProducto = row.getCell(4).getStringCellValue();
			if (nombreProducto.equals("Producto")) {
				// no estamos interesados en almacenar el header de la columna
				// de productos
				continue;
			}

			LOGGER.info(String.format("Read Insumo Cell with value '%s'", row.getCell(5).getStringCellValue()));
			
			String insumoCell = row.getCell(5).getStringCellValue().trim();
			insumoCell = insumoCell.toLowerCase();
			String unitMeasurementCell = row.getCell(7).getStringCellValue().trim();
			Double precioUnitarioCompraCell = row.getCell(8).getNumericCellValue();

			if (insumoCell == null || "".equals(insumoCell)) {
				LOGGER.warn(String.format("The cell '%s' contains a null value for product", row.getRowNum()));
				continue;
			}
			
			insumos.put(insumoCell, new Insumo(insumoCell, unitMeasurementCell, precioUnitarioCompraCell));
		}
		
		saveInsumos(insumos);
		LOGGER.info("Closing the method..");
	}

	
	public void readFileWithName(String filename) {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(filename));

			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workbook.getSheet("RECETAS");

			collectAllInsumos(sheet);
			readRecetasFromSheet(sheet);

			fileInputStream.close();

			LOGGER.info("Successfully read the excel file.");

		} catch (Exception e) {
			LOGGER.error("Error when reading excel file " + filename, e);
		}
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

	public boolean saveInsumos(Map<String, Insumo> insumos) {
		
		for (Map.Entry<String, Insumo> insumo : insumos.entrySet()) {
			saveInsumo(insumo.getValue());
		}
		
		return true;
	}
	
	public Integer saveInsumo(Insumo insumo) {
		try {
			Integer newId = insumoDao.save(insumo);
			
			if (newId == null) {
				LOGGER.error(String.format("Error when storing '%s' - '%s'", insumo.getNombre(), insumo.getUnidadDeMedida()));
				throw new RuntimeException();
			}
			
			return newId;
			
		} catch (ConstraintViolationException e) {
			LOGGER.warn(String.format("Already exists an Insumo with name '%s'. Ignore the current one. ", insumo.getNombre()));
		} catch (Exception e) {
			LOGGER.error(String.format("Error when trying to store Insumo with name '%s' and unidadMedida '%s'", insumo.getNombre(), insumo.getUnidadDeMedida()));
			throw e;
		}

		return -1;
	}
	
	
}
