package pt.iscte.pidesco.conventionchecker.visitor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulesValidator {
	private ArrayList<Log> stack = new ArrayList<>();
	
	/**
	 * Validate the node name against the rule requested
	 * @param p_input node name
	 * @param p_regex regex to validate against the input name
	 * @param p_offset offset in the file of this node
	 * @param p_line line where this node is
	 */
	public void validateRule(String p_input, String p_regex, int p_offset, int p_line) {
		Pattern p = Pattern.compile("^" + p_regex + "$"); // we need the ^ and $ to match the beginning and end of the string
		Matcher m = p.matcher(p_input);

		if (!m.find())		
			addUnique(p_input, p_regex, p_offset, p_line);
	}
	
	/**
	 * Resets the current errors 
	 * Used for new validations
	 */
	public void resetStack() {
		stack = new ArrayList<>();
	}
	
	/**
	 * @return all errors found
	 */
	public ArrayList<Log> getStack(){
		return stack;
	}
	
	/**
	 * Adds the node to the error list if that exact node hasn't been added yet
	 * @param p_input node name
	 * @param p_regex regex to validate against the input name
	 * @param p_offset offset in the file of this node
	 * @param p_line line where this node is
	 */
	private void addUnique(String p_input, String p_regex, int p_offset, int p_line) {
		for(Log log : stack) {
			if (log.getValue().equals(p_input) && log.getPosition() == p_offset && log.getLine() == p_line) {
				return;
			}
		}
		
		stack.add(new Log(p_input, p_offset, "Value \"" + p_input + "\" does not match rule " + p_regex, p_line));
	}
	
	
	/*public void printStack() {
		for (int i = 0; i < stack.size(); i++) {
			Log myLog = stack.get(i);
			System.out.println(myLog.getMessage());
		}	
	}*/
	
}
