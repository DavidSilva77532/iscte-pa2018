package pa.iscde.conventionchecker.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.conventionchecker.service.ConventionCheckerListener;
import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pa.iscde.conventionchecker.service.LogExt;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ConventionActivator implements BundleActivator {

	private static ConventionActivator instance;
	
	private static BundleContext context;
	private static ConventionCheckerService conventionService;
	private static JavaEditorServices editorServices;
	private static ProjectBrowserServices browserServices;
	private Set<ConventionCheckerListener> listeners = new HashSet<>();

	/**
	 * Get Activator class since its working as Singleton.
	 * The activator has all references to other services
	 * @return activator instance
	 */
	public static ConventionActivator getInstance() {
		return instance;
	}
	
	/**
	 * @return context
	 */
	public static BundleContext getContext() {
		return context;
	}

	/**
	 * Starts all the services
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		ConventionActivator.context = bundleContext;
		registerConventionService();
		initJavaEditorService();
		initJavaBrowserService();
		
	}

	/**
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ConventionActivator.context = null;
		instance = null;
	}
	
	/**
	 * Starts the Java Editor Service
	 */
	private void initJavaEditorService() {
		ServiceReference<JavaEditorServices> serviceReference2 = context.getServiceReference(JavaEditorServices.class);
		editorServices = context.getService(serviceReference2);
	}
	
	/**
	 * Starts the Java Browser Service
	 */
	private void initJavaBrowserService() {
		ServiceReference<ProjectBrowserServices> serviceReference2 = context.getServiceReference(ProjectBrowserServices.class);
		browserServices = context.getService(serviceReference2);
	}
	
	/**
	 * Registers my convention service
	 */
	private void registerConventionService () {
		conventionService = new ConventionCheckerServiceImpl();
		context.registerService(ConventionCheckerService.class, conventionService, null);		
	}
	
	public static ConventionCheckerService getConventionService() {
		return conventionService;
	}
	
	/**
	 * @return the Java Editor Service
	 */
	public static JavaEditorServices getJavaEditorService() {
		return editorServices;
	}
	
	/**
	 * @return the Java Browser Service
	 */
	public static ProjectBrowserServices getJavaBrowserService() {
		return browserServices;
	}
	
	/**
	 * Adds a new listener to the convention checker service
	 * @param listener to be added
	 */
	public void addListener(ConventionCheckerListener listener) {
		listeners.add(listener);	
	}
	
	/**
	 * Removes an existing listener from the convention checker service
	 * @param listener to be removed
	 */
	public void removeListener(ConventionCheckerListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Notify's all listeners with the current convention errors
	 * @param conventionErrors that were found in the last run
	 */
	public void notifyListeners(ArrayList<LogExt> conventionErrors) {
		for(ConventionCheckerListener l : listeners)
			l.checkingFinished(conventionErrors);
	}

}
