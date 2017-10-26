package com.romajs;

import com.romajs.tx.TransactionContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractTest {

	protected static final int HTTP_PORT = 8081;

	protected static ThreadLocal<Server> localServer = new ThreadLocal<>();

	protected EntityManager em;
	private static EntityManagerFactory emFactory;

	@BeforeClass
	public static void setUpClass() throws Exception {

		Server server = localServer.get();

		if (server != null) {
			return;
		}

		server = new Server(HTTP_PORT);
		server.setStopAtShutdown(true);

		// https://www.eclipse.org/jetty/documentation/9.3.x/jndi-embedded.html
		ClassList.setServerDefault(server).addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setErrorHandler(new ErrorPageErrorHandler());
		webAppContext.setContextPath("/");
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setClassLoader(ClassLoader.getSystemClassLoader());
//		webAppContext.setOverrideDescriptor("src/test/resources/jetty-override-web.xml");
		server.setHandler(webAppContext);

//		new XmlConfiguration(new File("src/test/resources/jetty.xml").toURI().toURL()).configure(webAppContext);

//		JDBCDataSource ds = new JDBCDataSource();
//		ds.setDatabaseName("jdbc:hsqldb:mem:db;sql.syntax_pgs=true;allow_empty_batch=true;");
//		ds.setUser("SA");
//		ds.setPassword("SA");

		server.start();
		localServer.set(server);

		emFactory = Persistence.createEntityManagerFactory("my-app-pu");
	}

	@Before
	public void setUp() {
		TransactionContext.config(emFactory);
		TransactionContext.beginTransaction();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		TransactionContext.terminate();
//		localServer.get().stop();
//		localServer.remove();
	}

	@After
	public void tearDown() {
		TransactionContext.commitTransaction();
		TransactionContext.close();
		em = null;
	}

}
