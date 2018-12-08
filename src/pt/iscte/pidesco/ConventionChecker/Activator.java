package pt.iscte.pidesco.conventionchecker;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import pt.iscte.pidesco.conventionchecker.service.ConventionCheckerService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private ConventionCheckerService service;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		service = new ConventionCheckerServiceImpl();
		context.registerService(ConventionCheckerService.class, service, null);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
