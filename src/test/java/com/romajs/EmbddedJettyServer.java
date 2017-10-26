package com.romajs;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class EmbddedJettyServer {

	static final String FRAGMENT_CONFIG_CLASS = "org.eclipse.jetty.webapp.FragmentConfiguration";
	static final String ENV_CONFIG_CLASS = "org.eclipse.jetty.plus.webapp.EnvConfiguration";
	static final String PLUS_CONFIG_CLASS = "org.eclipse.jetty.plus.webapp.PlusConfiguration";

	static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

	protected final int httpPort;
	protected final Server server;

	public EmbddedJettyServer(int httpPort, String contextPath, String resourceBase, String overrideDescriptor) {

		server = new Server(this.httpPort = httpPort);
		server.setStopAtShutdown(true);

		// https://www.eclipse.org/jetty/documentation/9.3.x/jndi-embedded.html
		Configuration.ClassList.setServerDefault(server).addAfter(FRAGMENT_CONFIG_CLASS,
				new String[]{ENV_CONFIG_CLASS, PLUS_CONFIG_CLASS});

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setErrorHandler(new ErrorPageErrorHandler());
		webAppContext.setClassLoader(ClassLoader.getSystemClassLoader());
		webAppContext.setContextPath(contextPath);
		webAppContext.setResourceBase(resourceBase);
		webAppContext.setOverrideDescriptor(overrideDescriptor);
		webAppContext.setTempDirectory(TEMP_DIRECTORY);

		server.setHandler(webAppContext);
	}

	public void start() throws Exception {
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}
}
