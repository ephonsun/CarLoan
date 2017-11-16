package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.CarParameters;
import transferObject.parameters.entityParameters.CarStatusParameters;
import utils.FieldChecker;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.StatoAutoveicolo;
import business.businessObjects.Utente;
import business.exceptions.BusinessException;
import business.exceptions.CarException;

public class GestioneAutoveicoloAS implements ApplicationService, CRUD<Autoveicolo>
{
	private static GestioneAutoveicoloAS instance;

	private GestioneAutoveicoloAS() {}

	public static GestioneAutoveicoloAS getInstance() {
		if(instance == null) instance = new GestioneAutoveicoloAS();

		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Autoveicolo c = new Autoveicolo((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));

		try {
			DAOFactory.buildDAO("Autoveicolo").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Autoveicolo c = new Autoveicolo((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));

		try 
		{
			DAOFactory.buildDAO("Autoveicolo").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Autoveicolo read(CarLoanTO parameters) throws BusinessException {
		DAO<Autoveicolo> d;
		Autoveicolo result = null;
		
		try 
		{
			d = DAOFactory.buildDAO("Autoveicolo");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) 
		{
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Autoveicolo> list(CarLoanTO parameters) throws BusinessException {
		CarLoanTO listData = null;

		if(parameters != null)
		{
			Utente u = (Utente)parameters.get(ServiceParameters.ACTIVE_USER);
			listData = new CarLoanTO();
			if(u != null)
				listData.put(CarStatusParameters.WORKPLACE, u.getSede());
			else
				listData.put(CarStatusParameters.WORKPLACE, null);
		}
		List<Autoveicolo> list = null;
		DAO<Autoveicolo> d;
		try {
			d = DAOFactory.buildDAO("Autoveicolo");
			list = d.readAll(listData);

		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Autoveicolo").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		String brand = (String)entityParameters.get(CarParameters.BRAND);
		if(!FieldChecker.isValidName(brand))
			throw new CarException("Errore nell'inserimento della marca");
		
		String model = (String)entityParameters.get(CarParameters.MODEL);
		if(!FieldChecker.isValidName(model))
			throw new CarException("Errore nell'inserimento del modello");
		
		String capacity = (String)entityParameters.get(CarParameters.ENGINE_CAPACITY);
		if(!FieldChecker.isInteger(capacity))
			throw new CarException("Errore nell'inserimento della cilindrata");
		
		String nPosti = (String)entityParameters.get(CarParameters.SEATS_NUMBER);
		if(!FieldChecker.isValidField(nPosti))
			throw new CarException("Numero posti non inserito");
		
		String nPorte = (String)entityParameters.get(CarParameters.DOOR_NUMBER);
		if(!FieldChecker.isValidField(nPorte))
			throw new CarException("Numero porte non inserito");
		
		String annoImmatricolazione = (String)entityParameters.get(CarParameters.MATRICULATION);
		if(!FieldChecker.isValidField(annoImmatricolazione))
			throw new CarException("Anno Immatricolazione non inserito");
		
		String tipoCambio = (String)entityParameters.get(CarParameters.GEAR_TYPE);
		if(!FieldChecker.isValidField(tipoCambio))
			throw new CarException("Tipo cambio non inserito");
		
		Categoria category = (Categoria)entityParameters.get(CarParameters.CATEGORY);
		if(category == null)
			throw new CarException("Categoria non inserita");
		else
		{
			if(category.getCilindrata() < Integer.parseInt(capacity))
				throw new CarException("Cilindrata non conforme alla categoria scelta");
			
			if(!category.getTipoCambio().equals(tipoCambio))
				throw new CarException("Tipologia di cambio non conforme alla categoria scelta");
		}
		
		String basicOptionals = (String)entityParameters.get(CarParameters.BASIC_OPTIONALS);
		if(!FieldChecker.isValidDescription(basicOptionals))
			throw new CarException("Errore nell'inserimento degli optional di serie");
		
		String targa = (String)entityParameters.get(CarParameters.LICENCE_PLATE);
		if(!FieldChecker.isValidLicencePlate(targa))
			throw new CarException("Errore nell'inserimento della targa");
		else if((Integer)entityParameters.get(CarParameters.CAR_ID) == 0)
		{
			try {
				List<Autoveicolo> auto = list(null);
				for(Autoveicolo c : auto)
				{
					if(c.getTarga().equals(targa))
						throw new CarException("Errore: targa duplicata!");
				}
			} catch (BusinessException e) {
				throw new CarException(e.getMessage());
			}
		}

		StatoAutoveicolo stato = (StatoAutoveicolo)entityParameters.get(CarParameters.CAR_STATUS);
		if(stato == null)
			throw new CarException("Stato autoveicolo non definito");
		
		return true;
	}
}
