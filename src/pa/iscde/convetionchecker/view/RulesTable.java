package pa.iscde.convetionchecker.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import pa.iscde.conventionchecker.core.ConventionRules;

public class RulesTable {
	private ConventionRules rules;
	private Composite view;
	private String[] headers = { "Type", "Regex" };
	
	public RulesTable (ConventionRules p_rules,
					Composite p_view) {
		rules = p_rules;
		view = p_view;
		createTable();
	}
	
	/**
	 * Create the header record
	 * @param table
	 */
	public void createHeader(Table table) {
		// Prepare the header
        for (int i = 0; i < headers.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(headers[i]);
            table.getColumn(i).setWidth(100);
        }
	}
	
	/**
	 * Update cells with the content from our file
	 */
	public void updateCellsByFile(Table table) {
		Map<String, String> myRules = rules.getRules();
		
		// Populate the cells with our rules loaded previously from the file
        for ( String key : myRules.keySet() ) {
        	TableItem item = new TableItem(table, SWT.NONE);
            item.setText (0, key);
            item.setText (1, myRules.get(key));
        }
	}
	
	/**
	 * Create editable cells
	 * @param table
	 */
	public void createEditCells(Table table) {
		//create editable cells
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        
        // only the second should be editable (the rule)
        final int EDITABLECOLUMN = 1;

        table.addSelectionListener(new SelectionAdapter() {
          
        	public void widgetSelected(SelectionEvent e) {
            // Clean up any previous editor control
            Control oldEditor = editor.getEditor();
            if (oldEditor != null)
              oldEditor.dispose();

            // Identify the selected row
            TableItem item = (TableItem) e.item;
            if (item == null)
              return;

            // The control that will be the editor must be a child of the Table
            Text newEditor = new Text(table, SWT.NONE);
            newEditor.setText(item.getText(EDITABLECOLUMN));
            newEditor.addModifyListener(new ModifyListener() {
            	public void modifyText(ModifyEvent me) {
            		Text text = (Text) editor.getEditor();
            		editor.getItem().setText(EDITABLECOLUMN, text.getText());
            		rules.updateRule(item.getText(0), item.getText(1));
            	}
            });
           
            newEditor.selectAll();
            newEditor.setFocus();
            editor.setEditor(newEditor, item, EDITABLECOLUMN);
          }
        });
	}
	
	/**
	 * Create table and prepare all listeners. Also populate the initial table with the rules file.
	 * @param p_view
	 */
	public void createTable() {
		
		// Set grid layouts and global table definitions
		Table table = new Table(view, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(false);
        table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 200;
        view.setLayoutData(data);
        
        createHeader(table);
        updateCellsByFile(table);
        createEditCells(table);   
	}
	
}
