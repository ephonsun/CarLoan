package presentation.boundary.tableModels;

import java.util.LinkedHashMap;
import java.util.Map;

import business.businessObjects.Optional;
import transferObject.CarLoanTO;

public class TMOptional implements TableModel
{
	private String id;
	private String prezzo;
	private String nome;
		
	public TMOptional() {}
	
	public TMOptional(String id, String nome, String prezzo)
	{
		this.id = id;
		this.nome = nome;
		this.prezzo = prezzo;
	}
	
	public String getId()
	{
		return id;
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
	public TableModel instantiate(Object entity) {
		Optional opt = (Optional)entity;
		
		return new TMOptional(opt == null ? "REMOVED" : opt.getID(),
							  opt == null ? "REMOVED" : opt.getNome(),
							  opt == null ? "REMOVED" : String.format("%.2f", opt.getPrezzo()) + " €");
	}

	@Override
	public boolean shouldBeFiltered(CarLoanTO parameters) {
		// TODO Auto-generated method stub
		return false;
	}

}
