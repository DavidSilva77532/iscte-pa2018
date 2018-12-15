package pa.iscde.conventionchecker.core;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class ConventionRulesTest {

	private String testFolder = ConventionRulesTest.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/bin", "") + "/test/testFiles/rules";
	ConventionRules rules;
	
	@Before
	public void setUp() {
		rules = new ConventionRules(testFolder);
	}
	
	@Test
	public void testConventionRules() {
		ConventionRules rules = new ConventionRules(testFolder);

		assertNotNull(rules);
		assertEquals(2, rules.getProfiles().length);
	}

	@Test
	public void testChangeProfile() {
		rules.changeProfile("default");
		
		assertEquals("v_[a-z][a-zA-z]+", rules.getRules().get("Variable"));
		
		rules.changeProfile("otherProfile");
		assertEquals(".*", rules.getRules().get("Variable"));
		
	}

	@Test
	public void testUpdateRule() {
		rules.changeProfile("otherProfile");
		rules.updateRule("Parameter", "p_p");
		assertEquals("p_p", rules.getRules().get("Parameter"));
	}

	@Test
	public void testGetSize() {
		assertEquals(6, rules.getSize());
	}

	@Test
	public void testGetKeys() {
		Set<String> keys = rules.getKeys();
		assertTrue(keys.contains("Variable"));
	}

	@Test
	public void testCreateNewFile() {
		Map<String, String> newRules = new HashMap<>();
		newRules.put("Variable", "something");
		rules.createNewFile("test", newRules);
		
		File f = new File(testFolder + "/test.file");
		
		assertTrue(f.exists());
		f.delete();
	}

	@Test
	public void testSaveFile() {
		rules.changeProfile("otherProfile");
		rules.getRules().put("Parameter", "something");
		rules.saveFile();
		rules.changeProfile("default");
		
		assertEquals("p_.*", rules.getRules().get("Parameter"));
		rules.changeProfile("otherProfile");
		assertEquals("something", rules.getRules().get("Parameter"));

		rules.getRules().put("Parameter", "");
		rules.saveFile();

	}

	@Test
	public void testProfileExists() {
		assertTrue(rules.profileExists("otherProfile"));
		assertFalse(rules.profileExists("aaa"));

	}

	@Test
	public void testGetProfiles() {
		assertEquals(2, rules.getProfiles().length);
	}

}
