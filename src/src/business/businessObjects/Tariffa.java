package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.RentalRateParameters;

public class Tariffa 
{
	public static final String[] tipiTariffa = {"Chilometrica", "Illimitata"};
	private String ID;
	private String nome;
	private float prezzoGiornaliero;
	private float prezzoChilometro;
	private float prezzoGiornalieroBonus;
	private float prezzoChilometroBonus;
	private String descrizione;
	private String tipo;
	
	public Tariffa() {}
	
	public Tariffa(String nome, float prezzoGiornaliero, float prezzoChilometro, 
				   String tipo, String descrizione, float prezzoGiornalieroBonus, float prezzoChilometroBonus) {
		this.nome = nome;
		this.setPrezzoGiornaliero(prezzoGiornaliero);
		this.tipo = tipo;
		this.descrizione = descrizione;
		this.prezzoChilometro = prezzoChilometro;
		this.setPrezzoChilometroBonus(prezzoChilometroBonus);
		this.setPrezzoGiornalieroBonus(prezzoGiornalieroBonus);
	}
	
	public Tariffa(CarLoanTO data) {
		this.ID = String.format("%02d", (Integer)data.get(RentalRateParameters.RATE_ID));
		this.prezzoGiornaliero = Float.parseFloat((String)data.get(RentalRateParameters.BASE_PRICE));
		this.prezzoChilometro = Float.parseFloat((String)data.get(RentalRateParameters.KM_PRICE));
		this.nome = (String)data.get(RentalRateParameters.NAME);
		this.descrizione = (String)data.get(RentalRateParameters.DESCRIPTION);
		this.tipo = (String)data.get(RentalRateParameters.TYPE);
		this.prezzoChilometroBonus = Float.parseFloat((String)data.get(RentalRateParameters.KM_PRICE_BONUS));
		this.prezzoGiornalieroBonus = Float.parseFloat((String)data.get(RentalRateParameters.BASE_PRICE_BONUS));
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public float getPrezzoChilometroBonus() {
		return prezzoChilometroBonus;
	}

	public void setPrezzoChilometroBonus(float prezzoChilometroBonus) {
		this.prezzoChilometroBonus = prezzoChilometroBonus;
	}

	public float getPrezzoGiornalieroBonus() {
		return prezzoGiornalieroBonus;
	}

	public void setPrezzoGiornalieroBonus(float prezzoGiornalieroBonus) {
		this.prezzoGiornalieroBonus = prezzoGiornalieroBonus;
	}

	public float getPrezzoGiornaliero() {
		return prezzoGiornaliero;
	}

	public void setPrezzoGiornaliero(float prezzoGiornaliero) {
		this.prezzoGiornaliero = prezzoGiornaliero;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		Tariffa t = (Tariffa)obj;
		
		return 	getNome().equals(t.getNome()) &&
				getDescrizione().equals(t.getDescrizione()) &&
				getPrezzoGiornaliero() == t.getPrezzoGiornaliero() &&
				getPrezzoChilometro() == t.getPrezzoChilometro() && 
				getPrezzoGiornalieroBonus() == t.getPrezzoGiornalieroBonus() &&
				getPrezzoChilometroBonus() == t.getPrezzoChilometroBonus() &&
				getTipo().equals(t.getTipo());
	}
}
