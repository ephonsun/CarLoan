package presentation.boundary.tableModels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Cliente;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CustomerParameters;


public class TMCliente implements TableModel
{
	private String id;
	private String nome;
	private String cognome;
	private String dataNascita;
	private String codiceFiscale;
	private String indirizzo;
	
	public TMCliente() {}
	
	public TMCliente(String id, String nome, String cognome, 
					 String indirizzo, String dataNascita, String codiceFiscale) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.indirizzo = indirizzo;
		this.dataNascita = LocalDate.parse(dataNascita).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		this.codiceFiscale = codiceFiscale;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getId() {
		return id;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public String getDataNascita()
	{
		return dataNascita;
	}
	
	public String getCodiceFiscale()
	{
		return codiceFiscale;
	}

	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("nome", "Nome");
		fields.put("cognome", "Cognome");
		fields.put("dataNascita", "Data Nascita");
		fields.put("codiceFiscale", "Codice Fiscale");
		fields.put("indirizzo", "Indirizzo Residenza");

		return fields;
	}
	
	@Override
	public TableModel instantiate(Object entity) {
		Cliente customer = (Cliente)entity;
		return new TMCliente(customer.getID(),
							 customer.getNome(),
							 customer.getCognome(),
							 customer.getIndirizzoResidenza(),
							 customer.getDataNascita().toString(),
							 customer.getCodiceFiscale());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) 
	{
		String filterName = (String)parameters.get(CustomerParameters.NAME);
		String filterSurname = (String)parameters.get(CustomerParameters.SURNAME);
		String filterTaxCode = (String)parameters.get(CustomerParameters.TAX_CODE);
		
		return getNome().contains(filterName) && 
			   getCognome().contains(filterSurname) && 
			   getCodiceFiscale().contains(filterTaxCode);
	}

}
