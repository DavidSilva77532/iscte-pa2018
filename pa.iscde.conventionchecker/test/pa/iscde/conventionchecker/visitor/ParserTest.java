package pa.iscde.conventionchecker.visitor;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import pa.iscde.conventionchecker.core.ConventionRules;
import pa.iscde.conventionchecker.core.ConventionRulesTest;

public class ParserTest {

	private String testFolder = ConventionRulesTest.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/bin", "") + "/test/testFiles/";

	@Test
	public void testParseFail() {
		ConventionVisitor visitor = new ConventionVisitor(new ConventionRules(testFolder + "rules"));
		File f = new File(testFolder + "classes/Application.java");
		Parser.parse(f, visitor);
		
		assertEquals(10, visitor.getErrors().size());
		assertEquals("Application", visitor.getErrors().get(0).getValue());
	}
	
	@Test
	public void testParseOK() {
		ConventionVisitor visitor = new ConventionVisitor(new ConventionRules(testFolder + "rules"));
		File f = new File(testFolder + "classes/c_ApplicationOK.java");
		Parser.parse(f, visitor);
		
		assertEquals(0, visitor.getErrors().size());
	}

}
