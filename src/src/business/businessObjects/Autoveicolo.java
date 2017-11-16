package business.businessObjects;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CarParameters;

public class Autoveicolo 
{
	public static final String[] TipologiaCambio = {"Automatico", "Manuale", "Tiptronic"};
	public static final String[] NumeroPortePossibili = {"3", "5"};
	public static final String[] NumeroPostiPossibili = {"2", "4", "5", "6", "7"};
	public static final String[] AnnoImmatricolazionePossibili = {"2000", "2001", "2002", "2003", "2004", "2005", "2006",
												   				  "2007", "2008", "2009", "2010", "2011", "2012", "2013",
												   				  "2014", "2015", "2016"};
	public static final String[] TipiPatentePossibili = {"AM", "A1" , "A2", "A"  , "B1", 
		  												 "B" , "C1" , "C" , "D1" , "D" ,
		  												 "BE", "C1E", "CE", "D1E", "DE"}; 
	private String ID;
	private String marca;
	private String modello;
	private int cilindrata;
	private String numeroPorte;
	private String numeroPosti;
	private String targa;
	private String annoImmatricolazione;
	private String tipoCambio;
	private String accessoriSerie;
	private StatoAutoveicolo statoVeicolo;
	private Categoria categoria;
	
	public Autoveicolo(String marca, String modello, int cilindrata,
			String numeroPorte, String numeroPosti, String targa, String annoImmatricolazione,
			String tipoCambio, String accessoriSerie,
			StatoAutoveicolo statoVeicolo, Categoria categoria) {
		super();
		this.marca = marca;
		this.modello = modello;
		this.cilindrata = cilindrata;
		this.numeroPorte = numeroPorte;
		this.targa = targa;
		this.annoImmatricolazione = annoImmatricolazione;
		this.tipoCambio = tipoCambio;
		this.accessoriSerie = accessoriSerie;
		this.statoVeicolo = statoVeicolo;
		this.categoria = categoria;
		this.numeroPosti = numeroPosti;
	}
	
	public Autoveicolo(CarLoanTO data) {
		this.ID = String.format("%03d", (Integer)data.get(CarParameters.CAR_ID));
		this.marca = (String)data.get(CarParameters.BRAND);
		this.modello = (String)data.get(CarParameters.MODEL);
		this.cilindrata = Integer.parseInt((String)data.get(CarParameters.ENGINE_CAPACITY));
		this.numeroPorte = (String)data.get(CarParameters.DOOR_NUMBER);
		this.targa = (String)data.get(CarParameters.LICENCE_PLATE);
		this.annoImmatricolazione = (String)data.get(CarParameters.MATRICULATION);
		this.tipoCambio = (String)data.get(CarParameters.GEAR_TYPE);
		this.accessoriSerie = (String)data.get(CarParameters.BASIC_OPTIONALS);
		this.statoVeicolo = (StatoAutoveicolo)data.get(CarParameters.CAR_STATUS);
		this.categoria = (Categoria)data.get(CarParameters.CATEGORY);
		this.numeroPosti = (String)data.get(CarParameters.SEATS_NUMBER);
	}

	public Autoveicolo() {
	}

	public String getNumeroPosti() {
		return numeroPosti;
	}

	public void setNumeroPosti(String numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	public String getModello() {
		return modello;
	}
	
	public void setModello(String modello) {
		this.modello = modello;
	}
	
	public int getCilindrata() {
		return cilindrata;
	}
	
	public void setCilindrata(int cilindrata) {
		this.cilindrata = cilindrata;
	}
	
	public String getNumeroPorte() {
		return numeroPorte;
	}
	
	public void setNumeroPorte(String numeroPorte) {
		this.numeroPorte = numeroPorte;
	}
	
	public String getTarga() {
		return targa;
	}
	
	public void setTarga(String targa) {
		this.targa = targa;
	}
	
	public String getAnnoImmatricolazione() {
		return annoImmatricolazione;
	}
	
	public void setAnnoImmatricolazione(String annoImmatricolazione) {
		this.annoImmatricolazione = annoImmatricolazione;
	}
	
	public String getTipoCambio() {
		return tipoCambio;
	}
	
	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	public String getAccessoriSerie() {
		return accessoriSerie;
	}
	
	public StatoAutoveicolo getStatoVeicolo() {
		return statoVeicolo;
	}
	public void setStatoVeicolo(StatoAutoveicolo statoVeicolo) {
		this.statoVeicolo = statoVeicolo;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setAccessoriSerie(String accessoriSerie) {
		this.accessoriSerie = accessoriSerie;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Autoveicolo a = (Autoveicolo)obj;
		
		return 	getMarca().equals(a.getMarca()) &&
				getModello().equals(a.getModello()) &&
				getTarga().equals(a.getTarga()) &&
				getCilindrata() == a.getCilindrata() &&
				getAnnoImmatricolazione().equals(a.getAnnoImmatricolazione()) &&
				getAccessoriSerie().equals(a.getAccessoriSerie()) &&
				getTipoCambio().equals(a.getTipoCambio()) &&
				getCategoria().equals(a.getCategoria()) &&
				getStatoVeicolo().equals(a.getStatoVeicolo()) &&
				getNumeroPorte().equals(a.getNumeroPorte()) &&
				getNumeroPosti().equals(a.getNumeroPosti());
	}
}
