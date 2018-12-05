package ASTVisitor;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.ConventionChecker.ConventionRules;
import pt.iscte.pidesco.javaeditor.service.AnnotationType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ConventionVisitor extends ASTVisitor {

	private RulesValidator myErrors = new RulesValidator();
	private ConventionRules rules;
	
	public ConventionVisitor(ConventionRules p_rules, JavaEditorServices p_serv) {
		this.rules = p_rules;
	}
	
	public void printStack() {
		myErrors.printStack();
	}
	
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
	/*private static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}*/
	
	// visits class/interface declaration
			@Override
			public boolean visit(TypeDeclaration node) {
				String name = node.getName().toString();								
				return true;
			}
			
	@Override
	public boolean visit(MethodDeclaration node) {
		return true;
	}

	// visits attributes
	@Override
	public boolean visit(FieldDeclaration node) {
		// loop for several variables in the same declaration
		for(Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			
			
			
			
			
			String name = var.getName().toString();
			myErrors.validateRule(var.getName().toString(), rules.getRules().get("Variable"), var.getStartPosition());

			//boolean isStatic = Modifier.isStatic(node.getModifiers());
			//boolean isFinal = Modifier.isFinal(node.getModifiers());
			System.out.println(name);
			
			System.out.println("off:" + 
					var.getStartPosition()  
			+ " length:" + name.length());
			
			
			
			
			
			/*if (isStatic && isFinal)
				myErrors.checkConstCase(name, sourceLine(node));
			else
				myErrors.checkLowerCase(name, sourceLine(node));*/

		}
		return true; // false to avoid child VariableDeclarationFragment to be processed again
	}
	
	
	// visits variable declarations
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		String name = node.getName().toString();
		System.out.println(node.getRoot().toString());
		//myErrors.checkLowerCase(name, sourceLine(node));
		
		return true;
	}

}