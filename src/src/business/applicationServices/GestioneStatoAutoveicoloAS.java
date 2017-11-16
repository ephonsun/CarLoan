package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.CarStatusParameters;
import utils.FieldChecker;
import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;
import business.exceptions.BusinessException;
import business.exceptions.CarException;
import business.exceptions.CarStatusException;

public class GestioneStatoAutoveicoloAS implements ApplicationService, CRUD<StatoAutoveicolo>
{
	private static GestioneStatoAutoveicoloAS instance;

	private GestioneStatoAutoveicoloAS() {}

	public static GestioneStatoAutoveicoloAS getInstance() {
		if(instance == null) instance = new GestioneStatoAutoveicoloAS();

		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		StatoAutoveicolo c = new StatoAutoveicolo((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("StatoAutoveicolo").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		StatoAutoveicolo c = new StatoAutoveicolo((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("StatoAutoveicolo").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public StatoAutoveicolo read(CarLoanTO parameters)
			throws BusinessException {
		StatoAutoveicolo result;
		try {
			DAO<StatoAutoveicolo> d = DAOFactory.buildDAO("StatoAutoveicolo");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<StatoAutoveicolo> list(CarLoanTO parameters)
			throws BusinessException {
		List<StatoAutoveicolo> list;
		try {
			DAO<StatoAutoveicolo> d = DAOFactory.buildDAO("StatoAutoveicolo");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}		
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("StatoAutoveicolo").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		
		String stato = (String)entityParameters.get(CarStatusParameters.STATUS);
		if(stato == null || stato.isEmpty())
			throw new CarStatusException("Stato non specificato");
		
		String details = (String)entityParameters.get(CarStatusParameters.DETAILS);
		if(details == null || !FieldChecker.isValidDescription(details))
			throw new CarStatusException("Errore nell'inserimento dei dettagli");
		
		Sede sede = (Sede)entityParameters.get(CarStatusParameters.WORKPLACE);
		if(sede == null)
			throw new CarStatusException("Sede non definita");
		
		String nKilometers = (String)entityParameters.get(CarStatusParameters.N_KM);
		if(nKilometers == null || !FieldChecker.isInteger(nKilometers))
			throw new CarException("Errore nell'inserimento del numero di chilometri");
		else
		{
			int inKM = Integer.parseInt(nKilometers);
			if(inKM < 0)
				throw new CarException("Errore: numero chilometri negativo!");
		}
		
		return true;
	}

}
