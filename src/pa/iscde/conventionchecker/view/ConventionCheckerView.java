package pa.iscde.conventionchecker.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.framework.BundleContext;

import pa.iscde.conventionchecker.core.ConventionActivator;
import pa.iscde.conventionchecker.core.ConventionCheckerServiceImpl;
import pa.iscde.conventionchecker.visitor.Parser;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
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
	private void createSaveButton(ToolBar viewArea, Map<String, Image> imageMap) {
		ToolItem button = new ToolItem(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("Save.png"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				conventionService.getRuleManager().saveFile();
				conventionTable.hideEditor();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	/**
	 * Create a combo box for the profiles
	 * 
	 * @param viewArea 
	 */
	private void createComboBox(ToolBar viewArea) {
		// Label
		ToolItem label = new ToolItem(viewArea, SWT.READ_ONLY);
        label.setText("Profile:");
        label.setEnabled(false);
        
	    ToolItem comboTool = new ToolItem(viewArea, SWT.SEPARATOR);

		Combo combo = new Combo(viewArea, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] items = conventionService.getProfiles();
		combo.setItems(items);
		combo.select(0);
		
		combo.pack();
		comboTool.setWidth(combo.getSize().x);
		comboTool.setControl(combo);
		
		 // User select a item in the Combo.
        combo.addSelectionListener(new SelectionListener() {
 
            @Override
            public void widgetSelected(SelectionEvent e) {
                int idx = combo.getSelectionIndex();
                String profile = combo.getItem(idx);
                conventionService.changeProfile(profile);
                conventionTable.refreshTable();
                conventionTable.hideEditor();
            }

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
	}
	
	/**
	 * Create Refresh button on my view to parse the files again
	 * 
	 * @param viewArea 
	 * @param imageMap
	 */
	private void createRefreshButton(ToolBar viewArea, Map<String, Image> imageMap) {
		ToolItem button = new ToolItem(viewArea, SWT.PUSH);
		button.setImage(imageMap.get("refresh.gif"));
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				conventionService.resetStack();
				Parser.parseAll(ConventionActivator.getJavaBrowserService().getRootPackage().getChildren(), conventionService.getVisitor());
				conventionService.annotateCurrentFile();
				conventionTable.hideEditor();
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
		// Avoid components being separated
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
		viewArea.setLayout(gridLayout);
		BundleContext context = ConventionActivator.getContext();
		
		// Tool bar are used to stick 2 components together
		ToolBar buttonToolbar = new ToolBar(viewArea, SWT.NONE);
		createSaveButton(buttonToolbar, imageMap);
		createRefreshButton(buttonToolbar, imageMap);
		
		createTable(viewArea);
		
		ToolBar comboToolbar = new ToolBar(viewArea, SWT.NONE);
		createComboBox(comboToolbar);
		
		
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
			Map<String, String> rows = new HashMap();
			String profileName = e.getAttribute("profileName");
			rows.put("Variable", e.getAttribute("variable"));
			rows.put("Constant", e.getAttribute("constant"));
			rows.put("Class", e.getAttribute("class"));
			rows.put("Method", e.getAttribute("method"));
			rows.put("Interface", e.getAttribute("interface"));
			rows.put("Parameter", e.getAttribute("parameter"));
			
			// I don't want to override the same profiles that were loaded (and possibly changed) before
			if (!conventionService.getRuleManager().profileExists(profileName)) {
				conventionService.addNewProfile(profileName, rows);
			}

		}
	}

}
