package mylib.services.test.example;

import mylib.services.ServiceDispatcher;
import mylib.services.exceptions.ServiceException;
import mylib.services.test.example.services.AddService;
import mylib.services.test.example.services.StoreIntegerService;

public class Main {
	
	public static void main(String[] args) throws ServiceException {
		System.out.println(ServiceDispatcher.executeService("help"));
	}
}
