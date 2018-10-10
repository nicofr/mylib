package mylib.services.test.example.services;

import mylib.services.Service;

public class GetValueService extends Service {

	public GetValueService() {
		super("get");
	}

	@Override
	public void perform() {
		System.out.println(StoreIntegerService.setValue);
	}

}
