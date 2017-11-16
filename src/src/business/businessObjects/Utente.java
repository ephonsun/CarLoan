package business.businessObjects;

import java.time.LocalDate;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.UserParameters;

public class Utente extends LoginUtente 
{
	private String nome;
	private String cognome;
	private LocalDate dataNascita;
	private String codiceFiscale;
	private Sede sede;

	public Utente(String username, String password, UserPermission permission, String nome,
			String cognome, String ID, LocalDate dataNascita, String codiceFiscale, Sede sede) {
		super(ID, username, password, permission);
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.codiceFiscale = codiceFiscale;
		this.sede = sede;
	}

	public Utente() {
		super(null, null, null, UserPermission.NONE);
	}

	public Utente(CarLoanTO data) {
		super((String)data.get(UserParameters.USER_ID),
			  (String)data.get(UserParameters.USERNAME),
			  (String)data.get(UserParameters.PASSWORD),
			  (UserPermission)data.get(UserParameters.USER_PERMISSION));		
		this.nome = (String)data.get(UserParameters.NAME);
		this.cognome = (String)data.get(UserParameters.SURNAME);
		this.codiceFiscale = (String)data.get(UserParameters.TAX_CODE);
		this.dataNascita = LocalDate.parse((String)data.get(UserParameters.BIRTHDATE));
		this.sede = (Sede)data.get(UserParameters.WORKPLACE);
	}

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

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCodiceFiscale()
	{
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale)
	{
		this.codiceFiscale = codiceFiscale;		
	}
	
	public Sede getSede() {
		return sede;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Utente u = (Utente)obj;
		
		return 	super.equals(u) &&
				getNome().equals(u.getNome()) &&
				getCognome().equals(u.getCognome()) && 
				getCodiceFiscale().equals(u.getCodiceFiscale()) &&
				getDataNascita().equals(u.getDataNascita()) &&
				getSede().equals(u.getSede());
	}
}
