package pt.iscte.pidesco.conventionchecker;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import pt.iscte.pidesco.conventionchecker.visitor.ConventionVisitor;
import pt.iscte.pidesco.conventionchecker.visitor.Log;
import pt.iscte.pidesco.conventionchecker.visitor.Parser;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.AnnotationType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ConventionCheckerView implements PidescoView {
	private ConventionRules rules;
	private RulesTable conventionTable;
	private JavaEditorServices javaServ;
	//private JavaEditorListener javaListener;
	
	public ConventionCheckerView() {
		rules = new ConventionRules();
	}
	
	
	/**
	 * Create Save button on my view to save the grid to the rule file
	 */
	private void createSaveButton(Composite viewArea) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setText("Save");
		
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
		BundleContext context = Activator.getContext();
		
		//Create table and prepare cells
		createTable(viewArea);
		createSaveButton(viewArea);
		
		ServiceReference<JavaEditorServices> serviceReference2 = context.getServiceReference(JavaEditorServices.class);
		this.javaServ = context.getService(serviceReference2);
		File f = this.javaServ.getOpenedFile();

		ConventionVisitor checker = new ConventionVisitor(rules, javaServ);
		Parser.parse(/*testView.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/TestExampleConventions.java"*/
				this.javaServ.getOpenedFile(), checker);

		
		for(Log l: checker.getErrors()) {
			this.javaServ.addAnnotation(f, AnnotationType.WARNING, l.getMessage(), l.getPosition()
					, l.getValue().length());
		}
		
		/*ServiceReference<JavaEditorListener> serviceListenerReferene = context.getServiceReference(JavaEditorListener.class);
		this.javaListener = context.getService(serviceListenerReferene);
		this.javaListener.fileSaved(this.javaServ.getOpenedFile());*/
		
		/*Button button = new Button(viewArea, SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			File f = javaServ.getOpenedFile();
			if (f!= null) {
				ITextSelection sel = javaServ.getTextSelected(f);
				javaServ.addAnnotation(f, AnnotationType.WARNING, "cenas ecoisas", sel.getOffset(), sel.getLength());
				new Label(viewArea, SWT.NONE).setText(sel.getText());
				viewArea.layout();
			}
		}
	})*/;
		
		/*
		Label label = new Label(viewArea, SWT.NONE);
		label.setImage(imageMap.get("smiley.png"));
		Text text = new Text(viewArea, SWT.BORDER);
		text.setText("hello world");*/
	}

}
