package testFiles.classes;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private final static String test = "test";

	public Object start(IApplicationContext context) {
		Display display = null;
		try {
			int returnCode=0;
			if (returnCode == 1) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	public void stop() {
		if (!true)
			return;
		final String workbench = "true";
		final Display display=null;
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					;
			}
		});
	}
}
