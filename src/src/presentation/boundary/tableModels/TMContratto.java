package presentation.boundary.tableModels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import business.businessObjects.Contratto;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.ContractParameters;

public class TMContratto implements TableModel
{
	private String id;
	private String stato;
	private String cliente;
	private String tariffa;
	private String marca;
	private String modello;
	private String dataInizio;
	
	public TMContratto() {}
	
	public TMContratto(String id, String stato, String cliente, String tariffa,
			String marca, String modello, String dataInizio) {
		super();
		this.id = id;
		this.stato = stato;
		this.cliente = cliente;
		this.tariffa = tariffa;
		this.marca = marca;
		this.modello = modello;
		this.dataInizio = LocalDate.parse(dataInizio).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}
	
	public String getDataInizio() {
		return dataInizio;
	}

	public String getStato() {
		return stato;
	}
	
	public String getCliente() {
		return cliente;
	}

	public String getTariffa() {
		return tariffa;
	}

	public String getMarca() {
		return marca;
	}

	public String getModello() {
		return modello;
	}
	
	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("stato", "Stato");
		fields.put("cliente", "Codice Fiscale Cliente");
		fields.put("tariffa", "Tariffa");
		fields.put("marca", "Marca");
		fields.put("modello", "Modello");
		fields.put("dataInizio", "Data Inizio");

		return fields;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Contratto c = (Contratto)entity;
		return new TMContratto(c.getID(), 
							   c.getStatoContratto(), 
							   c.getClienteStipulante() == null ? "REMOVED" : c.getClienteStipulante().getCodiceFiscale(),
							   c.getTariffa() == null ? "REMOVED" : c.getTariffa().getNome(), 
							   c.getAutoveicolo() == null ? "REMOVED" : c.getAutoveicolo().getMarca(), 
							   c.getAutoveicolo() == null ? "REMOVED" : c.getAutoveicolo().getModello(), 
							   c.getDataInizio().toString());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		@SuppressWarnings("unchecked")
		List<String> filterStatus = (List<String>)parameters.get(ContractParameters.STATUS);
		String filterCustomer = (String)parameters.get(ContractParameters.CUSTOMER);
		LocalDate filterStartDate = (LocalDate)parameters.get(ContractParameters.START_DATE);
		
		
				
		boolean hasStatus = filterStatus.isEmpty() || filterStatus.contains(getStato());
		boolean hasDate = filterStartDate == null || LocalDate.parse(getDataInizio(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).equals(filterStartDate);
		return 	hasStatus && 
				getCliente().contains(filterCustomer) && 
				hasDate;
	}
}
