package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Sede;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.WorkplaceParameters;

public class TMSede implements TableModel
{
	private String id;
	private String citta;
	private String recapito;
	private String indirizzo;
	
	public TMSede() {}
	
	public TMSede(String id, String citta, String indirizzo, String numeroTelefono) {
		this.id = id;
		this.citta = citta;
		this.recapito = numeroTelefono;
		this.indirizzo = indirizzo;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public String getCitta() {
		return citta;
	}
	
	public String getRecapito() {
		return recapito;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("citta", "Città");
		fields.put("recapito", "Recapito");
		fields.put("indirizzo", "Indirizzo");
		
		return fields;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Sede sede = (Sede)entity;
		return new TMSede(sede.getID(), sede.getCitta(), sede.getIndirizzo(), sede.getNumeroTelefono());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		String filterCity = (String)parameters.get(WorkplaceParameters.CITY);
		System.out.println(filterCity);
		return getCitta().contains(filterCity);
	}

}
