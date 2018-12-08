package pa.iscde.convetionchecker.view;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;

import pa.iscde.conventionchecker.core.ConventionActivator;
import pa.iscde.conventionchecker.core.ConventionRules;
import pa.iscde.conventionchecker.visitor.ConventionVisitor;
import pa.iscde.conventionchecker.visitor.Log;
import pa.iscde.conventionchecker.visitor.Parser;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.AnnotationType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ConventionCheckerView implements PidescoView {
	private ConventionRules rules;
	private RulesTable conventionTable;
	private JavaEditorServices javaServ;
	private ConventionVisitor checker;

	
	public ConventionCheckerView() {
		rules = new ConventionRules();
		this.javaServ = ConventionActivator.getInstance().getJavaEditorService();
		this.checker = new ConventionVisitor(rules, javaServ);
	}
	
	
	/**
	 * Create Save button on my view to save the grid to the rule file
	 */
	private void createSaveButton(Composite viewArea, Map<String, Image> imageMap) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("Save.png"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				rules.saveFile();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Create Refresh button on my view to parse the files again
	 */
	private void createRefreshButton(Composite viewArea, Map<String, Image> imageMap) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("refresh.gif"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				checker.resetStack();
				Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), checker);
				annotateCurrentFile();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Adds annotations to the current opened file
	 */
	private void annotateCurrentFile() {
		File f = javaServ.getOpenedFile();
		
		if (f != null)
			for(Log l: checker.getErrors(javaServ.getOpenedFile().getAbsolutePath())) {
				javaServ.addAnnotation(javaServ.getOpenedFile(), AnnotationType.WARNING, l.getMessage(), l.getPosition()
						, l.getValue().length());
			}
	}
	
	/**
	 * Create table and prepare all listeners. 
	 * Also populate the initial table with the rules file.
	 * @param p_view
	 */
	public void createTable(Composite p_view) {
		conventionTable = new RulesTable(rules, p_view);
	}
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		BundleContext context = ConventionActivator.getContext();
		
		createTable(viewArea);
		createSaveButton(viewArea, imageMap);
		createRefreshButton(viewArea, imageMap);
		
		Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), checker);

		JavaEditorListener listener = new JavaEditorListener.Adapter() {
			@Override
			public void fileSaved(File file) {
				checker.resetStack(file.getAbsolutePath());
				Parser.parse(file, checker);
				//annotateCurrentFile();
			}
			
			@Override
			public void fileOpened(File file) {
				checker.resetStack(file.getAbsolutePath());
				Parser.parse(file, checker);
				//annotateCurrentFile();
			}
		};
		
		this.javaServ.addListener(listener);
	}

}
