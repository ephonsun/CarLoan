package presentation.boundary.tableModels;


import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Impiegato;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.WorkerParameters;
import transferObject.parameters.entityParameters.WorkplaceParameters;

public class TMImpiegato implements TableModel
{
	private String id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String cittaSede;
	private String indirizzoSede;

	public TMImpiegato(String ID, String nome, String cognome, String codiceFiscale, String cittaSede, String indirizzoSede)
	{
		this.id = ID;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.cittaSede = cittaSede;
		this.indirizzoSede = indirizzoSede;
	}
	
	
	public TMImpiegato() {}

	@Override
	public String getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public String getCittaSede() {
		return cittaSede;
	}
	
	public String getIndirizzoSede() {
		return indirizzoSede;
	}

	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("nome", "Nome");
		fields.put("cognome", "Cognome");
		fields.put("codiceFiscale", "Codice Fiscale");
		fields.put("cittaSede", "Città Sede");
		fields.put("indirizzoSede", "Indirizzo Sede");

		return fields;
	}
	
	@Override
	public TableModel instantiate(Object entity) {
		Impiegato worker = (Impiegato)entity;
		return new TMImpiegato(worker.getID(),
							   worker.getNome(),
							   worker.getCognome(),
							   worker.getCodiceFiscale(),
							   worker.getSede() == null ? "" : worker.getSede().getCitta(),
							   worker.getSede() == null ? "" : worker.getSede().getIndirizzo());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		String filterTaxCode = (String)parameters.get(WorkerParameters.TAX_CODE);
		String filterWorkplaceCity = (String)parameters.get(WorkplaceParameters.CITY);
	
		return getCodiceFiscale().contains(filterTaxCode) && 
			   getCittaSede().contains(filterWorkplaceCity);
	}

}
