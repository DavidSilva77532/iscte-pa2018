package pa.iscde.conventionchecker.ext;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;


public interface ConventionCheckerExt {

	/**
	 * Rule types available to access the rule table.
	 * Each one maps to a key of the rule Map
	 */
	String VARIABLE = "Variable";
	String CONSTANT = "Constant";
	String CLASS = "Class";
	String METHOD = "Method";
	String INTERFACE = "Interface";
	String PARAMETER = "Parameter";
	
	/**
	 * Allows others to change the convention checker view. They can add objects as pleased.
	 * They also have access to the regex rules
	 * 
	 * @param area of the component
	 * @param rules of the convention checker. Use one of the options available in this interface spec (Rule types)
	 */
	public void changeView(Composite area, Map<String, String> rules);
	
	/**
	 * Faz sentido ser ponto de extensao? I guess not
	 */
	//public void getErrors(ArrayList<LogExt> errors);
}
