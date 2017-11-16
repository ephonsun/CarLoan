package business.businessObjects;

import java.time.LocalDate;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.CustomerParameters;

public class Cliente
{
	private String ID;
	private String nome;
	private String cognome;
	private String numeroTelefono;
	private String codiceFiscale;
	private LocalDate dataNascita;
	private String cittaResidenza;
	private String indirizzoResidenza;
	private String tipoPatente;
	private String numeroPatente;
	private LocalDate scadenzaPatente;

	
	public Cliente(CarLoanTO data) {
		this.ID = (String)data.get(CustomerParameters.CUSTOMER_ID);
		this.nome = (String)data.get(CustomerParameters.NAME);
		this.cognome = (String)data.get(CustomerParameters.SURNAME);
		this.numeroTelefono = (String)data.get(CustomerParameters.TELEPHONE_NUMBER);
		this.codiceFiscale = (String)data.get(CustomerParameters.TAX_CODE);
		this.dataNascita = LocalDate.parse((String)data.get(CustomerParameters.BIRTHDATE));
		this.cittaResidenza = (String)data.get(CustomerParameters.CITY);
		this.indirizzoResidenza = (String)data.get(CustomerParameters.ADDRESS);
		this.tipoPatente = (String)data.get(CustomerParameters.LICENCE_TYPE);
		this.numeroPatente = (String)data.get(CustomerParameters.LICENCE_NUMBER);
		this.scadenzaPatente = LocalDate.parse((String)data.get(CustomerParameters.LICENCE_EXPIRATION_DATE));
	}
	
	public Cliente(String nome, String cognome,
					String numeroTelefono, String codiceFiscale, LocalDate dataNascita,
					String cittaResidenza, String indirizzoResidenza,
					String tipoPatente, String numeroPatente, LocalDate scadenzaPatente) 
	{
		this.nome = nome;
		this.cognome = cognome;
		this.numeroTelefono = numeroTelefono;
		this.codiceFiscale = codiceFiscale;
		this.dataNascita = dataNascita;
		this.cittaResidenza = cittaResidenza;
		this.indirizzoResidenza = indirizzoResidenza;
		this.tipoPatente = tipoPatente;
		this.numeroPatente = numeroPatente;
		this.scadenzaPatente = scadenzaPatente;	
	}

	public Cliente() {}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCittaResidenza() {
		return cittaResidenza;
	}

	public void setCittaResidenza(String cittaResidenza) {
		this.cittaResidenza = cittaResidenza;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	public String getTipoPatente() {
		return tipoPatente;
	}

	public void setTipoPatente(String tipoPatente) {
		this.tipoPatente = tipoPatente;
	}

	public String getNumeroPatente() {
		return numeroPatente;
	}

	public void setNumeroPatente(String numeroPatente) {
		this.numeroPatente = numeroPatente;
	}

	public LocalDate getScadenzaPatente() {
		return scadenzaPatente;
	}

	public void setScadenzaPatente(LocalDate scadenzaPatente) {
		this.scadenzaPatente = scadenzaPatente;
	}	
	
	@Override
	public boolean equals(Object obj)
	{
		Cliente c = (Cliente)obj;
		
		return 	getNome().equals(c.getNome()) && 
				getCognome().equals(c.getCognome()) &&
				getNumeroTelefono().equals(c.getNumeroTelefono()) &&
				getCodiceFiscale().equals(c.getCodiceFiscale()) && 
				getDataNascita().equals(c.getDataNascita()) &&
				getCittaResidenza().equals(c.getCittaResidenza()) &&
				getIndirizzoResidenza().equals(c.getIndirizzoResidenza()) &&
				getTipoPatente().equals(c.getTipoPatente()) &&
				getScadenzaPatente().equals(c.getScadenzaPatente());
	}
}
