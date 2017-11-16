package integration.daos;

import integration.exceptions.IntegrationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DAOFactory 
{
	private DAOFactory() {}
	
	@SuppressWarnings("unchecked")
	public static <T> DAO<T> buildDAO(String entityType) throws IntegrationException
	{
		String className = "integration.daos." + entityType + "DAO";
		DAO<T> daoInstance = null;
		try 
		{
			Method daoCreation = Class.forName(className).getMethod("getInstance", (Class<?>[])null);
			daoInstance = (DAO<T>)daoCreation.invoke(null, (Object[])null);
		} catch (IllegalAccessException   | 
				 ClassNotFoundException   | 
				 NoSuchMethodException 	  | 
				 SecurityException 		  | 
				 IllegalArgumentException | 
				 InvocationTargetException e) 
		{
			throw new IntegrationException("DAO non trovata: " + className);
		}
		
		return daoInstance;
	}
}
