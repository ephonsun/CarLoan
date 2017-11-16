package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.LoginUtente;
import business.businessObjects.UserPermission;

import org.junit.*;

import static org.junit.Assert.*;

public class LoginDAOTest {
	DAO<LoginUtente> testDAO;

	@Before
	public void setUp() throws Exception 
	{
		testDAO = LoginDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception 
	{
		TestUtil.restoreTable("Login");
	}
	
	@Test
	public void testCreate() throws Exception
	{
		LoginUtente[] testCase = {null,
								  new LoginUtente(),
								  new LoginUtente("1", "root.admin", "root.admin", UserPermission.MANAGER)};
		
		boolean[] expectedResult = {false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				testDAO.create(testCase[i]);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(LoginUtente) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testCreate_1() throws Exception
	{
		LoginUtente testLogin = new LoginUtente("1", "root.admin", "root.admin", UserPermission.MANAGER);
		testDAO.create(testLogin);
		
		LoginUtente testCase = new LoginUtente("2", "root.admin", "root.admin", UserPermission.MANAGER);
		
		try
		{
			testDAO.create(testCase);
		}
		catch(IntegrationException e)
		{
			e.printStackTrace();
		}
		finally
		{
			assertNull("create(LoginUtente) TC4: ", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		LoginUtente testLogin = new LoginUtente("1", "root.admin", "p.password123", UserPermission.MANAGER);
		testDAO.create(testLogin);
		
		LoginUtente[] testCase = {null,
								  new LoginUtente(),
								  new LoginUtente("3", "root.admin", "root.admin", UserPermission.MANAGER),
								  new LoginUtente("1", "root.admin", "root.admin", UserPermission.MANAGER)};
	
		boolean[] expectedResult = {false, false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			try
			{
				LoginUtente categoria = testCase[i];
				testDAO.update(categoria);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(LoginUtente) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate_1() throws Exception
	{
		LoginUtente testLogin1 = new LoginUtente("1", "root.admin", "p.password123", UserPermission.MANAGER);
		testDAO.create(testLogin1);
		
		LoginUtente testLogin2 = new LoginUtente("2", "sono.admin", "p.password123", UserPermission.WORKER);
		testDAO.create(testLogin2);
		
		LoginUtente testCase = new LoginUtente("1", "sono.admin", "p.password123", UserPermission.MANAGER);
		
		try
		{
			testDAO.update(testCase);	
		}
		catch(IntegrationException e)
		{
			e.printStackTrace();
		}
		finally
		{
			assertNotEquals("update(LoginUtente) TC5: ", testCase, testDAO.read("1"));
		}
	
	}
	
	@Test
	public void testRead() throws Exception
	{
		LoginUtente testLogin = new LoginUtente("1", "root.admin", "p.password123", UserPermission.MANAGER);
		testDAO.create(testLogin);
		
		String[] testCase = {null, 
		       				 "3", 
		       				 "ABC",
							 "1"};

		boolean[] expectedResult = {false, 
									false, 
									false,
									true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;

			try 
			{
				LoginUtente result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testLogin);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}

	}
	
	@Test
	public void testReadAll_1() throws Exception
	{
		LoginUtente testLogin = new LoginUtente("1", "root.admin", "p.password123", UserPermission.MANAGER);
		testDAO.create(testLogin);

		List<LoginUtente> logins = testDAO.readAll(null);

		assertTrue(logins.contains(testLogin));
	}
	
	@Test
	public void testReadAll_2() throws Exception
	{
		List<LoginUtente> logins = testDAO.readAll(null);
		
		assertTrue(logins.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception
	{
		LoginUtente testLogin = new LoginUtente("1", "root.admin", "p.password123", UserPermission.MANAGER);
		testDAO.create(testLogin);

		String[] testCase = {null, 
				 			 "3", 
				 			 "ABC",
				 			 "1"};
		
		boolean[] expectedResult = {false, 
									false, 
									false,
									true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				testDAO.delete(testCase[i]);
				
				iResult = testDAO.read(testCase[i]) == null; 
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LoginDAOTest.class);
	}
}