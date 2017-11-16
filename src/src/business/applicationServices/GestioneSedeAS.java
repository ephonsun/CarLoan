package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.WorkplaceParameters;
import utils.FieldChecker;
import business.businessObjects.Sede;
import business.exceptions.BusinessException;
import business.exceptions.CustomerException;

public class GestioneSedeAS implements ApplicationService, CRUD<Sede>
{
	private static GestioneSedeAS instance;
	
	private GestioneSedeAS() {}
	
	public static GestioneSedeAS getInstance() {
		if(instance == null) instance = new GestioneSedeAS();
		
		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Sede c = new Sede((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Sede").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Sede c = new Sede((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Sede").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Sede read(CarLoanTO parameters) throws BusinessException {
		Sede result;
		try {
			DAO<Sede> d = DAOFactory.buildDAO("Sede");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Sede> list(CarLoanTO parameters) throws BusinessException {
		List<Sede> list;
		try {
			DAO<Sede> d = DAOFactory.buildDAO("Sede");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}	
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Sede").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		String city = (String)entityParameters.get(WorkplaceParameters.CITY);
		if(!FieldChecker.isValidName(city))
			throw new CustomerException("Errore nell'inserimento della città");
		
		String address = (String)entityParameters.get(WorkplaceParameters.ADDRESS);
		if(!FieldChecker.isValidAddress(address))
			throw new CustomerException("Errore nell'inserimento dell'indirizzo");
		
		String nTelefono = (String)entityParameters.get(WorkplaceParameters.TELEPHONE_NUMBER);
		if(!FieldChecker.isValidTelephoneNumber(nTelefono))
			throw new CustomerException("Errore nell'inserimento del recapito telefonico");
		
		return true;
	}

}
