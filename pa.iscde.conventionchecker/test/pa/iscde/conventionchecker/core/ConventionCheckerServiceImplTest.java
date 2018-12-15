package pa.iscde.conventionchecker.core;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import pa.iscde.conventionchecker.visitor.ConventionVisitor;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;


public class ConventionCheckerServiceImplTest {
	
	private String testFolder = ConventionRulesTest.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/bin", "") + "/test/testFiles/rules";
	ConventionCheckerServiceImpl service;
	
	@Before
	public void setUp() {
		ConventionRules rules = new ConventionRules(testFolder);
		ConventionVisitor visitor = new ConventionVisitor(rules);
		service = new ConventionCheckerServiceImpl(rules, visitor);
	}
	
	@Test
	public void testChangeProfile() {		
		ConventionRules rules = new ConventionRules(testFolder);
		ConventionVisitor visitor = new ConventionVisitor(rules);
		ConventionCheckerServiceImpl service = new ConventionCheckerServiceImpl(rules, visitor);

		assertNotNull(service);
	}


	@Test
	public void testSetJavaEditor() {
		JavaEditorServices javaserv = null;
		service.setJavaEditor(javaserv);
	}

	@Test
	public void testGetRuleManager() {
		ConventionRules rules = new ConventionRules(testFolder);
		ConventionVisitor visitor = new ConventionVisitor(rules);
		ConventionCheckerServiceImpl service = new ConventionCheckerServiceImpl(rules, visitor);
		assertEquals(rules,service.getRuleManager());
	}


	@Test
	public void testGetVisitor() {
		ConventionRules rules = new ConventionRules(testFolder);
		ConventionVisitor visitor = new ConventionVisitor(rules);
		ConventionCheckerServiceImpl service = new ConventionCheckerServiceImpl(rules, visitor);
		assertEquals(visitor,service.getVisitor());
	}


	@Test
	public void testAnnotateCurrentFileFile() {
		File f = new File(testFolder + "/default.file");
		service.annotateCurrentFile(f);
	}

	@Test
	public void testGetConventionErrors() {
		assertEquals(0, service.getConventionErrors().size());
	}


}
