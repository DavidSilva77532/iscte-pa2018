package pa.iscde.conventionchecker.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pa.iscde.conventionchecker.service.LogExt;
import pa.iscde.conventionchecker.visitor.ConventionVisitor;
import pt.iscte.pidesco.javaeditor.service.AnnotationType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ConventionCheckerServiceImpl implements ConventionCheckerService{
	private ConventionVisitor checker;
	private ConventionRules rules;
	private JavaEditorServices javaServ;
	
	public ConventionCheckerServiceImpl () {
		rules = new ConventionRules();
		this.checker = new ConventionVisitor(rules);
	}
	
	/**
	 * changes profile
	 * 
	 * @param p_profileName
	 */
	public void changeProfile(String p_profileName) {
		rules.changeProfile(p_profileName);
	}
	
	/**
	 * create a new file for the new profile
	 * 
	 * @param p_profileName
	 * @param p_rules
	 */
	public void addNewProfile(String p_profileName, Map<String, String> p_rules) {
		rules.createNewFile(p_profileName, p_rules);
	}
	
	/**
	 * get a list of all profiles
	 * 
	 * @return a list of profiles
	 */
	public String[] getProfiles() {
		return rules.getProfiles();
	}
	
	/**
	 * Set the java editor service
	 * 
	 */
	public void setJavaEditor(JavaEditorServices p_javaServ) {
		javaServ = p_javaServ;
	}
	
	/**
	 * Returns the rules object
	 * 
	 * @return rules
	 */
	public ConventionRules getRuleManager() {
		return rules;
	}
	
	/**
	 * Cleans all errors 
	 */
	public void resetStack() {
		checker.resetStack();
	}
	
	/**
	 * Cleans all errors for the specified file
	 * 
	 * @param p_filePath absolute path to the file we want the errors for
	 */
	public void resetStack(String p_filePath) {
		checker.resetStack(p_filePath);
	}
	
	/**
	 * Gets the visitor used in the parser
	 * 
	 * @return visitor object
	 */
	public ConventionVisitor getVisitor() {
		return checker;
	}
	
	/**
	 * Adds annotations to the current opened file
	 */
	public void annotateCurrentFile() {
		File f = javaServ.getOpenedFile();

		if (f != null)
			for(LogExt l: getFileConventionErrors(javaServ.getOpenedFile().getAbsolutePath())) {
				javaServ.addAnnotation(javaServ.getOpenedFile(), AnnotationType.WARNING, l.getMessage(), l.getPosition()
						, l.getValue().length());
			}
	}
	
	/**
	 * Adds annotations to the passed in file
	 */
	public void annotateCurrentFile(File f) {
		if (f != null)
			for(LogExt l: getFileConventionErrors(f.getAbsolutePath())) {
				javaServ.addAnnotation(f, AnnotationType.WARNING, l.getMessage(), l.getPosition()
						, l.getValue().length());
			}
	}

	/**
	 * Gets all convention errors
	 * 
	 * @return convention errors
	 */
	@Override
	public ArrayList<LogExt> getConventionErrors() {
		return checker.getErrors();
	}
	
	/**
	 * Gets all convention errors for a specific file
	 * 
	 * @param p_filePath absolute path to the file we want the errors for
	 * @return convention errors for the specified file
	 */
	@Override
	public ArrayList<LogExt> getFileConventionErrors(String p_filePath) {
		return checker.getErrors(p_filePath);
	}

}
