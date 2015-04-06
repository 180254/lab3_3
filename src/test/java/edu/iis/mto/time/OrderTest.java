package edu.iis.mto.time;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import restx.common.ThreadLocalMillisProvider;

public class OrderTest {

	Order order;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		order = new Order();
	}

	@After
	public void tearDown() throws Exception {
		ThreadLocalMillisProvider.setCurrentMillisSystem();
	}

	@Test(expected = OrderExpiredException.class)
	public final void test_OrderIsExpired_shouldThrowOrderExpiredException() {
		order.submit();

		long expiredMilis = new DateTime()
				.plusHours(Order.VALID_PERIOD_HOURS + 1)
				.toInstant().getMillis();

		ThreadLocalMillisProvider.setCurrentMillisFixed(expiredMilis);

		order.confirm();
	}

}
