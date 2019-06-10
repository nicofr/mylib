package mylib.services.test.example;

import mylib.services.TerminalDispatcher;
import mylib.services.exceptions.ServiceException;
import mylib.services.test.example.services.AddService;
import mylib.services.test.example.services.StoreIntegerService;

public class Main {
	
	public static void main(String[] args) throws ServiceException {
		System.out.println(TerminalDispatcher.executeService("help"));
	}
}
