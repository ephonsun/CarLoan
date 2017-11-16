package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Cliente;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CustomerParameters;

public class TMClienteContratto implements TableModel
{
	private String id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	
	public TMClienteContratto()
	{
		
	}
	
	public TMClienteContratto(String id, String nome, String cognome, String codiceFiscale) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
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
		fields.put("codiceFiscale", "Codice Fiscale");

		return fields;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Cliente customer = (Cliente)entity;
		return new TMClienteContratto(customer.getID(),
							 customer.getNome(),
							 customer.getCognome(),
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
