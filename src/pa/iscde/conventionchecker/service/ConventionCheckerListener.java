package pa.iscde.conventionchecker.service;

import java.util.ArrayList;

public interface ConventionCheckerListener {

	/**
	 * Sends the convention errors each time the processing finishes
	 * 
	 * @param conventionErrors all the errors found in the current processing
	 */
	void checkingFinished(ArrayList<LogExt> conventionErrors);

}
