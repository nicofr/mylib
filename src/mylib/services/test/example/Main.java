package mylib.services.test.example;

import mylib.services.ExportsTerminalService;
import mylib.services.TerminalDispatcher;
import mylib.services.exceptions.ServiceException;
import mylib.services.test.example.services.StoreIntegerService;

public class Main {
	
	public static void main(String[] args) throws ServiceException {
		ExportsTerminalService.register(StoreIntegerService.class);
		TerminalDispatcher.interactive();
		while (TerminalDispatcher.running()) {
			System.out.println(TerminalDispatcher.executeService(System.console().readLine()));
		}
	}
}
