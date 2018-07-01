package mylib.collections;

public class Pair<L,R> {
	
	private final L l;
	private final R r;

	public Pair(L l, R r) {
		this.l = l;
		this.r = r;
	}

	public L getL() {
		return l;
	}

	public R getR() {
		return r;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Pair<?,?>)) return false;
		
		Pair<?,?> other = (Pair<?,?>) obj;
		return l.equals(other.getL()) && r.equals(other.getR());
	}
	
	@Override
	public String toString() {
		return "("+l.toString()+", "+r.toString()+")";
	}

}
