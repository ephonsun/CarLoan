package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Categoria;
import transferObject.CarLoanTO;

public class TMCategoria implements TableModel
{
	private String id;
	private String nome;
	private String prezzo;
	
	public TMCategoria() {}
	
	public TMCategoria(String id, String nome, String prezzo) {
		super();
		this.id = id;
		this.nome = nome;
		this.prezzo = prezzo;
	}

	public String getNome() {
		return nome;
	}

	public String getPrezzo() {
		return prezzo;
	}

	@Override
	public Map<String, String> getTableFields() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		
		fields.put("id", "ID");
		fields.put("nome", "Nome");
		fields.put("prezzo", "Prezzo (€)");
		
		return fields;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public TableModel instantiate(Object entity) {
		Categoria category = (Categoria)entity;
		return new TMCategoria(category.getID(), 
							   category.getNome(), 
							   String.format("%.2f", category.getPrezzoChilometro()) + " €"); 
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		return false;
	}
}
