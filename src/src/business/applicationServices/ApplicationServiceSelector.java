package business.applicationServices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import business.exceptions.BusinessException;

public class ApplicationServiceSelector 
{
	public static ApplicationService Get(String serviceEntity) throws BusinessException
	{
		String serviceEntityName = "business.applicationServices." + serviceEntity + "AS";
		ApplicationService serviceClass = null;
		try {
			Method serviceCreation = Class.forName(serviceEntityName).getMethod("getInstance", (Class<?>[])null);
			serviceClass = (ApplicationService)serviceCreation.invoke(null, (Object[])null);
		} catch (SecurityException      	| 
				 ClassNotFoundException 	| 
				 IllegalAccessException 	| 
				 IllegalArgumentException 	| 
				 InvocationTargetException 	| 
				 NoSuchMethodException e) 
		{
			throw new BusinessException("Errore ApplicationService non trovato: " + serviceEntityName);
		}
		return serviceClass;
		
	}
	
	private ApplicationServiceSelector() {}
}
