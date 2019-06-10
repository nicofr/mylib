package mylib.services.test.example.services;

import mylib.services.ExportsTerminalService;
import mylib.services.annotations.ExportParam;
import mylib.services.components.standardmappers.StringToBooleanMapper;

public class StoreIntegerService implements ExportsTerminalService {
	
	static {
		// uncomment to test
		//ExportsService.register(StoreIntegerService.class);
	}
	
	public static int setValue = 0;
	
	@ExportParam(Ident = "neg", MapperClass = StringToBooleanMapper.class)
	private boolean neg;
	
	@ExportParam(Ident = "value", MapperClass = mylib.services.components.standardmappers.StringToIntegerMapper.class)
	private Integer value;

	@Override
	public String performService() {
		setValue = neg ? value * -1 : value;
		System.out.println(setValue);
		return "";
	}

	@Override
	public String getServiceName() {
		return "store";
	}

}
