package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.WorkplaceParameters;

public class Sede 
{
	private String ID;
	private String citta;
	private String indirizzo;
	private String numeroTelefono;
	
	public Sede() {}
	
	public Sede(String citta, String indirizzo, String numeroTelefono)
	{
		this.citta = citta;
		this.indirizzo = indirizzo;
		this.numeroTelefono = numeroTelefono;
	}
	
	public Sede(CarLoanTO data) {
		this.ID = String.format("%02d", (Integer)data.get(WorkplaceParameters.WORKPLACE_ID));
		this.citta = (String)data.get(WorkplaceParameters.CITY);
		this.indirizzo = (String)data.get(WorkplaceParameters.ADDRESS);
		this.numeroTelefono = (String)data.get(WorkplaceParameters.TELEPHONE_NUMBER);
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public String getCitta() {
		return citta;
	}
	
	public void setCitta(String citta) {
		this.citta = citta;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	
	@Override
	public String toString()
	{
		return citta + " - " + indirizzo;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		Sede s = (Sede)obj;
		
		return getCitta().equals(s.getCitta()) &&
			   getIndirizzo().equals(s.getIndirizzo()) &&
			   getNumeroTelefono().equals(s.getNumeroTelefono());
	}
}
