package business.applicationServices;

import business.exceptions.BusinessException;
import transferObject.CarLoanTO;

@FunctionalInterface
public interface ApplicationService {
	public boolean check(CarLoanTO entityParameters) throws BusinessException;
}