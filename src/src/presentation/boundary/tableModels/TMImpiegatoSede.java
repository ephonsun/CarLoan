package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import transferObject.CarLoanTO;
import business.businessObjects.Impiegato;

public class TMImpiegatoSede implements TableModel
{
	private String id;
	private String nome;
	private String cognome;
	private String codiceFiscale;

	public TMImpiegatoSede(String ID, String nome, String cognome, String codiceFiscale)
	{
		this.id = ID;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
	}
	
	
	public TMImpiegatoSede() {
		// TODO Auto-generated constructor stub
	}

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
	
	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("nome", "Nome");
		fields.put("cognome", "Cognome");
		fields.put("codiceFiscale", "Codice Fiscale");

		return fields;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Impiegato worker = (Impiegato)entity;
		return new TMImpiegato(worker.getID(),
							   worker.getNome(),
							   worker.getCognome(),
							   worker.getCodiceFiscale(),
							   worker.getSede().getCitta(),
							   worker.getSede().getIndirizzo());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		return false;
	}

}
