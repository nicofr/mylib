package mylib.services;

import mylib.services.exceptions.ServiceException;

public interface ExportsService {
	
	public String performService();
	public String getServiceName();
	
	public default String getHelpText() {
		return "";
	}
	
	static void register(Class<? extends ExportsService> service) {
		try {
			ServiceDispatcher.registerService(service);
		} catch (ServiceException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
