package com.romajs;

import com.romajs.tx.TransactionContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractTest {

	protected static EmbddedJettyServer server;
	protected static EntityManagerFactory emFactory;
	protected EntityManager em;

	@BeforeClass
	public static void setUpClass() throws Exception {
		server = new EmbddedJettyServer(8081, "/", "src/main/webapp", null);
		server.start();
		emFactory = Persistence.createEntityManagerFactory("mvn-web-2017-pu");
	}

	@Before
	public void setUp() {
		TransactionContext.config(emFactory);
		TransactionContext.beginTransaction();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		TransactionContext.terminate();
		server.stop();
		server = null;
		emFactory = null;
	}

	@After
	public void tearDown() {
		TransactionContext.commitTransaction();
		TransactionContext.close();
		em = null;
	}

}
