package business.applicationServices;

import java.util.List;

import business.exceptions.BusinessException;
import transferObject.CarLoanTO;

public interface CRUD<Entity> 
{
	public void create(CarLoanTO parameters) throws BusinessException;
	public void update(CarLoanTO parameters) throws BusinessException;
	public Entity read(CarLoanTO parameters) throws BusinessException;
	public List<Entity> list(CarLoanTO parameters) throws BusinessException;
	public void delete(CarLoanTO parameters) throws BusinessException;
}
