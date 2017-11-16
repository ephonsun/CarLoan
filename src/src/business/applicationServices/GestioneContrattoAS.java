package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.ContractParameters;
import utils.FieldChecker;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Cliente;
import business.businessObjects.Contratto;
import business.businessObjects.Optional;
import business.businessObjects.Sede;
import business.businessObjects.Tariffa;
import business.exceptions.BusinessException;
import business.exceptions.ContractException;

public class GestioneContrattoAS implements ApplicationService, CRUD<Contratto> 
{
	private static GestioneContrattoAS instance;
	
	private GestioneContrattoAS() {}
	
	public static GestioneContrattoAS getInstance() {
		if(instance == null) instance = new GestioneContrattoAS();
		
		return instance;
	}
	
	public List<Autoveicolo> mostraAutoDisponibili(CarLoanTO params) throws BusinessException
	{
		// Prelevo i dati del form dal Transfer Object
		CarLoanTO formData = (CarLoanTO)params.get(GenericEntityParameters.ENTITY);
		LocalDate dataInizio = LocalDate.parse((String)formData.get(ContractParameters.START_DATE));
		LocalDate dataFine = LocalDate.parse((String)formData.get(ContractParameters.END_DATE));
		Sede sedeInizio = (Sede)formData.get(ContractParameters.START_WORKPLACE);
		Sede sedeFine = (Sede)formData.get(ContractParameters.END_WORKPLACE);

		String ID = (String)formData.get(ContractParameters.CONTRACT_ID);
		
		// Elenco delle auto
		List<Autoveicolo> autoveicoli = GestioneAutoveicoloAS.getInstance().list(null);
		// Elenco dei contratti
		List<Contratto> contratti = list(null);

		List<Autoveicolo> disponibili = new ArrayList<Autoveicolo>();
		
		// Cicla per ogni autoveicolo
		for(Autoveicolo a : autoveicoli)
		{
			// Se l'autoveicolo è in giacenza nella sede di inizio del contratto
			if(a.getStatoVeicolo().getSedeVeicolo().getID().equals(sedeInizio.getID()))
			{
				// Do per scontato che sia disponibile
				boolean isAvialable = true;
				// Cicla per ogni contratto
				for(Contratto c : contratti)
				{
					// Se non è chiuso o annullato E non è il contratto che stiamo modificando
					if(!(("Chiuso").equals(c.getStatoContratto()) || ("Annullato").equals(c.getStatoContratto()))
						&& !c.getID().equals(ID) && c.getAutoveicolo().getID().equals(a.getID()))
					{
						LocalDate _dataI1 = c.getDataInizio();
						LocalDate _dataI2 = dataInizio;

						LocalDate _dataF1 = c.getDataFine();
						LocalDate _dataF2 = dataFine;

						// Se gli intervalli di tempo si sovrappongono, l'espressione ritorna falso.
						// Avendo dato per scontato che l'autoveicolo fosse disponibile, l'AND logico
						// rende tutto falso.
						// Espressione per controllare che due intervalli si sovrappongono:
						// 	start1 <= end2 and start2 <= end1
						// Ritorna vero se non si sovrappongono. La negazione quindi è:
						//	start1 >= end2 OR start2 >= end1
						isAvialable &= (_dataF1.isBefore(_dataI2) || _dataF2.isBefore(_dataI1));						
					}
				}
				// Se dopo l'iterata il veicolo risulta ancora disponibile, aggiungilo alla lista
				if(isAvialable)
					disponibili.add(a);
			}
			else if(a.getStatoVeicolo().getSedeVeicolo().getID().equals(sedeFine.getID()))
			{
				boolean isAvialable = true;
				// Cicla per ogni contratto
				for(Contratto c : contratti)
				{
					// Se non è chiuso o annullato E non è il contratto che stiamo modificando
					if(!(("Chiuso").equals(c.getStatoContratto()) || ("Annullato").equals(c.getStatoContratto()))
						&& !c.getID().equals(ID) && c.getAutoveicolo().getID().equals(a.getID()))
					{						
						LocalDate _dataF1 = c.getDataFine();
						LocalDate _dataI2 = dataInizio;

						isAvialable &= _dataF1.isBefore(_dataI2);
					}
				}
				
				// Se dopo l'iterata il veicolo risulta ancora disponibile, aggiungilo alla lista
				if(isAvialable)
					disponibili.add(a);
			}
		}
		
		return disponibili;
	}
	
	public CarLoanTO annullaContratto(CarLoanTO params) throws BusinessException
	{
		check((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		Contratto cont = new Contratto((CarLoanTO)params.get(GenericEntityParameters.ENTITY));

		CarLoanTO result = new CarLoanTO();
		result.put(ContractParameters.HAS_PENALTY_CLAUSE, true);
		result.put(ContractParameters.PENALTY_CLAUSE, cont.getCosto() * 0.25f);
		
		return result;
	}
	
	public CarLoanTO avviaContratto(CarLoanTO params) throws BusinessException
	{
		check((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		Contratto cont = new Contratto((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		
		LocalDate startDate = (LocalDate)params.get(ContractParameters.START_DATE);
		if(!startDate.isEqual(cont.getDataInizio()))
			throw new BusinessException("Impossibile avviare un contratto in una data diversa da quella stabilita.");
		
		cont.getAutoveicolo().getStatoVeicolo().setStato("Non disponibile");
		cont.setStatoContratto("In corso");
		
		return null;
	}
	
	public CarLoanTO chiudiContratto(CarLoanTO params) throws BusinessException
	{
		check((CarLoanTO)params.get(GenericEntityParameters.ENTITY));

		String nKm = (String)params.get(ContractParameters.ACTUAL_KILOMETERS);
		if(nKm == null || nKm.isEmpty() || !FieldChecker.isInteger(nKm))
			throw new ContractException("Numero di chilometri non valido");
		else if(Integer.parseInt(nKm) < 0)
			throw new ContractException("Errore! Numero di chilometri negativo");

		Contratto cont = new Contratto((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		int km = Integer.parseInt(nKm);

		float penaltyClause = 0;
		boolean hasPenaltyClause = false;
		
		boolean loanRateWrong = false;
		if(!("Illimitata").equals(cont.getTariffa().getTipo()))
			loanRateWrong = cont.getnChilometri() < km;
		
		if(loanRateWrong || cont.getDataFine().isBefore(LocalDate.now()))
		{
			hasPenaltyClause = true;
			int days = (int)ChronoUnit.DAYS.between(cont.getDataFine(), LocalDate.now());

			penaltyClause = (km - cont.getnChilometri()) * cont.getTariffa().getPrezzoChilometroBonus() + 
					days * cont.getTariffa().getPrezzoGiornalieroBonus();
		}
		
		CarLoanTO result = new CarLoanTO();
		result.put(ContractParameters.HAS_PENALTY_CLAUSE, hasPenaltyClause);
		result.put(ContractParameters.PENALTY_CLAUSE, penaltyClause);
		
		cont.setStatoContratto("Chiuso");
		cont.setDataChiusura(LocalDate.now());
		cont.setCosto(cont.getCosto() + penaltyClause);
		cont.getAutoveicolo().getStatoVeicolo().setSedeVeicolo(cont.getSedeConsegna());
		cont.getAutoveicolo().getStatoVeicolo().setNumeroChilometri(cont.getAutoveicolo().getStatoVeicolo().getNumeroChilometri() + km);
		cont.getAutoveicolo().getStatoVeicolo().setStato("In manutenzione");
		
		return result;
	}
	
	public float calcolaPrezzo(CarLoanTO params) throws BusinessException
	{
		check((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		Contratto c = new Contratto((CarLoanTO)params.get(GenericEntityParameters.ENTITY));
		float prezzo;
		
		int numeroGiorni = Math.max((int)ChronoUnit.DAYS.between(c.getDataInizio(), c.getDataFine()), 1);
		prezzo = (numeroGiorni * c.getTariffa().getPrezzoGiornaliero()) + 
				 (c.getnChilometri() * c.getTariffa().getPrezzoChilometro() * c.getAutoveicolo().getCategoria().getPrezzoChilometro());
		
		for(Optional opt : c.getListaOptional())
		{
			prezzo += opt.getPrezzo() * numeroGiorni;
		}
		
		return prezzo;
	}
		
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Contratto c = new Contratto((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Contratto").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Contratto c = new Contratto((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		try {
			DAOFactory.buildDAO("Contratto").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Contratto read(CarLoanTO parameters) throws BusinessException {
		Contratto result;
		try {
			DAO<Contratto> d = DAOFactory.buildDAO("Contratto");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Contratto> list(CarLoanTO parameters) throws BusinessException {
		List<Contratto> list;
		try {
			DAO<Contratto> d = DAOFactory.buildDAO("Contratto");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}		
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Contratto").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		LocalDate dateI = null;
		LocalDate dateF = null;

		String dateIString = (String)entityParameters.get(ContractParameters.START_DATE);
		if(!FieldChecker.isValidField(dateIString))
			throw new ContractException("Data inizio del noleggio non inserita.");
		else
		{
			dateI = LocalDate.parse(dateIString);
			if(dateI.isBefore(LocalDate.now()))
				throw new ContractException("Data inizio del noleggio non valida. Inserire una data successiva a quella odierna.");
		}

		String dateFString = (String)entityParameters.get(ContractParameters.END_DATE);
		if(!FieldChecker.isValidField(dateFString))
		{
			throw new ContractException("Data di fine del noleggio non inserita.");
		}
		else
		{
			dateF = LocalDate.parse(dateFString);
			if(dateF.isBefore(LocalDate.now()))
				throw new ContractException("Data di fine del noleggio non valida. Inserire una data successiva a quella odierna.");
			else if(dateF.isBefore(dateI))
				throw new ContractException("Data di fine del noleggio non valida. Inserire una data successiva a quella di inizio.");
		}

		Sede sedeI = (Sede)entityParameters.get(ContractParameters.START_WORKPLACE);
		if(sedeI == null)
			throw new ContractException("Sede di ritiro non specificata");
		
		Sede sedeF = (Sede)entityParameters.get(ContractParameters.END_WORKPLACE);
		if(sedeF == null)
			throw new ContractException("Sede di consegna non specificata");
		
		Cliente c = (Cliente)entityParameters.get(ContractParameters.CUSTOMER);
		if(c == null)
			throw new ContractException("Cliente non selezionato");
		
		Autoveicolo a = (Autoveicolo)entityParameters.get(ContractParameters.CAR);
		if(a == null)
			throw new ContractException("Autoveicolo non selezionato");
		
		Tariffa t = (Tariffa)entityParameters.get(ContractParameters.RENTAL_RATE);
		if(t == null)
			throw new ContractException("Tariffa non selezionata");
		
		String nKm = (String)entityParameters.get(ContractParameters.N_KM);
		if(!FieldChecker.isInteger(nKm))
			throw new ContractException("Numero di chilometri non valido");

		return true;
	}

}
