package com.romajs.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionContext {

	private static EntityManagerFactory emFactory;

	public static class InternalContext {

		Stack<EntityManager> ems = new Stack<EntityManager>();

		public EntityManager getEntityManager() {
			return ems.peek();
		}

		public void beginTransaction() {
			logger.trace("Starting transaction...");
			EntityManager em = emFactory.createEntityManager();

			em.getTransaction().begin();
			ems.push(em);

			logger.trace("Started transaction");
		}

		public void commitTransaction() {
			logger.trace("Commiting transaction...");

			EntityManager em = getEntityManager();
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}

			logger.trace("Transaction commited");

			closeEntityManager();
		}

		public void rollbackTransaction() {
			logger.trace("Rolling transaction back...");

			EntityManager em = getEntityManager();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			logger.trace("Transaction rolled back");

			closeEntityManager();
		}

		private void closeEntityManager() {
			EntityManager em = ems.pop();
			if (em == null || !em.isOpen()) {
				logger.warn("TransactionContext already closed");
			} else {
				logger.trace("Closing TransactionContext...");
				try {
					em.close();
				} catch (RuntimeException e) {
					logger.trace("TransactionalContext closed! " + e.getLocalizedMessage());
				}
			}
		}

		private void close() {
			if (!ems.isEmpty()) {
				logger.warn("TransactionalContext may have transaction leak! Freeing resources for safety...");
				do {
					closeEntityManager();
				} while (!ems.isEmpty());
			}
		}

	}

	static final ThreadLocal<InternalContext> transContext = new ThreadLocal<InternalContext>();

	private static final Logger logger = LoggerFactory.getLogger(TransactionContext.class.getName());

	public static EntityManager getEntityManager() {
		return transContext.get().getEntityManager();
	}

	public static void config(EntityManagerFactory emFactory) {
		TransactionContext.emFactory = emFactory;
		transContext.set(new InternalContext());
	}

	public static void close() {
		transContext.get().close();
		transContext.remove();
	}

	public static void terminate() {
		if(isOpen()) {
			close();
		}
		if (isActive()) {
			emFactory.close();
		}
	}

	public static void beginTransaction() {
		transContext.get().beginTransaction();
	}

	public static void commitTransaction() {
		transContext.get().commitTransaction();
	}

	public static void rollbackTransaction() {
		transContext.get().rollbackTransaction();
	}

	public static boolean isActive() {
		return emFactory != null;
	}

	public static boolean isOpen() {
		return transContext.get() != null;
	}

	public static void exec(Consumer<EntityManager> consumer) {
        beginTransaction();
        try {
			consumer.accept(getEntityManager());
			commitTransaction();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			rollbackTransaction();
		}
	}

}
