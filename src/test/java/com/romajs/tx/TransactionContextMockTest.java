package com.romajs.tx;

import com.romajs.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

//@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest(TransactionContext.class)
public class TransactionContextMockTest extends AbstractTest {

	@Test
	public void shouldKeepOneTxAfterCrashAtSecondExec() throws Exception {
		assertEquals(1, TransactionContext.transContext.get().ems.size());

		PowerMockito.mockStatic(TransactionContext.class);
		PowerMockito.doThrow(new RuntimeException()).when(TransactionContext.class, "beginTransaction");

		try {
			TransactionContext.exec(em -> {
				fail();
			});
		} catch (Throwable t) {
			assertEquals(1, TransactionContext.transContext.get().ems.size());
		}
	}
}
