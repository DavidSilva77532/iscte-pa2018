package pa.iscde.conventionchecker.view;

import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import pa.iscde.conventionchecker.core.ConventionRules;
import pa.iscde.conventionchecker.core.ConventionRulesTest;

public class RulesTableTest {

	private String testFolder = ConventionRulesTest.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/bin", "") + "/test/testFiles/rules";
	private ConventionRules rules;
	private RulesTable table;
	private Shell shell;
	
	@Before
	public void setUp() {
		rules = new ConventionRules(testFolder);
		Display display = new Display();
	    shell = new Shell(display);
		table = new RulesTable(rules, shell);
	}
	
	@Test
	public void constructorTest() {
		assertNotNull(table);
		assertNotNull(shell.getChildren()); // has objects drawn
	}

}
