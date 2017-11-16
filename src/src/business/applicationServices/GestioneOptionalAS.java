package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.OptionalParameters;
import utils.FieldChecker;
import business.businessObjects.Optional;
import business.exceptions.BusinessException;
import business.exceptions.OptionalException;

public class GestioneOptionalAS implements ApplicationService, CRUD<Optional>
{
	private static GestioneOptionalAS instance;

	private GestioneOptionalAS() {}

	public static GestioneOptionalAS getInstance() {
		if(instance == null) instance = new GestioneOptionalAS();

		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));

		Optional c = new Optional((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Optional").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}	
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));

		Optional c = new Optional((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Optional").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Optional read(CarLoanTO parameters) throws BusinessException {
		Optional result;
		try {
			DAO<Optional> d = DAOFactory.buildDAO("Optional");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Optional> list(CarLoanTO parameters) throws BusinessException {
		List<Optional> list;
		try {
			DAO<Optional> d = DAOFactory.buildDAO("Optional");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}	
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Optional").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {

		String name = (String)entityParameters.get(OptionalParameters.NAME);
		if(!FieldChecker.isOptionalNameValid(name))
			throw new OptionalException("Errore nell'inserimento del nome");
		
		String description = (String)entityParameters.get(OptionalParameters.DESCRIPTION);
		if(!FieldChecker.isValidDescription(description))
			throw new OptionalException("Errore nell'inserimento della descrizione");
		
		
		String price = (String)entityParameters.get(OptionalParameters.PRICE);
		if(!FieldChecker.isValidPrice(price))
			throw new OptionalException("Errore nell'inserimento del prezzo");
		
		return true;
	}

}
