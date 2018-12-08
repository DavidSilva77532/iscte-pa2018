package pa.iscde.conventionchecker.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ConventionActivator implements BundleActivator {

	private static ConventionActivator instance;
	
	private static BundleContext context;
	private ConventionCheckerService conventionService;
	private static JavaEditorServices editorServices;
	private static ProjectBrowserServices browserServices;

	public static ConventionActivator getInstance() {
		return instance;
	}
	
	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		ConventionActivator.context = bundleContext;
		registerConvetionService();
		initJavaEditorService();
		initJavaBrowserService();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ConventionActivator.context = null;
		instance = null;
	}
	
	private void initJavaEditorService() {
		ServiceReference<JavaEditorServices> serviceReference2 = context.getServiceReference(JavaEditorServices.class);
		editorServices = context.getService(serviceReference2);
	}
	
	private void initJavaBrowserService() {
		ServiceReference<ProjectBrowserServices> serviceReference2 = context.getServiceReference(ProjectBrowserServices.class);
		browserServices = context.getService(serviceReference2);
	}
	
	
	private void registerConvetionService () {
		conventionService = new ConventionCheckerServiceImpl();
		context.registerService(ConventionCheckerService.class, conventionService, null);
				
	}
	
	public static JavaEditorServices getJavaEditorService() {
		return editorServices;
	}
	
	public static ProjectBrowserServices getJavaBrowserService() {
		return browserServices;
	}

}
