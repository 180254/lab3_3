/*

Copyright (C) 2013 Xavier HANIN

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Original URL: https://github.com/restx/restx/blob/master/restx-common/src/main/java/restx/common/ThreadLocalMillisProvider.java

 */

package restx.common;

import static com.google.common.base.Preconditions.checkNotNull;

import org.joda.time.DateTimeUtils;

/**
 * A MillisProvider which delegates to other providers with a per thread
 * setting.
 *
 * It is generally used as a replacement for JodaTime millis provider which are
 * classloader wide, and thus not compatible with parallel tests.
 *
 * Note that calling any of the set method will install a
 * ThreadLocalMillisProvider as DateTimeUtils millis provider.
 */
public class ThreadLocalMillisProvider implements DateTimeUtils.MillisProvider {
	private static final DateTimeUtils.MillisProvider systemMillisProvider = new DateTimeUtils.MillisProvider() {
		@Override
		public long getMillis() {
			return System.currentTimeMillis();
		}
	};

	private static final ThreadLocal<DateTimeUtils.MillisProvider> local = new ThreadLocal<DateTimeUtils.MillisProvider>() {
		@Override
		protected DateTimeUtils.MillisProvider initialValue() {
			return systemMillisProvider;
		}
	};

	private static final DateTimeUtils.MillisProvider INSTANCE = new ThreadLocalMillisProvider();

	public static final void setCurrentMillisSystem() throws SecurityException {
		install();
		local.set(systemMillisProvider);
	}

	public static final void setCurrentMillisFixed(long fixedMillis) throws SecurityException {
		install();
		local.set(new FixedMillisProvider(fixedMillis));
	}

	public static final void setCurrentMillisOffset(long offsetMillis) throws SecurityException {
		install();
		local.set(new OffsetMillisProvider(offsetMillis));
	}

	public static final void setCurrentMillisProvider(DateTimeUtils.MillisProvider millisProvider)
			throws SecurityException {
		install();
		local.set(checkNotNull(millisProvider));
	}

	public static void clear() {
		local.remove();
	}

	public static DateTimeUtils.MillisProvider current() {
		return local.get();
	}

	private static void install() {
		DateTimeUtils.setCurrentMillisProvider(INSTANCE);
	}

	private ThreadLocalMillisProvider() {

	}

	@Override
	public long getMillis() {
		return local.get().getMillis();
	}

	/**
	 * Fixed millisecond provider.
	 */
	public static class FixedMillisProvider implements DateTimeUtils.MillisProvider {
		/** The fixed millis value. */
		private final long iMillis;

		/**
		 * Constructor.
		 * 
		 * @param fixedMillis
		 *            the millis offset
		 */
		public FixedMillisProvider(long fixedMillis) {
			iMillis = fixedMillis;
		}

		/**
		 * Gets the current time.
		 * 
		 * @return the current time in millis
		 */
		public long getMillis() {
			return iMillis;
		}
	}

	/**
	 * Offset from system millis provider.
	 */
	public static class OffsetMillisProvider implements DateTimeUtils.MillisProvider {
		/** The millis offset. */
		private final long iMillis;

		/**
		 * Constructor.
		 * 
		 * @param offsetMillis
		 *            the millis offset
		 */
		public OffsetMillisProvider(long offsetMillis) {
			iMillis = offsetMillis;
		}

		/**
		 * Gets the current time.
		 * 
		 * @return the current time in millis
		 */
		public long getMillis() {
			return System.currentTimeMillis() + iMillis;
		}
	}
}
