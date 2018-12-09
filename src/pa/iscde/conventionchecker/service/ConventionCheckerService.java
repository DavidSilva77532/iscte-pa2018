package pa.iscde.conventionchecker.service;

import java.util.ArrayList;

public interface ConventionCheckerService {

	/**
	 * Get all convention errors
	 * 
	 * @return an array with all the errors
	 */
	ArrayList<LogExt> getConventionErrors();

	/**
	 * Get all convention errors from a specific file
	 * 
	 * @param p_filePath Absolute path to the file we want to get the errors from
	 * @return an array with all the errors from the specified file
	 */
	ArrayList<LogExt> getFileConventionErrors(String p_filePath);
}
