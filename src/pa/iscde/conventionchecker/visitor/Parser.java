package pa.iscde.conventionchecker.visitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public class Parser {
	
	/**
	 * parse a file and validate all the convention rules applicable
	 * 
	 * @param file is the file that is going to be analyzed
	 * @param visitor the class that is going to go through the syntax tree and apply the rules
	 * @return AST parser
	 */
	public static void parse(File file, ASTVisitor visitor) {
		assert file.exists() && file.isFile();
		
		// Use absolute path to avoid confusing 2 files with the same name
		((ConventionVisitor) visitor).setFileName(file.getAbsolutePath());
		
		ASTParser parser = getParser(file);
		String src = readSource(file);
		parser.setSource(src.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		unit.accept(visitor);
	}
	
	/**
	 * get the AST parser and set all configs
	 * 
	 * @return AST parser
	 */
	private static ASTParser getParser(File f) {
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		
		return parser;
	}

	/**
	 * reads the file to be processed by the parser
	 * 
	 * @return the file as a string
	 */
	private static String readSource(File file) {
		StringBuilder src = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine())
				src.append(scanner.nextLine()).append("\r\n");
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return src.toString();
	}
	
	/**
	 * Go through all the files and parse them all
	 * 
	 * @param files is a collection of all the files to be processed
	 * @param visitor the class that is going to go through the syntax tree and apply the rules
	 */
	public static void parseAll(SortedSet<SourceElement> files, ASTVisitor visitor) {
		// Parse multiple files
		for (SourceElement e : files) {
			if (e.isPackage())
				parseAll(((PackageElement) e).getChildren(), visitor);
			else {
				parse(e.getFile(), visitor);
			}
		}
	}
}

