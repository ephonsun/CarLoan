package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.CategoryParameters;
import utils.FieldChecker;
import business.businessObjects.Categoria;
import business.exceptions.BusinessException;
import business.exceptions.CategoryException;

public class GestioneCategoriaAS implements ApplicationService, CRUD<Categoria>
{
	private static GestioneCategoriaAS instance;
	
	private GestioneCategoriaAS() {}
	
	public static GestioneCategoriaAS getInstance() {
		if(instance == null) instance = new GestioneCategoriaAS();
		
		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));		
		Categoria c = new Categoria((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Categoria").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}				
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Categoria c = new Categoria((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Categoria").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Categoria read(CarLoanTO parameters) throws BusinessException {
		DAO<Categoria> d;
		Categoria result = null;
		try {
			d = DAOFactory.buildDAO("Categoria");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Categoria> list(CarLoanTO parameters) throws BusinessException {
		DAO<Categoria> d;
		List<Categoria> list = null;
		try {
			d = DAOFactory.buildDAO("Categoria");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Categoria").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		String name = (String)entityParameters.get(CategoryParameters.NAME);
		if(!FieldChecker.isValidName(name))
		{
			throw new CategoryException("Errore nell'inserimento del nome");
		}
		else if((Integer)entityParameters.get(CategoryParameters.CATEGORY_ID) == 0)
		{
			try {
				List<Categoria> categorie = list(null);
				for(Categoria c : categorie)
				{
					if(c.getNome().equals(name))
						throw new CategoryException("Errore: nome duplicato!");
				}
			} catch (BusinessException e) {
				throw new CategoryException(e.getMessage());
			}
		}
		
		String price = (String)entityParameters.get(CategoryParameters.PRICE);
		if(!FieldChecker.isValidPrice(price))
			throw new CategoryException("Errore nell'inserimento del prezzo");
		
		String capacity = (String)entityParameters.get(CategoryParameters.ENGINE_CAPACITY);
		if(!FieldChecker.isInteger(capacity))
			throw new CategoryException("Errore nell'inserimento della cilindrata");
		
		String tipoCambio = (String)entityParameters.get(CategoryParameters.GEAR_TYPE);
		if(!FieldChecker.isValidField(tipoCambio))
			throw new CategoryException("Tipo cambio non inserito");
		
		String description = (String)entityParameters.get(CategoryParameters.DESCRIPTION);
		if(!FieldChecker.isValidDescription(description))
			throw new CategoryException("Errore nell'inserimento della descrizione");

		return true;
	}
}
