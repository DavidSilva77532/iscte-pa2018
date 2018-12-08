package pt.iscte.pidesco.conventionchecker.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.conventionchecker.ConventionRules;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ConventionVisitor extends ASTVisitor {

	private RulesValidator myErrors = new RulesValidator();
	private ConventionRules rules;
	
	public ConventionVisitor(ConventionRules p_rules, JavaEditorServices p_serv) {
		this.rules = p_rules;
	}
	
	//public void printStack() {
		//myErrors.printStack();
	//}
	
	public void resetStack() {
		myErrors.resetStack();
	}
	
	public ArrayList<Log> getErrors(){
		return myErrors.getStack();
	}
	
	/**
	 * returns the line where it was parsed.
	 * @param node
	 * @return line number
	 */
	private static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}
	
	
	/**
	 * visits interfaces and classes nodes
	 * @param node
	 * @return
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		SimpleName var = node.getName();
		String name = var.toString();
		
		if (node.isInterface())
			myErrors.validateRule(name.toString(), rules.getRules().get("Interface"), var.getStartPosition(), sourceLine(var));
		else
			myErrors.validateRule(name.toString(), rules.getRules().get("Class"), var.getStartPosition(), sourceLine(var));
		
		return true;
	}
	
	/**
	 * visits Methods nodes and parameters
	 * @param node
	 * @return
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		SimpleName var = node.getName();
		String name = var.toString();
		
		myErrors.validateRule(name.toString(), rules.getRules().get("Variable"), var.getStartPosition(), sourceLine(var));

		// Test methods parameters
		List<SingleVariableDeclaration> parameters = node.parameters();
		for (SingleVariableDeclaration o : parameters) {
			SimpleName paramName = o.getName();
			myErrors.validateRule(paramName.toString(), rules.getRules().get("Parameter"), paramName.getStartPosition(), sourceLine(paramName));
		}
		
		return true;
	}

	/**
	 * visits Variable declaration nodes
	 * @param node
	 * @return
	 */
	@Override
	public boolean visit(FieldDeclaration node) {

		for(Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			String name = var.getName().toString();
			boolean isFinal = Modifier.isFinal(node.getModifiers());
			
			if (isFinal)
				myErrors.validateRule(name.toString(), rules.getRules().get("Constant"), var.getStartPosition(), sourceLine(var));
			else
				myErrors.validateRule(name.toString(), rules.getRules().get("Variable"), var.getStartPosition(), sourceLine(var));

		}
		return true;
	}
	
			

	/**
	 * visits variable declarations inside functions (all variables in fact)
	 * We still need the other node validation to acess the modifiers of constants
	 * @param node
	 * @return
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		SimpleName var = node.getName();
		String name = var.toString();
				
		myErrors.validateRule(name.toString(), rules.getRules().get("Variable"), var.getStartPosition(), sourceLine(var));
				
		return true;
	}

}