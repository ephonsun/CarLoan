package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;

import org.junit.*;

import static org.junit.Assert.*;

public class StatoAutoveicoloDAOTest {
	DAO<StatoAutoveicolo> testDAO;
	
	@Before
	public void setUp()	throws Exception {
		testDAO = StatoAutoveicoloDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.restoreTable("Stato_Autoveicolo");
		TestUtil.restoreTable("Sede");
	}
	
	@Test
	public void testCreate() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo[] testCase = {null,
									   new StatoAutoveicolo(),
									   new StatoAutoveicolo(25000, "Disponibile", "Graffi sulla carrozzeria", testSede)};
	
		boolean[] expectedResult = {false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);
				
				StatoAutoveicolo result = testDAO.read("1");
				
				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("create(StatoAutoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
												"Graffi sulla carrozzeria", 
												testSede);
		testDAO.create(testStato);
		
		
		StatoAutoveicolo[] testCase = {null,
				   					   new StatoAutoveicolo(),
				   					   new StatoAutoveicolo(25000, "In manutenzione", 
				   							   "Graffi sulla carrozzeria e specchietto rotto", 
				   							   testSede),
				   					   new StatoAutoveicolo(25000, "In manutenzione", 
				   							   "Graffi sulla carrozzeria e specchietto rotto", 
				   							   testSede)};
		testCase[2].setID("3");
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
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("update(StatoAutoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testRead() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
												"Graffi sulla carrozzeria", 
												testSede);
		testDAO.create(testStato);
		
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
				StatoAutoveicolo result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testStato);
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("read(StatoAutoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
												"Graffi sulla carrozzeria", 
												testSede);
		testDAO.create(testStato);
		
		List<StatoAutoveicolo> stati = testDAO.readAll(null);
		assertTrue(stati.contains(testStato));
	}
	
	@Test
	public void testReadAll_2() throws Exception {
		List<StatoAutoveicolo> stati = testDAO.readAll(null);
		assertTrue(stati.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception {
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
												"Graffi sulla carrozzeria", 
												testSede);
		testDAO.create(testStato);
		
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
				assertEquals("delete(StatoAutoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(StatoAutoveicoloDAOTest.class);
	}
}