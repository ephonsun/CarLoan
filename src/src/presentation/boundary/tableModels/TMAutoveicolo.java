package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CarParameters;

public class TMAutoveicolo implements TableModel
{
	private String id;
	private String marca;
	private String modello;
	private String categoria;
	private String targa;
	private String immatricolazione;
	private String stato;
	
	public TMAutoveicolo(String id, String marca, String modello, 
						 String targa, String stato, String categoria, String immatricolazione) {
		this.id = id;
		this.marca = marca;
		this.modello = modello;
		this.targa = targa;
		this.stato = stato;
		this.categoria = categoria;
		this.immatricolazione = immatricolazione;
	}

	public String getImmatricolazione() {
		return immatricolazione;
	}

	public TMAutoveicolo() {
		// TODO Auto-generated constructor stub
	}



	public String getMarca() {
		return marca;
	}

	public String getModello() {
		return modello;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getTarga() {
		return targa;
	}

	public String getStato() {
		return stato;
	}

	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("marca", "Marca");
		fields.put("modello", "Modello");
		fields.put("categoria", "Categoria");
		fields.put("targa", "Targa");
		fields.put("immatricolazione", "Anno Imm.");
		fields.put("stato", "Stato Veicolo");
		
		return fields;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Autoveicolo auto = (Autoveicolo)entity;
		
		return new TMAutoveicolo(auto.getID(), 
								 auto.getMarca(), 
								 auto.getModello(), 
								 auto.getTarga(), 
								 auto.getStatoVeicolo() == null ? "" : auto.getStatoVeicolo().getStato(),
								 auto.getCategoria() == null ? "" : auto.getCategoria().getNome(),
								 auto.getAnnoImmatricolazione());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		Categoria filterCategory = (Categoria)parameters.get(CarParameters.CATEGORY);
		String filterBrand = (String)parameters.get(CarParameters.BRAND);
		String filterModel = (String)parameters.get(CarParameters.MODEL);
		
		boolean hasCategory = filterCategory == null || filterCategory.getNome().equals(categoria);
		return hasCategory && marca.contains(filterBrand) && modello.contains(filterModel);
	}

}
