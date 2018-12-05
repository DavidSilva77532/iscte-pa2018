package ASTVisitor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulesValidator {
	private ArrayList<Log> stack = new ArrayList<>();
	
	public void validateRule(String p_input, String p_regex, int p_offset) {
		Pattern p = Pattern.compile("^" + p_regex + "$"); // we need the ^ and $ to match the beginning and end of the string
		Matcher m = p.matcher(p_input);

		if (!m.find())		
			stack.add(new Log(p_input, p_offset, "Value \"" + p_input + "\" does not match rule " + p_regex));
	}
	
	public void resetStack() {
		stack = new ArrayList<>();
	}
	
	public ArrayList<Log> getStack(){
		return stack;
	}
	
	public void printStack() {
		for (int i = 0; i < stack.size(); i++) {
			Log myLog = stack.get(i);
			System.out.println(myLog.getMessage());
		}	
	}
	
}
