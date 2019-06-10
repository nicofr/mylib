package mylib.services;

import mylib.services.exceptions.ServiceException;

public interface ExportsTerminalService {
	
	public String performService();
	public String getServiceName();
	
	public default String getHelpText() {
		return "";
	}
	
	static void register(Class<? extends ExportsTerminalService> service) {
		try {
			TerminalDispatcher.registerService(service);
		} catch (ServiceException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
