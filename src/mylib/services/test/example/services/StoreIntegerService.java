package mylib.services.test.example.services;

import mylib.services.Service;
import mylib.services.annotations.Flag;
import mylib.services.annotations.Param;

public class StoreIntegerService extends Service {
	
	public static int setValue = 0;
	
	@Flag(Name = "n")
	boolean negate;
	
	@Param(Ident = "value", MapperClassName = "mylib.services.components.standardmappers.StringToIntegerMapper")
	private Integer value;

	public StoreIntegerService() {
		super("store");
	}

	@Override
	public void perform() {
		setValue = negate ? value * -1 : value;
	}

}
