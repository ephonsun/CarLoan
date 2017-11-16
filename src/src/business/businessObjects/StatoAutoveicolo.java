package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CarStatusParameters;

public class StatoAutoveicolo 
{
	public static final String[] StatoVeicolo = {"Disponibile", "Non disponibile", "In manutenzione"};
	private String ID;
	private int numeroChilometri;
	private String stato;
	private String dettagli;
	private Sede sedeVeicolo;
	
	public StatoAutoveicolo() {}
	
	public StatoAutoveicolo(int numeroChilometri, String stato, String dettagli, Sede sedeVeicolo) {
		super();
		this.numeroChilometri = numeroChilometri;
		this.stato = stato;
		this.dettagli = dettagli;
		this.sedeVeicolo = sedeVeicolo;
	}
	
	public StatoAutoveicolo(CarLoanTO data) {
		this.ID = "" + (Integer)data.get(CarStatusParameters.STATUS_ID);
		this.stato = (String)data.get(CarStatusParameters.STATUS);
		this.numeroChilometri = Integer.parseInt((String)data.get(CarStatusParameters.N_KM));
		this.sedeVeicolo = (Sede)data.get(CarStatusParameters.WORKPLACE);
		this.dettagli = (String)data.get(CarStatusParameters.DETAILS);
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	
	public Sede getSedeVeicolo() {
		return sedeVeicolo;
	}
	
	public void setSedeVeicolo(Sede sedeVeicolo) {
		this.sedeVeicolo = sedeVeicolo;
	}
	
	public int getNumeroChilometri() {
		return numeroChilometri;
	}
	
	public void setNumeroChilometri(int numeroChilometri) {
		this.numeroChilometri = numeroChilometri;
	}
	
	public String getDettagli() {
		return dettagli;
	}
	
	public void setDettagli(String dettagli) {
		this.dettagli = dettagli;
	}
	
	public String getStato() {
		return stato;
	}
	
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		StatoAutoveicolo s = (StatoAutoveicolo)obj;
		
		return  getStato().equals(s.getStato()) && 
				getDettagli().equals(s.getDettagli()) && 
				getNumeroChilometri() == s.getNumeroChilometri() &&
				getSedeVeicolo().equals(s.getSedeVeicolo());
	}
}
