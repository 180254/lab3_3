package edu.iis.mto.time;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
	}

	@Test(expected = OrderExpiredException.class)
	public final void test_OrderIsExpired_shouldThrowOrderExpiredException() {
		final int MILIS_IN_HOUR = 60 * 60 * 1000;

		order.setTimeSource(new TimeSource() {

			@Override
			public long currentTimeMillis() {
				return 0;
			}
		});
		order.submit();

		order.setTimeSource(new TimeSource() {

			@Override
			public long currentTimeMillis() {
				return (Order.VALID_PERIOD_HOURS + 1) * MILIS_IN_HOUR;
			}
		});
		order.confirm();
	}

}
