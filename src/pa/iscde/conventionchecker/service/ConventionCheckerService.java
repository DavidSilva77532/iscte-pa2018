package pa.iscde.conventionchecker.service;

import java.util.ArrayList;

import pa.iscde.conventionchecker.visitor.Log;

public interface ConventionCheckerService {

	/**
	 * Get the file that is opened (on top) in the editor.
	 * @return a reference to the opened file, or null if no file is opened
	 */
	ArrayList<Log> getConventionErrors();
}
