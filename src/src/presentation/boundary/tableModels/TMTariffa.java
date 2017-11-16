package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Tariffa;
import transferObject.CarLoanTO;

public class TMTariffa implements TableModel
{
	String id;
	String nome;
	String tipo;
	
	public TMTariffa() {}
	
	public TMTariffa(String id, String nome, String prezzoGiornaliero, String costoKM, String tipo)
	{
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
	}
	
	public String getId() { return id; }
	

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return tipo;
	}
	
	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("nome", "Nome");
		fields.put("tipo", "Tipo Tariffa");
		
		return fields;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Tariffa t = (Tariffa)entity;
		return new TMTariffa(t.getID(),
						 	 t.getNome(),
						 	 "" + t.getPrezzoGiornaliero(),
						 	 "" + t.getPrezzoChilometro(),
						 	 t.getTipo());
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		// TODO Auto-generated method stub
		return false;
	}

}
