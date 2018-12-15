package pa.iscde.conventionchecker.visitor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RulesValidatorTest {

	RulesValidator validator;
	
	@Before
	public void setUp() {
		validator = new RulesValidator();
	}
	
	@Test
	public void testValidateRule() {
		validator.validateRule("test", "[a-z]+", 100, 3, "file1");
		assertEquals(validator.getStack().size(), 0);
	}
	
	@Test
	public void testFailValidateRule() {
		String t = "test";
		validator.validateRule(t, "[A-Z]+", 100, 3, "file1");
		assertEquals(validator.getStack().size(), 1);
		assertEquals(validator.getStack().get(0).getValue(), t);
	}

	@Test
	public void testResetStack() {
		String t = "test";
		assertEquals(validator.getStack().size(), 0);
		
		validator.validateRule(t, "[A-Z]+", 100, 3, "file1");
		assertEquals(validator.getStack().size(), 1);
		
		validator.resetStack();
		assertEquals(validator.getStack().size(), 0);
	}

	@Test
	public void testResetStackString() {
		String t = "test";
		assertEquals(validator.getStack().size(), 0);
		
		validator.validateRule(t, "[A-Z]+", 100, 3, "file1");
		validator.validateRule(t, "[A-Z]+", 100, 3, "file2");

		assertEquals(validator.getStack().size(), 2);
		
		validator.resetStack("file1");
		assertEquals(validator.getStack().size(), 1);
	}

	@Test
	public void testGetStack() {
		String t = "test";
		validator.validateRule(t, "[A-Z]+", 100, 3, "file1");
		assertEquals(validator.getStack().get(0).getValue(), t);
		assertEquals(validator.getStack().get(0).getPosition(), 100);
		assertEquals(validator.getStack().get(0).getLine(), 3);
	}

	@Test
	public void testGetStackString() {
		String t = "test";
		validator.validateRule(t, "[A-Z]+", 100, 3, "file1");
		validator.validateRule(t, "[A-Z]+", 200, 6, "file2");

		assertEquals(validator.getStack("file2").get(0).getValue(), t);
		assertEquals(validator.getStack("file2").get(0).getPosition(), 200);
		assertEquals(validator.getStack("file2").get(0).getLine(), 6);
	}

}
