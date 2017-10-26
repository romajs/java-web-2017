package com.romajs.tx;

import com.romajs.AbstractTest;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionContextTest extends AbstractTest {

	@Test
	public void shouldHaveOneTxInStack() {
		assertNotNull(TransactionContext.getEntityManager());
		assertNotNull(TransactionContext.transContext.get());
		assertEquals(1, TransactionContext.transContext.get().ems.size());
		assertTrue(TransactionContext.isActive());
		assertTrue(TransactionContext.isOpen());
	}

	@Test
	public void shouldStackMoreTxInStack() {
		assertEquals(1, TransactionContext.transContext.get().ems.size());
		TransactionContext.beginTransaction();
		assertEquals(2, TransactionContext.transContext.get().ems.size());
		TransactionContext.beginTransaction();
		assertEquals(3, TransactionContext.transContext.get().ems.size());
		TransactionContext.rollbackTransaction();
		assertEquals(2, TransactionContext.transContext.get().ems.size());
		TransactionContext.commitTransaction();
		assertEquals(1, TransactionContext.transContext.get().ems.size());
	}

	@Test
	public void shouldExecTx() {
		assertEquals(1, TransactionContext.transContext.get().ems.size());
		TransactionContext.exec(em -> {
			assertEquals(em, TransactionContext.getEntityManager());
			assertEquals(2, TransactionContext.transContext.get().ems.size());
		});
		assertEquals(1, TransactionContext.transContext.get().ems.size());
	}

	@Test
	public void shouldFailTx() {
		assertEquals(1, TransactionContext.transContext.get().ems.size());
		TransactionContext.exec(em -> {
			assertEquals(em, TransactionContext.getEntityManager());
			assertEquals(2, TransactionContext.transContext.get().ems.size());
			throw new RuntimeException();
		});
		assertEquals(1, TransactionContext.transContext.get().ems.size());
	}
}
