package integration.daos;

import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import business.businessObjects.Impiegato;
import business.businessObjects.Sede;

import org.junit.*;

import static org.junit.Assert.*;


public class ImpiegatoDAOTest 
{
	DAO<Impiegato> testDAO;
	
	@Before
	public void setUp() throws Exception {
		testDAO = ImpiegatoDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.restoreTable("Login");
		TestUtil.restoreTable("Utente");
		TestUtil.restoreTable("Sede");
	}

	@Test
	public void testCreate() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		Impiegato[] testCase =  {null,
							     new Impiegato(),
							     new Impiegato("f.sinisi", "root.admin",
									  	 	   "Francesco", "Sinisi", "1", 
									  	 	   LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
									  	 	   "SNSFNC94A03B285K", testSede)
		};
		
		boolean[] expectedResult = {false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);

				Impiegato result = testDAO.read("1");
				
				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				System.out.println("CREATE");
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Impiegato) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testCreate_1() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		Impiegato testImpiegato = new Impiegato("f.sinisi", "root.admin",
					  	 	   "Francesco", "Sinisi", "1", 
					  	 	   LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					  	 	   "SNSFNC94A03B285K", testSede);
		testDAO.create(testImpiegato);
		
		Impiegato testCase = new Impiegato("f.sinisi", "sono.admin",
		  	 	   "Giovanni", "Pascoli", "2", 
		  	 	   LocalDate.parse("13-08-1982", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
		  	 	   "SNSFNC94A03B285K", testSede);
		
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
			assertNull("create(Impiegato) TC4: ", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		Impiegato testImpiegato = new Impiegato("f.sinisi", "root.admin",
					  	 	   "Francesco", "Sinisi", "1", 
					  	 	   LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					  	 	   "SNSFNC94A03B285K", testSede);
		testDAO.create(testImpiegato);
		
		Impiegato[] testCase = new Impiegato[4];
		testCase[0] = null;
		testCase[1] = new Impiegato();
		testCase[2] = new Impiegato("root.admin", "root.admin",
								 "Piergiacomo", "Sinisi", "3", 
								 LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								 "SNSPRG94A03B285K", testSede);
		testCase[2].setID("3");
		testCase[3] = new Impiegato("root.admin", "root.admin",
								 "Piergiacomo", "Sinisi", "1", 
								 LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								 "SNSPRG94A03B285K", testSede);
		testCase[3].setID("1");
		
		boolean[] expectedResult = {false, false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				testDAO.update(testCase[i]);
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Impiegato) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate_1() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		Impiegato testUtente1 = new Impiegato("root.admin", "root.admin",
			  	 						"Francesco", "Sinisi", "1", 
			  	 						LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
			  	 						"SNSFNC94A03B285K", testSede);
		testDAO.create(testUtente1);
		
		Impiegato testUtente2 = new Impiegato("sono.Admin", "root.admin",
					"Alberto", "Pietro", "2", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"LBTPTR94A03B285K", testSede);
		testDAO.create(testUtente2);
		
		Impiegato testCase = new Impiegato("sono.Admin", "root.admin",
					 "Piergiacomo", "Sinisi", "1", 
					 LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					 "LBTPTR94A03B285K", testSede);
		
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
			assertNotEquals("update(Impiegato) TC5: ", testCase, testDAO.read("1"));
		}
	}
	
	@Test
	public void testRead() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);

		Impiegato testUtente = new Impiegato("root.admin", "root.admin",
										"Francesco", "Sinisi", "1", 
										LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										"SNSFNC94A03B285K", testSede);
		testDAO.create(testUtente);

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
				Impiegato result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testUtente);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("read(Impiegato) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);

		Impiegato testUtente = new Impiegato("root.admin", "root.admin",
										"Francesco", "Sinisi", "1", 
										LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										"SNSFNC94A03B285K", testSede);
		testDAO.create(testUtente);
		
		List<Impiegato> utenti = testDAO.readAll(null);

		assertTrue(utenti.contains(testUtente));
	}
	
	@Test
	public void testReadAll_2() throws Exception {
		List<Impiegato> utenti = testDAO.readAll(null);
		assertTrue(utenti.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);

		Impiegato testUtente = new Impiegato("root.admin", "root.admin",
										"Francesco", "Sinisi", "1", 
										LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										"SNSFNC94A03B285K", testSede);
		testDAO.create(testUtente);
		
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
				System.out.println(testCase[i]);
				iResult = testDAO.read(testCase[i]) == null;
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("delete(Utente) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ImpiegatoDAOTest.class);
	}
}