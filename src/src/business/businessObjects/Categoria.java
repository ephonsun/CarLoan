package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CategoryParameters;

public class Categoria
{
	private String ID;
	private String nome;
	private String descrizione;
	private float prezzoChilometro;
	private int cilindrata;
	private String tipoCambio;
	
	public Categoria(String nome, String descrizione,
			float prezzoChilometro, int cilindrata,
			String tipoCambio) {
		super();
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzoChilometro = prezzoChilometro;
		this.cilindrata = cilindrata;
		this.tipoCambio = tipoCambio;
	}
	
	public Categoria(CarLoanTO carLoanTO) 
	{
		this.ID = String.format("%02d", (Integer)carLoanTO.get(CategoryParameters.CATEGORY_ID));
		this.nome = (String)carLoanTO.get(CategoryParameters.NAME);
		this.descrizione = (String)carLoanTO.get(CategoryParameters.DESCRIPTION);
		this.cilindrata = Integer.parseInt((String)carLoanTO.get(CategoryParameters.ENGINE_CAPACITY));
		this.tipoCambio = (String)carLoanTO.get(CategoryParameters.GEAR_TYPE);
		this.prezzoChilometro = Float.parseFloat((String)carLoanTO.get(CategoryParameters.PRICE));
	}

	public Categoria() {
		// TODO Auto-generated constructor stub
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public float getPrezzoChilometro() {
		return prezzoChilometro;
	}

	public void setPrezzoChilometro(float prezzoChilometro) {
		this.prezzoChilometro = prezzoChilometro;
	}

	public int getCilindrata() {
		return cilindrata;
	}

	public void setCilindrata(int cilindrata) {
		this.cilindrata = cilindrata;
	}

	public String getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	@Override
	public boolean equals(Object cat) {
		if(cat == null)
			return false;
		
		Categoria c = (Categoria)cat;
		
		return getNome().equals(c.getNome()) &&
			   getCilindrata() == c.getCilindrata() && 
			   getDescrizione().equals(c.getDescrizione()) &&
			   getTipoCambio().equals(c.getTipoCambio()) &&
			   getPrezzoChilometro() == c.getPrezzoChilometro();
	}
}
