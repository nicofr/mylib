package mylib.sessions;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SessionPool {
	
	private final Map<Integer, Session> sessions;
	
	private static final SessionPool instance = new SessionPool();
	
	private static int curUid = 0;
	
	private SessionPool() {
		sessions = new HashMap<>();
	}
	
	public static <SES_TYPE extends Session> SES_TYPE newSession(Class<SES_TYPE> sessionType) {
		try {
			SES_TYPE session = sessionType.getConstructor(int.class).newInstance(curUid);
			instance.sessions.put(curUid, session);
			curUid++;
			return session;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Session getSessionById(int id) {
		return instance.sessions.get(id);
	}
	
}
