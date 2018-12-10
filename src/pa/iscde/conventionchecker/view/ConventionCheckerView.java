package pa.iscde.conventionchecker.view;

import java.io.File;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;

import pa.iscde.conventionchecker.core.ConventionActivator;
import pa.iscde.conventionchecker.core.ConventionCheckerServiceImpl;
import pa.iscde.conventionchecker.ext.ConventionCheckerExt;
import pa.iscde.conventionchecker.visitor.Parser;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;

public class ConventionCheckerView implements PidescoView {
	private RulesTable conventionTable;
	private ConventionCheckerServiceImpl conventionService;
	
	public ConventionCheckerView() {
		this.conventionService = (ConventionCheckerServiceImpl) ConventionActivator.getInstance().getConventionService();
		this.conventionService.setJavaEditor(ConventionActivator.getInstance().getJavaEditorService());
	}
	
	
	/**
	 * Create Save button on my view to save the grid to the rule file
	 * 
	 * @param viewArea 
	 * @param imageMap
	 */
	private void createSaveButton(Composite viewArea, Map<String, Image> imageMap) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("Save.png"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				conventionService.getRuleManager().saveFile();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	/**
	 * Create Refresh button on my view to parse the files again
	 * 
	 * @param viewArea 
	 * @param imageMap
	 */
	private void createRefreshButton(Composite viewArea, Map<String, Image> imageMap) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("refresh.gif"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				conventionService.resetStack();
				Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), conventionService.getVisitor());
				conventionService.annotateCurrentFile();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});
	}
	
	/**
	 * Create table and prepare all table listeners. 
	 * Also populate the initial table with the rules file.
	 * 
	 * @param p_view
	 */
	public void createTable(Composite p_view) {
		conventionTable = new RulesTable(conventionService.getRuleManager(), p_view);
	}
	
	/**
	 * Starts my convention checker components. 
	 * 
	 * @param viewArea
	 * @param imageMap
	 */
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		BundleContext context = ConventionActivator.getContext();
		
		createTable(viewArea);
		createSaveButton(viewArea, imageMap);
		createRefreshButton(viewArea, imageMap);
		
		Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), conventionService.getVisitor());
		conventionService.annotateCurrentFile();
				
		JavaEditorListener listener = new JavaEditorListener.Adapter() {
			@Override
			public void fileSaved(File file) {
				conventionService.resetStack(file.getAbsolutePath());
				Parser.parse(file, conventionService.getVisitor());
				conventionService.annotateCurrentFile();
			}
			
			@Override
			public void fileOpened(File file) {
				conventionService.resetStack(file.getAbsolutePath());
				Parser.parse(file, conventionService.getVisitor());
				conventionService.annotateCurrentFile(file);
			}
		};
		
		ConventionActivator.getInstance().getJavaEditorService().addListener(listener);
		triggerExtensions(viewArea);
	}
	
	/**
	 * Go through our extension points and execute methods for whatever plugin's that are connected
	 * 
	 * @param viewArea
	 */
	private void triggerExtensions(Composite viewArea) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pa.iscde.conventionchecker.ConventionCheckerExtension");
		for(IConfigurationElement e : elements) {
			try {
				ConventionCheckerExt action = (ConventionCheckerExt) e.createExecutableExtension("class");
				action.changeView(viewArea, conventionService.getRuleManager().getRules()); // allows to change the checker view and gives the rules
				//action.getErrors(conventionService.getConventionErrors()); // gives the convention errors
				viewArea.layout();
				conventionTable.refreshTable();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
	}

}
