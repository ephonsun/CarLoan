package business.applicationServices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import business.exceptions.BusinessException;
import transferObject.CarLoanTO;
import transferObject.parameters.ServiceParameters;

public class CarLoanAS 
{
	public CarLoanTO callService(CarLoanTO data) throws BusinessException
	{
		CarLoanTO serviceResult = new CarLoanTO();
		String serviceName = (String)data.get(ServiceParameters.SERVICE_NAME);
		String serviceEntity = (String)data.get(ServiceParameters.SERVICE_ENTITY);
		
		ApplicationService serviceRequested = ApplicationServiceSelector.Get(serviceEntity);
		try {
			Method serviceMethod = serviceRequested.getClass().getMethod(serviceName, data.getClass());
			serviceResult.put(ServiceParameters.SERVICE_RESULT, serviceMethod.invoke(serviceRequested, data));
		} catch (NoSuchMethodException    | 
				 SecurityException 	      | 
				 IllegalAccessException   | 
				 IllegalArgumentException | 
				 InvocationTargetException e) 
		{
			throw new BusinessException(e.getCause().getMessage());
		}
		return serviceResult;
	}
}
