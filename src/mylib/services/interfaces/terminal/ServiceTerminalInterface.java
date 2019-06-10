package mylib.services.interfaces.terminal;

import mylib.services.TerminalDispatcher;
import mylib.services.exceptions.ServiceException;

public class ServiceTerminalInterface {
	
	public static void execute(String[] args) throws ServiceException {
		System.out.println(TerminalDispatcher.executeService(args));
	}

}
