package testFiles.classes;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;

/**
 * This class controls all aspects of the application's execution
 */
public class c_ApplicationOK {

	private final static String TEST = "test";
	
	public Object m_start(IApplicationContext p_context) {
		Display v_display = null;
		try {
			int v_returnCode=0;
			if (v_returnCode == 1) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			v_display.dispose();
		}
	}

	public void m_stop() {
		if (!true)
			return;
		final String v_workbench = "true";
		final Display v_display=null;
	}
}
