package org.mackenzine.notifications.model;

public class Schedule {

	private Long delay;
	private Long period;
	private Integer repeat;
	private String cronEntry;

	public Long getDelay() {
		return delay;
	}

	public Long getPeriod() {
		return period;
	}

	public Integer getRepeat() {
		return repeat;
	}

	public String getCronEntry() {
		return cronEntry;
	}

	public void setDelay(final Long delay) {
		this.delay = delay;
	}

	public void setPeriod(final Long period) {
		this.period = period;
	}

	public void setRepeat(final Integer repeat) {
		this.repeat = repeat;
	}

	public void setCronEntry(final String cronEntry) {
		this.cronEntry = cronEntry;
	}
}
