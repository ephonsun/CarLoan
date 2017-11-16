package business.businessObjects;

import java.time.LocalDate;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.UserParameters;
import transferObject.parameters.entityParameters.WorkerParameters;

public class Impiegato extends Utente 
{
	public Impiegato() {}

	public Impiegato(String username, String password, String nome,
			String cognome, String ID, LocalDate dataNascita, String codiceFiscale, Sede sede) {
		super(username, password, UserPermission.WORKER, nome,
				cognome, ID, dataNascita, codiceFiscale, sede);
	}
	
	public Impiegato(CarLoanTO data) {
		super((String)data.get(UserParameters.USERNAME),
			  (String)data.get(UserParameters.PASSWORD),
			  UserPermission.WORKER,
			  (String)data.get(WorkerParameters.NAME),
			  (String)data.get(WorkerParameters.SURNAME),
			  (String)data.get(WorkerParameters.WORKER_ID),
			  LocalDate.parse((String)data.get(WorkerParameters.BIRTHDATE)),
			  (String)data.get(WorkerParameters.TAX_CODE),
			  (Sede)data.get(WorkerParameters.WORKPLACE));				
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}
