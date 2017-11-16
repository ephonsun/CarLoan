package integration.daos;

import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.Cliente;
import business.businessObjects.Contratto;
import business.businessObjects.Optional;
import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;
import business.businessObjects.Tariffa;
import business.businessObjects.UserPermission;
import business.businessObjects.Utente;

import org.junit.*;

import static org.junit.Assert.*;

public class ContrattoDAOTest {
	DAO<Contratto> testDAO;
	@Before
	public void setUp() throws Exception {
		testDAO = ContrattoDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.restoreTable("Login");
		TestUtil.restoreTable("Contratto");
		TestUtil.restoreTable("Autoveicolo");
		TestUtil.restoreTable("Sede");
		TestUtil.restoreTable("Stato_Autoveicolo");
		TestUtil.restoreTable("Categoria");
		TestUtil.restoreTable("Utente");
		TestUtil.restoreTable("Cliente");
		TestUtil.restoreTable("Optional");
		TestUtil.restoreTable("Tariffa");
		TestUtil.restoreTable("Optional_Contratto");
	}

	@Test
	public void testCreate() throws Exception
	{
		Sede testSede1 = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede1.setID("1");
		SedeDAO.getInstance().create(testSede1);
		
		Sede testSede2 = new Sede("Barletta", "Via Bacco, 88", "0883475193");
		testSede2.setID("2");
		SedeDAO.getInstance().create(testSede2);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede1);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
		  		  "AB123CX", "2000", "Manuale", 
		  		  "Airbag", 
		          testStato, testCategoria);
		AutoveicoloDAO.getInstance().create(testAutoveicolo);
		testAutoveicolo.setID("1");

		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		ClienteDAO.getInstance().create(testCliente); 
		testCliente.setID("1");
		
		Utente testUtente = new Utente("root.admin", "root.admin", 
					UserPermission.MANAGER,
					"Francesco", "Sinisi", "1", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"SNSFNC94A03B285K", testSede1);
		UtenteDAO.getInstance().create(testUtente);
		
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				  "Semplice tariffa base giornaliera", 10.0f, 3.0f);
		TariffaDAO.getInstance().create(testTariffa);
		testTariffa.setID("1");
		
		List<Optional> testOptionals = new ArrayList<Optional>();
		
		Contratto[] testCase = {null,
								new Contratto(),
								new Contratto(testCliente, testUtente, 
										LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
										LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
										null, 
										LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										testAutoveicolo, testOptionals, testSede1, 
										testSede2, "Aperto", 350.0f, 480, testTariffa)};
		testCase[2].setID("1");
		
		boolean[] expectedResult = {false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);
				Contratto result = testDAO.read("1");

				iResult = result != null;
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Contratto) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}	
		
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		Sede testSede1 = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede1.setID("1");
		SedeDAO.getInstance().create(testSede1);
		
		Sede testSede2 = new Sede("Barletta", "Via Bacco, 88", "0883475193");
		testSede2.setID("2");
		SedeDAO.getInstance().create(testSede2);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede1);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
		  		  "AB123CX", "2000", "Manuale", 
		  		  "Airbag", 
		          testStato, testCategoria);
		AutoveicoloDAO.getInstance().create(testAutoveicolo);
		testAutoveicolo.setID("1");

		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		ClienteDAO.getInstance().create(testCliente); 
		testCliente.setID("1");
		
		Utente testUtente = new Utente("root.admin", "root.admin", 
					UserPermission.MANAGER,
					"Francesco", "Sinisi", "1", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"SNSFNC94A03B285K", testSede1);
		UtenteDAO.getInstance().create(testUtente);
		
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				  "Semplice tariffa base giornaliera", 10.0f, 3.0f);
		TariffaDAO.getInstance().create(testTariffa);
		testTariffa.setID("1");
		
		List<Optional> testOptionals = new ArrayList<Optional>();
		
		Contratto testContratto = new Contratto(testCliente, testUtente, 
						LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
						LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
						null, 
						LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
						testAutoveicolo, testOptionals, testSede1, 
						testSede2, "Aperto", 270.0f, 480, testTariffa);
		testContratto.setID("1");
		testDAO.create(testContratto);
	
		Contratto[] testCase = {null, 
						new Contratto(),
						new Contratto(testCliente, testUtente, 
								LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								null, 
								LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								testAutoveicolo, testOptionals, testSede1, 
								testSede2, "Aperto", 270.0f, 480, testTariffa),
						new Contratto(testCliente, testUtente, 
								LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								null, 
								LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								testAutoveicolo, testOptionals, testSede1, 
								testSede2, "Aperto", 270.0f, 480, testTariffa)};

		testCase[2].setID("3");
		testCase[3].setID("1");

		boolean[] expectedResult = {false, false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;

			try
			{
				Contratto contratto = testCase[i];
				testDAO.update(contratto);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Contratto) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testRead() throws Exception
	{
		Sede testSede1 = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede1.setID("1");
		SedeDAO.getInstance().create(testSede1);
		
		Sede testSede2 = new Sede("Barletta", "Via Bacco, 88", "0883475193");
		testSede2.setID("2");
		SedeDAO.getInstance().create(testSede2);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede1);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
		  		  "AB123CX", "2000", "Manuale", 
		  		  "Airbag", 
		          testStato, testCategoria);
		AutoveicoloDAO.getInstance().create(testAutoveicolo);
		testAutoveicolo.setID("1");

		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		ClienteDAO.getInstance().create(testCliente); 
		testCliente.setID("1");
		
		Utente testUtente = new Utente("root.admin", "root.admin", 
					UserPermission.MANAGER,
					"Francesco", "Sinisi", "1", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"SNSFNC94A03B285K", testSede1);
		UtenteDAO.getInstance().create(testUtente);
		
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				"Semplice tariffa base giornaliera", 10.0f, 3.0f);
		TariffaDAO.getInstance().create(testTariffa);
		testTariffa.setID("1");

		List<Optional> testOptionals = new ArrayList<Optional>();

		Contratto testContratto = new Contratto(testCliente, testUtente, 
								LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								null, 
								LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								testAutoveicolo, testOptionals, testSede1, 
								testSede2, "Aperto", 270.0f, 480, testTariffa);
		testContratto.setID("1");
		testDAO.create(testContratto);

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
				Contratto result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testContratto);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("read(Contratto) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception
	{
		Sede testSede1 = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede1.setID("1");
		SedeDAO.getInstance().create(testSede1);
		
		Sede testSede2 = new Sede("Barletta", "Via Bacco, 88", "0883475193");
		testSede2.setID("2");
		SedeDAO.getInstance().create(testSede2);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede1);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
		  		  "AB123CX", "2000", "Manuale", 
		  		  "Airbag", 
		          testStato, testCategoria);
		AutoveicoloDAO.getInstance().create(testAutoveicolo);
		testAutoveicolo.setID("1");

		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		ClienteDAO.getInstance().create(testCliente); 
		testCliente.setID("1");
		
		Utente testUtente = new Utente("root.admin", "root.admin", 
					UserPermission.MANAGER,
					"Francesco", "Sinisi", "1", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"SNSFNC94A03B285K", testSede1);
		UtenteDAO.getInstance().create(testUtente);
		
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				"Semplice tariffa base giornaliera", 10.0f, 3.0f);
		TariffaDAO.getInstance().create(testTariffa);
		testTariffa.setID("1");

		List<Optional> testOptionals = new ArrayList<Optional>();

		Contratto testContratto = new Contratto(testCliente, testUtente, 
								LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								null, 
								LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								testAutoveicolo, testOptionals, testSede1, 
								testSede2, "Aperto", 270.0f, 480, testTariffa);
		testContratto.setID("1");
		testDAO.create(testContratto);
		
		List<Contratto> contratti = testDAO.readAll(null);
		assertTrue(contratti.contains(testContratto));
	}
	
	@Test
	public void testReadAll_2() throws Exception
	{
		List<Contratto> contratti = testDAO.readAll(null);
		assertTrue(contratti.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception
	{
		Sede testSede1 = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede1.setID("1");
		SedeDAO.getInstance().create(testSede1);
		
		Sede testSede2 = new Sede("Barletta", "Via Bacco, 88", "0883475193");
		testSede2.setID("2");
		SedeDAO.getInstance().create(testSede2);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede1);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
		  		  "AB123CX", "2000", "Manuale", 
		  		  "Airbag", 
		          testStato, testCategoria);
		AutoveicoloDAO.getInstance().create(testAutoveicolo);
		testAutoveicolo.setID("1");

		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		ClienteDAO.getInstance().create(testCliente); 
		testCliente.setID("1");
		
		Utente testUtente = new Utente("root.admin", "root.admin", 
					UserPermission.MANAGER,
					"Francesco", "Sinisi", "1", 
					LocalDate.parse("03-08-1994", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					"SNSFNC94A03B285K", testSede1);
		UtenteDAO.getInstance().create(testUtente);
		
		Tariffa testTariffa = new Tariffa("Base", 6.0f, 1.0f, "Chilometrica", 
				"Semplice tariffa base giornaliera", 10.0f, 3.0f);
		TariffaDAO.getInstance().create(testTariffa);
		testTariffa.setID("1");

		List<Optional> testOptionals = new ArrayList<Optional>();

		Contratto testContratto = new Contratto(testCliente, testUtente, 
								LocalDate.parse("15-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								LocalDate.parse("19-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
								null, 
								LocalDate.parse("10-02-2016", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
								testAutoveicolo, testOptionals, testSede1, 
								testSede2, "Aperto", 270.0f, 480, testTariffa);
		testContratto.setID("1");
		testDAO.create(testContratto);
		
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
				assertEquals("delete(Contratto) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ContrattoDAOTest.class);
	}
}