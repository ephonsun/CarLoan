package business.businessObjects;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class LoginUtente 
{
	private String ID;
	private String username;
	private String password;
	private UserPermission userPermission;

	public LoginUtente() {}
	
	public LoginUtente(String ID, String username, String password, UserPermission permission) {
		super();
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.userPermission = permission;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserPermission getUserPermission()
	{
		return userPermission;
	}
	
	public void setUserPermission(UserPermission permission)
	{
		this.userPermission = permission;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		LoginUtente login = (LoginUtente)obj;

		String hashPassword1 = getPassword().length() <= 15 ? Hashing.sha512().hashString(getPassword(), Charsets.UTF_8).toString() : getPassword();
		String hashPassword2 = login.getPassword().length() <= 15 ? Hashing.sha512().hashString(login.getPassword(), Charsets.UTF_8).toString() : login.getPassword();

		boolean correctPassword = hashPassword1.equals(hashPassword2);
		
		return 	getUsername().equals(login.getUsername()) &&
				correctPassword &&
				getUserPermission().getNumVal() == login.getUserPermission().getNumVal() &&
				getID().equals(login.getID());
	}
}
