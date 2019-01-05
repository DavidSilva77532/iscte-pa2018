package pa.iscde.conventionchecker.test;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pt.iscte.pidesco.extensibility.PidescoView;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;



public class testView implements PidescoView {
	
	ConventionCheckerService test;
	
	public testView() {}
	
	/**
	 * Create Refresh button on my view to parse the files again
	 * 
	 * @param viewArea 
	 * @param imageMap
	 */
	private void createRefreshButton(Composite viewArea, Map<String, Image> imageMap) {
		Button button = new Button(viewArea, SWT.PUSH);
		button.setText("Test");
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// loop through the convention errors	
				System.out.println("Total errors: " + test.getConventionErrors().size());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});
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
		
		createRefreshButton(viewArea, imageMap);
		
		// get the service
		test = Activator.getConvetionSerice();
			
		
	}
	
	

}
