package com.jalasoft.diner.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.proxy.HibernateProxy;

import com.jalasoft.diner.dao.GenericDao;
import com.jalasoft.diner.dao.IngredienteDao;
import com.jalasoft.diner.dao.IngredienteDaoImpl;
import com.jalasoft.diner.models.Ingrediente;

public class ApplicationDaoFactory {

	/** map lock. */
    private static Object daoMapLock = new Object();

    /** Internal handle. */
    private static Map<Class<?>, GenericDao<?, ?>> daoMap = null; 
    
//	private RecetaDao recetaDao;
	private IngredienteDao ingredienteDao;
	
//	private RecetaDao getRecetaDao() {
//		if (recetaDao == null) {
//			recetaDao = new RecetaDaoImpl();
//		}
//		return recetaDao;
//	}
	
	private IngredienteDao getIngredienteDao() {
		if (ingredienteDao == null) {
			ingredienteDao = new IngredienteDaoImpl();
		}
		return ingredienteDao;
	}
	
	@SuppressWarnings("unchecked")
	public <T> GenericDao<T, ?> getDao(final T persistentObject) {
		T persistent = persistentObject;
		
		synchronized (daoMapLock) {
			if (daoMap == null) {
				daoMap = new ConcurrentHashMap<>();
				daoMap.put(Ingrediente.class, getIngredienteDao());
				//daoMap.put(Receta.class, getRecetaDao());
			}
		}
		
		if (persistentObject instanceof HibernateProxy) {
			persistent = (T)((HibernateProxy)persistent).getHibernateLazyInitializer().getImplementation();
		}
		
		GenericDao<T, ?> result = (GenericDao<T, ?>)daoMap.get(persistent.getClass());
		
		if (result == null) {
			throw new IllegalAccessError("Unable to create a DAO with the given object.");
		}
		
		return result;
	}
}
