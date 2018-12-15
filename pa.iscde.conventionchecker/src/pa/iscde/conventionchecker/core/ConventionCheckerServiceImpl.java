package pa.iscde.conventionchecker.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Label;

import pa.iscde.conventionchecker.service.ConventionCheckerListener;
import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pa.iscde.conventionchecker.service.LogExt;
import pa.iscde.conventionchecker.visitor.ConventionVisitor;
import pa.iscde.conventionchecker.visitor.Parser;
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
	 * Creates the service. Allows to pass in a conventionRules object that holds any interaction with the rules. Also receives a custom visitor.
	 * 
	 * @param p_rules conventionRules object that holds any interaction with the rules
	 * @param p_visitor custom visitor to apply the rules to the classes
	 */
	public ConventionCheckerServiceImpl (ConventionRules p_rules, ConventionVisitor p_visitor) {
		rules = p_rules;
		this.checker = p_visitor;
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
	
	/**
	 * Adds a new listener to the convention checker service
	 * @param listener to be added
	 */
	@Override
	public void addListener(ConventionCheckerListener listener) {
		Assert.isNotNull(listener, "lsitener cannot be null");
		ConventionActivator.getInstance().addListener(listener);
	}
	
	/**
	 * Removes an existing listener from the convention checker service
	 * @param listener to be removed
	 */
	@Override
	public void removeListener(ConventionCheckerListener listener) {
		Assert.isNotNull(listener, "listener cannot be null");
		ConventionActivator.getInstance().removeListener(listener);
	}
	
	/**
	 * Parses all files in the project browser and annotates current opened file
	 */
	public void parseAll() {
		Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), this.getVisitor());
		this.annotateCurrentFile();
		ConventionActivator.getInstance().notifyListeners(this.getVisitor().getErrors());
	}
	
	/**
	 * Parses a single file. Used in open/save file listeners.
	 * @param f file that is going to be parsed
	 */
	public void parse(File f) {
		Parser.parse(f, this.getVisitor());
		this.annotateCurrentFile(f);
		ConventionActivator.getInstance().notifyListeners(this.getVisitor().getErrors());
	}
}
