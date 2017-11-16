package business.businessObjects;

import java.time.LocalDate;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.ContractParameters;

public class Contratto 
{
	public static final String[] StatoContrattoPossibili = {"Aperto", "Chiuso", "In corso", "Annullato"};
	private String ID;
	private Cliente clienteStipulante;
	private Utente impiegatoStipulatore;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private LocalDate dataChiusura;
	private LocalDate dataStipula;
	private Autoveicolo autoveicolo;
	private List<Optional> listaOptional;
	private Sede sedeConsegna;
	private Sede sedeRitiro;
	private String statoContratto;
	private float costo;
	private int nChilometri;
	private Tariffa tariffa;

	public Contratto() {}
	
	public Contratto(Cliente clienteStipulante,
			Utente impiegatoStipulatore, LocalDate dataInizio,
			LocalDate dataFine, LocalDate dataChiusura, LocalDate dataStipula,
			Autoveicolo autoveicolo, List<Optional> listaOptional,
			Sede sedeRitiro, Sede sedeStipula, String statoContratto,
			float costo, int nChilometri, Tariffa tariffa) {
		this.clienteStipulante = clienteStipulante;
		this.impiegatoStipulatore = impiegatoStipulatore;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.dataChiusura = dataChiusura;
		this.dataStipula = dataStipula;
		this.autoveicolo = autoveicolo;
		this.listaOptional = listaOptional;
		this.sedeConsegna = sedeRitiro;
		this.sedeRitiro = sedeStipula;
		this.statoContratto = statoContratto;
		this.costo = costo;
		this.nChilometri = nChilometri;
		this.tariffa = tariffa;
	}

	@SuppressWarnings("unchecked")
	public Contratto(CarLoanTO data) {
		this.ID = (String)data.get(ContractParameters.CONTRACT_ID);
		this.clienteStipulante = (Cliente)data.get(ContractParameters.CUSTOMER);
		this.impiegatoStipulatore = (Utente)data.get(ContractParameters.WORKER);
		this.dataInizio = LocalDate.parse((String)data.get(ContractParameters.START_DATE));
		this.dataFine = LocalDate.parse((String)data.get(ContractParameters.END_DATE));
		if(data.get(ContractParameters.CLOSING_DATE) != null)
			this.dataChiusura = LocalDate.parse((String)data.get(ContractParameters.CLOSING_DATE));
		this.dataStipula = LocalDate.parse((String)data.get(ContractParameters.STIPULATION_DATE));
		this.autoveicolo = (Autoveicolo)data.get(ContractParameters.CAR);
		this.listaOptional = (List<Optional>)data.get(ContractParameters.OPTIONALS);
		this.sedeConsegna = (Sede)data.get(ContractParameters.END_WORKPLACE);
		this.sedeRitiro = (Sede)data.get(ContractParameters.START_WORKPLACE);
		this.statoContratto = (String)data.get(ContractParameters.STATUS);
		this.costo = Float.parseFloat((String)data.get(ContractParameters.PRICE));
		this.nChilometri = Integer.parseInt((String)data.get(ContractParameters.N_KM));
		this.tariffa = (Tariffa)data.get(ContractParameters.RENTAL_RATE);
	}

	public List<Optional> getListaOptional() {
		return listaOptional;
	}

	public Tariffa getTariffa() {
		return tariffa;
	}

	public void setTariffa(Tariffa tariffa) {
		this.tariffa = tariffa;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public void setListaOptional(List<Optional> listaOptional) {
		this.listaOptional = listaOptional;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}

	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}

	public String getID() 
	{
		return ID;
	}
	
	public void setID(String iD) 
	{
		ID = iD;
	}
	
	public Cliente getClienteStipulante() 
	{
		return clienteStipulante;
	}
	
	public void setClienteStipulante(Cliente clienteStipulante) 
	{
		this.clienteStipulante = clienteStipulante;
	}
	
	public Utente getImpiegatoStipulatore() 
	{
		return impiegatoStipulatore;
	}
	
	public void setImpiegatoStipulatore(Impiegato impiegatoStipulatore) 
	{
		this.impiegatoStipulatore = impiegatoStipulatore;
	}
	
	public LocalDate getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(LocalDate dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public LocalDate getDataStipula() {
		return dataStipula;
	}

	public void setDataStipula(LocalDate dataStipula) {
		this.dataStipula = dataStipula;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public int getnChilometri() {
		return nChilometri;
	}

	public void setnChilometri(int nChilometri) {
		this.nChilometri = nChilometri;
	}

	public Autoveicolo getAutoveicolo() 
	{
		return autoveicolo;
	}
	
	public void setAutoveicolo(Autoveicolo autoveicolo) 
	{
		this.autoveicolo = autoveicolo;
	}
	
	public Sede getSedeConsegna() 
	{
		return sedeConsegna;
	}
	
	public void setSedeConsegna(Sede sedeConsegna) 
	{
		this.sedeConsegna = sedeConsegna;
	}
	
	public Sede getSedeRitiro() 
	{
		return sedeRitiro;
	}
	
	public void setSedeRitiro(Sede sedeRitiro) 
	{
		this.sedeRitiro = sedeRitiro;
	}
	
	public String getStatoContratto() 
	{
		return statoContratto;
	}
	
	public void setStatoContratto(String statoContratto) 
	{
		this.statoContratto = statoContratto;
	}	
	
//	private Cliente clienteStipulante;
//	private Utente impiegatoStipulatore;
//	private LocalDate dataInizio;
//	private LocalDate dataFine;
//	private LocalDate dataChiusura;
//	private LocalDate dataStipula;
//	private Autoveicolo autoveicolo;
//	private List<Optional> listaOptional;
//	private Sede sedeConsegna;
//	private Sede sedeRitiro;
//	private String statoContratto;
//	private float costo;
//	private int nChilometri;
//	private Tariffa tariffa;
	
	@Override
	public boolean equals(Object obj)
	{
		Contratto c = (Contratto)obj;
			
		return  (getClienteStipulante() == c.getClienteStipulante() || getClienteStipulante().equals(c.getClienteStipulante())) &&
				(getImpiegatoStipulatore() == c.getImpiegatoStipulatore() || getImpiegatoStipulatore().equals(c.getImpiegatoStipulatore())) &&
				 getDataInizio().equals(c.getDataInizio()) &&
				 getDataFine().equals(c.getDataFine()) &&
				 getDataStipula().equals(c.getDataStipula()) && 
				(getDataChiusura() == c.getDataChiusura() || getDataChiusura().equals(c.getDataChiusura())) &&
				(getAutoveicolo() == c.getAutoveicolo() || getAutoveicolo().equals(c.getAutoveicolo())) &&
				(getListaOptional() == c.getListaOptional() || getListaOptional().equals(c.getListaOptional())) &&
				(getSedeConsegna() == c.getSedeConsegna() || getSedeConsegna().equals(c.getSedeConsegna())) &&
				(getSedeRitiro() == c.getSedeRitiro() || getSedeRitiro().equals(c.getSedeRitiro())) &&
				 getStatoContratto().equals(c.getStatoContratto()) && 
				 getCosto() == c.getCosto() &&
				 getnChilometri() == c.getnChilometri() && 
				(getTariffa() == c.getTariffa() || getTariffa().equals(c.getTariffa())) &&
				(getID().equals(c.getID()));
	}
}
