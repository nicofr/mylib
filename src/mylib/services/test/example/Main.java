package mylib.services.test.example;

import mylib.services.ServiceDispatcher;
import mylib.services.test.example.services.StoreIntegerService;

public class Main {
	
	public static void main(String[] args) {
		ServiceDispatcher.registerService(StoreIntegerService.class);
		
	}

}
