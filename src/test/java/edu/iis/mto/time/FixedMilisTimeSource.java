package edu.iis.mto.time;

public class FixedMilisTimeSource implements TimeSource {

	long fixedMilis;

	public FixedMilisTimeSource(long fixedMilis) {
		this.fixedMilis = fixedMilis;
	}

	@Override
	public long currentTimeMillis() {
		return fixedMilis;
	}

}
