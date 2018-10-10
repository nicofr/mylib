package mylib.sessions;

public abstract class SessionAction<SES_TYPE extends Session, RE_TYPE> {
	
	private final SES_TYPE session;
	
	public SessionAction(SES_TYPE session) {
		this.session = session;
	}
	
	protected final SES_TYPE getSession() {
		return session;
	}
	
	public abstract void perform();

}
