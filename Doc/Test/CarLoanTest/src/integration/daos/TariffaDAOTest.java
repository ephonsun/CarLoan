package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.Tariffa;

import org.junit.*;

import static org.junit.Assert.*;

public class TariffaDAOTest {
	DAO<Tariffa> testDAO;
	
	@Before
	public void setUp()
		throws Exception {
		testDAO = TariffaDAO.getInstance();
	}

	@After
	public void tearDown()
		throws Exception {
		TestUtil.restoreTable("Tariffa");
	}
	
	@Test
	public void testCreate() throws Exception {
		Tariffa[] testCase = {null,
				   new Tariffa(),
				   new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
						   	   "Tariffa base chilometrica", 12.0f, 4.0f)};

		boolean[] expectedResult = {false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);
				
				Tariffa result = testDAO.read("1");

				System.out.println("A");
				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Tariffa) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testCreate_1() throws Exception {
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
  										  "Semplice tariffa base giornaliera", 10.0f, 3.0f);
		testDAO.create(testTariffa);
		
		Tariffa testCase = new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
  									   "Tariffa base chilometrica", 12.0f, 4.0f);
		
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
			assertNull("create(Tariffa) TC4: ", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		Tariffa testTariffa = new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
			   	   					"Tariffa base chilometrica", 12.0f, 4.0f);
		testDAO.create(testTariffa);

		Tariffa[] testCase = {null, 
				 		   	  new Tariffa(), 
				 		   	  new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
			   	   						  "Tariffa base chilometrica", 8.0f, 2.0f),
			   	   		      new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				   	   					  "Tariffa base chilometrica", 8.0f, 2.0f)};
		testCase[2].setID("3");
		testCase[3].setID("1");
		
		boolean[] expectedResult = {false, false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				Tariffa tariffa = testCase[i];
				testDAO.update(tariffa);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Tariffa) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate_1() throws Exception
	{
		Tariffa testTariffa1 = new Tariffa("Go!Go!Go!", 15.0f, 0.0f, "Illimitata", 
				"Viaggia più che puoi!", 20.0f, 0.0f);
		testDAO.create(testTariffa1);

		Tariffa testTariffa2 = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				"Semplice tariffa base giornaliera", 10.0f, 3.0f);
		testDAO.create(testTariffa2);
		
		Tariffa testCase = new Tariffa("Go!Go!Go!", 
										8.0f, 2.0f, "Chilometrica", 
										"Tariffa base chilometrica", 
										12.0f, 4.0f);
		testCase.setID("2");

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
			assertNotEquals("update(Tariffa) TC5:", testDAO.read("2"), testCase);
		}
		
	}
	
	@Test
	public void testRead() throws Exception 
	{
		Tariffa testTariffa = new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
  					"Tariffa base chilometrica", 12.0f, 4.0f);
		testDAO.create(testTariffa);
		
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
				Tariffa result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testTariffa);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("read(Tariffa) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception {
		Tariffa testTariffa = new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
										  "Tariffa base chilometrica", 12.0f, 4.0f);
		testDAO.create(testTariffa);
		
		List<Tariffa> tariffe = testDAO.readAll(null);
		assertTrue(tariffe.contains(testTariffa));
	}
	
	@Test
	public void testReadAll_2() throws Exception {
		List<Tariffa> tariffe = testDAO.readAll(null);
		assertTrue(tariffe.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception {
		Tariffa testTariffa = new Tariffa("Base", 8.0f, 2.0f, "Chilometrica", 
				  "Tariffa base chilometrica", 12.0f, 4.0f);
		testDAO.create(testTariffa);
		
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
				assertEquals("delete(Tariffa) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(TariffaDAOTest.class);
	}
}