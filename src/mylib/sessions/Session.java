package mylib.sessions;

import mylib.util.HasId;

public abstract class Session implements HasId {
	
	private final int uid;
	
	Session(int uid) {
		this.uid = uid;
	}

	@Override
	public int getId() {
		return uid;
	}
	
	@Override
	public int hashCode() {
		return uid;
	}

}
