package integration.daos;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ImpiegatoDAOTest.class,
	UtenteDAOTest.class,
	StatoAutoveicoloDAOTest.class,
	TariffaDAOTest.class,
	CategoriaDAOTest.class,
	SedeDAOTest.class,
	LoginDAOTest.class,
	ContrattoDAOTest.class,
	ClienteDAOTest.class,
	AutoveicoloDAOTest.class,
	OptionalDAOTest.class,
})
public class TestAll {

	public static void main(String[] args) {
		JUnitCore.runClasses(new Class[] { TestAll.class });
	}
}
