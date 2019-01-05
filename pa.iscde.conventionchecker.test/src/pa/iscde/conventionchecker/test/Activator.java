package pa.iscde.conventionchecker.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.conventionchecker.service.ConventionCheckerService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ConventionCheckerService test;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// get the service
		ServiceReference<ConventionCheckerService> serviceReference = bundleContext.getServiceReference(ConventionCheckerService.class);
		test = bundleContext.getService(serviceReference);

		listener l = new listener();
		test.addListener(l);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public static ConventionCheckerService getConvetionSerice() {
		return test;
	}

}
