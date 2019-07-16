package mylib.services;

import mylib.services.exceptions.ServiceException;

public class TerminalApplication {
	
	public static void exportService(Class<? extends ExportsTerminalService> service) {
		try {
			TerminalDispatcher.registerService(service);
		} catch (ServiceException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void exportServices(Class<? extends ExportsTerminalService>[] services) {
		for (Class<? extends ExportsTerminalService> service: services) {
			exportService(service);
		}
	}

}
