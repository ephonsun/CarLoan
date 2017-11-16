package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.Sede;

import org.junit.*;

import static org.junit.Assert.*;

public class SedeDAOTest {

	DAO<Sede> testDAO;
	
	@Before
	public void setUp() throws Exception {
		testDAO = SedeDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.restoreTable("Sede");
	}
	
	@Test
	public void testCreate() throws Exception 
	{
		Sede[] testCase = {null,
						   new Sede(),
						   new Sede("Andria", "Via Colbacco, 23", "0883447852")};
		
		boolean[] expectedResult = {false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);
				
				Sede result = testDAO.read("1");

				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Sede) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}	
	}
	
	@Test
	public void testUpdate() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testDAO.create(testSede);

		Sede[] testCase = {null, 
				 		   new Sede(), 
				 		   new Sede("Andria", "Piazza Aperta, 154", "0883785216"),
				 		   new Sede("Andria", "Piazza Aperta, 154", "0883785216")};
		
		testCase[2].setID("3");
		testCase[3].setID("1");
		
		boolean[] expectedResult = {false, false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				Sede sede = testCase[i];
				testDAO.update(sede);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Sede) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testRead() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testDAO.create(testSede);

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
				Sede result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testSede);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("read(Sede) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testDAO.create(testSede);

		List<Sede> sedi = testDAO.readAll(null);

		assertTrue(sedi.contains(testSede));
	}
	
	@Test
	public void testReadAll_2() throws Exception {
		List<Sede> sedi = testDAO.readAll(null);
		assertTrue(sedi.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testDAO.create(testSede);

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
				assertEquals("delete(Sede) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(SedeDAOTest.class);
	}
}