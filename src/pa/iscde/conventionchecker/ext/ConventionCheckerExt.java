package pa.iscde.conventionchecker.ext;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;


public interface ConventionCheckerExt {
	
	/**
	 * Allows others to change the convention checker view. They can add objects as pleased.
	 * They also have access to the regex rules
	 * 
	 * @param area of the component
	 * @param rules of the convention checker
	 */
	public void changeView(Composite area, Map<String, String> rules);
	
	/**
	 * Faz sentido ser ponto de extensao? I guess not
	 */
	public void getErrors(ArrayList<LogExt> errors);
}
