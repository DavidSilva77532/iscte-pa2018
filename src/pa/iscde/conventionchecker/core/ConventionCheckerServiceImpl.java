package pa.iscde.conventionchecker.core;

import java.io.File;
import java.util.ArrayList;

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
