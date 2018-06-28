package mylib.util;

public class TimestampedValue<VAL> implements CSVable {
	
	private final VAL value;	
	private final long timestamp_ms;
	
	public TimestampedValue(VAL value, long timestamp_ms) {
		this.value = value;
		this.timestamp_ms = timestamp_ms;
	}

	/**
	 * @return the value
	 */
	public VAL getValue() {
		return value;
	}

	/**
	 * @return the timestamp_ms
	 */
	public long getTimestamp_ms() {
		return timestamp_ms;
	}

	@Override
	public String toCSV() {
		return timestamp_ms + ";"+value.toString();
	}

}
