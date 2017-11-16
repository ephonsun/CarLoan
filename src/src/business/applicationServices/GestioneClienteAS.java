package business.applicationServices;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.CustomerParameters;
import utils.FieldChecker;
import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;
import business.businessObjects.Cliente;
import business.exceptions.BusinessException;
import business.exceptions.CustomerException;

public class GestioneClienteAS implements ApplicationService, CRUD<Cliente> 
{
	private static GestioneClienteAS instance;
	
	private GestioneClienteAS() {}
	
	public static GestioneClienteAS getInstance() {
		if(instance == null) instance = new GestioneClienteAS();
		
		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException 
	{
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));		
		Cliente c = new Cliente((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Cliente").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());

		}		
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException 
	{
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));	
		Cliente c = new Cliente((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Cliente").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Cliente read(CarLoanTO parameters) throws BusinessException 
	{
		DAO<Cliente> d;
		Cliente result;
		try {
			d = DAOFactory.buildDAO("Cliente");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Cliente> list(CarLoanTO params)
			throws BusinessException {
		List<Cliente> list;
		try {
			DAO<Cliente> d = DAOFactory.buildDAO("Cliente");
			list = d.readAll(params);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}		
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException 
	{
		try {
			DAOFactory.buildDAO("Cliente").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {

		String name = (String)entityParameters.get(CustomerParameters.NAME);
		if(!FieldChecker.isValidName(name))
			throw new CustomerException("Errore nell'inserimento del nome");
		
		String cognome = (String)entityParameters.get(CustomerParameters.SURNAME);
		if(!FieldChecker.isValidName(cognome))
			throw new CustomerException("Errore nell'inserimento del cognome");
		
		String dataNascita = (String)entityParameters.get(CustomerParameters.BIRTHDATE);
		if(!FieldChecker.isValidField(dataNascita))
		{
			throw new CustomerException("Data Nascita non inserita");
		}
		else
		{
			LocalDate data = LocalDate.parse(dataNascita);
			if((int)ChronoUnit.YEARS.between(data, LocalDate.now()) < 21)
				throw new CustomerException("Cliente troppo giovane per noleggiare un veicolo");
		}
		
		String nTelefono = (String)entityParameters.get(CustomerParameters.TELEPHONE_NUMBER);
		if(!FieldChecker.isValidTelephoneNumber(nTelefono))
			throw new CustomerException("Errore nell'inserimento del recapito telefonico");
		
		String ID = (String)entityParameters.get(CustomerParameters.CUSTOMER_ID);
		
		String codiceFiscale = (String)entityParameters.get(CustomerParameters.TAX_CODE);
		if(!FieldChecker.isValidTaxCode(codiceFiscale))
			throw new CustomerException("Errore nell'inserimento del codice fiscale");
		else if(ID.equals("0"))
		{
			try {
				List<Cliente> clienti = list(null);
				for(Cliente c : clienti)
				{
					if(c.getCodiceFiscale().equals(codiceFiscale) && !c.getID().equals(ID))
						throw new CustomerException("Errore: codice fiscale duplicato!");
				}
			} catch (BusinessException e) {
				throw new CustomerException(e.getMessage());
			}
		}
		
		String citta = (String)entityParameters.get(CustomerParameters.CITY);
		if(!FieldChecker.isValidName(citta))
			throw new CustomerException("Nome città non valido");
		
		String indirizzo = (String)entityParameters.get(CustomerParameters.ADDRESS);
		if(!FieldChecker.isValidAddress(indirizzo))
			throw new CustomerException("Indirizzo non valido");
		
		String tipoPatente = (String)entityParameters.get(CustomerParameters.LICENCE_TYPE);
		if(!FieldChecker.isValidField(tipoPatente))
			throw new CustomerException("Tipo patente non inserito");
		
		String numeroPatente = (String)entityParameters.get(CustomerParameters.LICENCE_NUMBER);
		if(!FieldChecker.isValidLicenceNumber(numeroPatente))
			throw new CustomerException("Numero di patente non valido");
		
		String scadenzaPatente = (String)entityParameters.get(CustomerParameters.LICENCE_EXPIRATION_DATE);
		if(!FieldChecker.isValidField(scadenzaPatente))
			throw new CustomerException("Scadenza patente non inserita");
		else
		{
			LocalDate scadenza = LocalDate.parse(scadenzaPatente);
			if(scadenza.isBefore(LocalDate.now()))
				throw new CustomerException("Patente scaduta!");
		}
		
		return true;
	}
}
