package edu.iis.mto.time;

public final class DefaultTimeSource implements TimeSource {
	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}
}