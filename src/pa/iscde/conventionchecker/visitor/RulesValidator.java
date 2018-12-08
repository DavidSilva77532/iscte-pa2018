package pa.iscde.conventionchecker.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pa.iscde.conventionchecker.ext.LogExt;

public class RulesValidator {
	private ArrayList<LogExt> stack = new ArrayList<>();
		
	/**
	 * Validate the node name against the rule requested
	 * 
	 * @param p_input node name
	 * @param p_regex regex to validate against the input name
	 * @param p_offset offset in the file of this node
	 * @param p_line line where this node is
	 * @param p_fileName filename of the processed file
	 */
	public void validateRule(String p_input, String p_regex, int p_offset, int p_line, String p_fileName) {
		Pattern p = Pattern.compile("^" + p_regex + "$"); // we need the ^ and $ to match the beginning and end of the string
		Matcher m = p.matcher(p_input);

		if (!m.find())		
			addUnique(p_input, p_regex, p_offset, p_line, p_fileName);
	}
	
	/**
	 * Resets the current errors 
	 * Used for new validations
	 */
	public void resetStack() {
		stack = new ArrayList<LogExt>();
	}
	
	/**
	 * Resets the current errors for a specific file
	 * 
	 * @param p_fileName
	 */
	public ArrayList<LogExt> resetStack(String p_fileName) {
		//TODO- refactor this to use a global map instead of looping through the existing array			
		for(Iterator<LogExt> it = stack.iterator(); it.hasNext();) {
			LogExt log = it.next();
			if (log.getFilePath().equals(p_fileName))
				it.remove();
		}
		
		return stack;
	}
	
	/**
	 * @return all errors found
	 */
	public ArrayList<LogExt> getStack(){
		return stack;
	}
	
	/**
	 * @param p_fileName
	 * @return all errors found
	 */
	public ArrayList<LogExt> getStack(String p_fileName){
		//TODO- refactor this to use a global map instead of looping through the existing array
		ArrayList<LogExt> retStack = new ArrayList<>();
		
		for(LogExt log : stack) {
			if (log.getFilePath().equals(p_fileName))
				retStack.add(log);
		}
		
		return retStack;
	}
	
	/**
	 * Adds the node to the error list if that exact node hasn't been added before
	 * 
	 * @param p_input node name
	 * @param p_regex regex to validate against the input name
	 * @param p_offset offset in the file of this node
	 * @param p_line line where this node is
	 * @param p_fileName filename of the processed file
	 */
	private void addUnique(String p_input, String p_regex, int p_offset, int p_line, String p_fileName) {
		for(LogExt log : stack) {
			if (log.getValue().equals(p_input) && log.getPosition() == p_offset && log.getLine() == p_line && log.getFilePath().equals(p_fileName)) {
				return;
			}
		}
		
		stack.add(new Log(p_input, p_offset, "Value \"" + p_input + "\" does not match rule \"" + p_regex + "\"", p_line, p_fileName));
	}
	
}
