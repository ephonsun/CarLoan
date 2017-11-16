package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.UserParameters;
import utils.FieldChecker;
import business.businessObjects.Utente;
import business.exceptions.BusinessException;
import business.exceptions.UserException;

public class GestioneUtenteAS implements ApplicationService, CRUD<Utente>
{
	private static GestioneUtenteAS instance;
	
	private GestioneUtenteAS() {}
	
	public static GestioneUtenteAS getInstance() {
		if(instance == null) instance = new GestioneUtenteAS();
		
		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Utente c = new Utente((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Utente").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Utente c = new Utente((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Utente").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Utente read(CarLoanTO parameters) throws BusinessException 
	{
		Utente result;
		try {
			DAO<Utente> d = DAOFactory.buildDAO("Utente");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Utente> list(CarLoanTO parameters)
			throws BusinessException {
		List<Utente> list;
		try {
			DAO<Utente> d = DAOFactory.buildDAO("Utente");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {	
		try {
			DAOFactory.buildDAO("Utente").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		
		String ID = (String)entityParameters.get(UserParameters.USER_ID);
		
		String name = (String)entityParameters.get(UserParameters.NAME);
		if(!FieldChecker.isValidName(name))
			throw new UserException("Errore nell'inserimento del nome");
		
		String surname = (String)entityParameters.get(UserParameters.SURNAME);
		if(!FieldChecker.isValidName(surname))
			throw new UserException("Errore nell'inserimento del cognome");
		
		String dataNascita = (String)entityParameters.get(UserParameters.BIRTHDATE);
		if(!FieldChecker.isValidField(dataNascita))
		{
			throw new UserException("Data Nascita non inserita");
		}
		else
		{
			LocalDate data = LocalDate.parse(dataNascita);
			if((int)ChronoUnit.YEARS.between(data, LocalDate.now()) < 18)
				throw new UserException("Impiegato troppo giovane per essere assunto");
		}
				
		String codiceFiscale = (String)entityParameters.get(UserParameters.TAX_CODE);
		if(!FieldChecker.isValidTaxCode(codiceFiscale))
			throw new UserException("Errore nell'inserimento del codice fiscale");
		else
		{
			try {
				List<Utente> utenti = list(null);
				for(Utente c : utenti)
				{
					if(c.getCodiceFiscale().equals(codiceFiscale) && 
					   !c.getID().equals(ID))
						throw new UserException("Errore: codice fiscale duplicato!");
				}
			} catch (BusinessException e) {
				throw new UserException(e.getMessage());
			}
		}
		
		String username = (String)entityParameters.get(UserParameters.USERNAME);
		if(!FieldChecker.isValidUsername(username))
			throw new UserException("Errore nell'inserimento dell'username");
		else
		{
			try {
				List<Utente> utenti = list(null);
				for(Utente c : utenti)
				{
					if(c.getUsername().equals(username) && 
					   !c.getID().equals(ID))
						throw new UserException("Errore: username duplicato!");
				}
			} catch (BusinessException e) {
				throw new UserException(e.getMessage());
			}
		}
		
		String password = (String)entityParameters.get(UserParameters.PASSWORD);
		if(!FieldChecker.isValidPassword(password))
			throw new UserException("Errore nell'inserimento della password");
		
		return true;
	}

}
