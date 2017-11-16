package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.OptionalParameters;

public class Optional 
{
	private String ID;
	private String nome;
	private String descrizione;
	private float prezzo;
	
	public Optional() {}
	
	public Optional(String nome, String descrizione, float prezzo) {
		super();
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
	}
	
	public Optional(CarLoanTO data) {
		this.ID = "" + (Integer)data.get(OptionalParameters.OPTIONAL_ID);
		this.nome = (String)data.get(OptionalParameters.NAME);
		this.prezzo = Float.parseFloat((String)data.get(OptionalParameters.PRICE));
		this.descrizione = (String)data.get(OptionalParameters.DESCRIPTION);
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public float getPrezzo() {
		return prezzo;
	}
	
	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	
	@Override
	public boolean equals(Object opt)
	{
		Optional o = (Optional)opt;
		return 	getNome().equals(o.getNome()) &&
				getDescrizione().equals(o.getDescrizione()) &&
				getPrezzo() == o.getPrezzo();
	}
}
