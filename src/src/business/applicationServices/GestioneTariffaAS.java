package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.RentalRateParameters;
import utils.FieldChecker;
import business.businessObjects.Tariffa;
import business.exceptions.BusinessException;
import business.exceptions.RentalRateException;

public class GestioneTariffaAS implements ApplicationService, CRUD<Tariffa>
{
	private static GestioneTariffaAS instance;
	
	private GestioneTariffaAS() {}
	
	public static GestioneTariffaAS getInstance() {
		if(instance == null) instance = new GestioneTariffaAS();
		
		return instance;
	}
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Tariffa c = new Tariffa((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Tariffa").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Tariffa c = new Tariffa((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Tariffa").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Tariffa read(CarLoanTO parameters) throws BusinessException {
		Tariffa result;
		try {
			DAO<Tariffa> d = DAOFactory.buildDAO("Tariffa");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Tariffa> list(CarLoanTO parameters)
			throws BusinessException {
		List<Tariffa> list;
		try {
			DAO<Tariffa> d = DAOFactory.buildDAO("Tariffa");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Tariffa").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {

		String name = (String)entityParameters.get(RentalRateParameters.NAME);
		if(!FieldChecker.isValidName(name))
		{
			throw new RentalRateException("Errore nell'inserimento del nome");
		}
		else if((Integer)entityParameters.get(RentalRateParameters.RATE_ID) == 0)
		{
			try {
				List<Tariffa> tariffe = list(null);
				for(Tariffa c : tariffe)
				{
					if(c.getNome().equals(name))
						throw new RentalRateException("Errore: nome duplicato!");
				}
			} catch (BusinessException e) {
				throw new RentalRateException(e.getMessage());
			}
		}

		String BasePrice = (String)entityParameters.get(RentalRateParameters.BASE_PRICE);
		if(!FieldChecker.isValidPrice(BasePrice))
			throw new RentalRateException("Errore nell'inserimento del prezzo base");
		
		String KMPrice = (String)entityParameters.get(RentalRateParameters.KM_PRICE);
		if(!FieldChecker.isValidPrice(KMPrice))
			throw new RentalRateException("Errore nell'inserimento del prezzo chilometrico");
		
		String basePriceBonus = (String)entityParameters.get(RentalRateParameters.BASE_PRICE_BONUS);
		if(!FieldChecker.isValidPrice(basePriceBonus))
			throw new RentalRateException("Errore nell'inserimento del prezzo giornaliero bonus");
		
		String KMPriceBonus = (String)entityParameters.get(RentalRateParameters.KM_PRICE_BONUS);
		if(!FieldChecker.isValidPrice(KMPriceBonus))
			throw new RentalRateException("Errore nell'inserimento del prezzo chilometrico bonus");
		
		String tipoTariffa = (String)entityParameters.get(RentalRateParameters.TYPE);
		if(!FieldChecker.isValidField(tipoTariffa))
			throw new RentalRateException("Tipo tariffa non inserito");
		
		String description = (String)entityParameters.get(RentalRateParameters.DESCRIPTION);
		if(!FieldChecker.isValidDescription(description))
			throw new RentalRateException("Errore nell'inserimento della descrizione");
		
		return true;
	}

}
