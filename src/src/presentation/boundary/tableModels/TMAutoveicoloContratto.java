package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Autoveicolo;
import transferObject.CarLoanTO;

public class TMAutoveicoloContratto implements TableModel
{
	private String id;
	private String marca;
	private String modello;
	private String immatricolazione;
	
	public TMAutoveicoloContratto(String id, String marca, String modello, String immatricolazione) {
		this.id = id;
		this.marca = marca;
		this.modello = modello;
		this.immatricolazione = immatricolazione;
	}

	public String getImmatricolazione() {
		return immatricolazione;
	}

	public TMAutoveicoloContratto() {}

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
		fields.put("marca", "Marca");
		fields.put("modello", "Modello");
		fields.put("immatricolazione", "Anno Imm.");
		
		return fields;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Autoveicolo auto = (Autoveicolo)entity;
		
		return new TMAutoveicoloContratto(auto == null ? "REMOVED" : auto.getID(), 
										  auto == null ? "REMOVED" : auto.getMarca(), 
										  auto == null ? "REMOVED" : auto.getModello(), 
										  auto == null ? "REMOVED" : auto.getAnnoImmatricolazione());
	}	

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		// TODO Auto-generated method stub
		return false;
	}

}
