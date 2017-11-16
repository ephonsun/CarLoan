package presentation;

import business.businessObjects.Utente;

public class SessionManager 
{
	public static Utente user;
	
	public static void Reset()
	{
		user = null;
	}
}
