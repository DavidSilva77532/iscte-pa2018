package pt.iscte.pidesco.conventionchecker.service;

import java.util.ArrayList;

import pt.iscte.pidesco.conventionchecker.visitor.Log;

public interface ConventionCheckerService {

	/**
	 * Get the file that is opened (on top) in the editor.
	 * @return a reference to the opened file, or null if no file is opened
	 */
	ArrayList<Log> getConventionErrors();
}
