package mylib.services.test.example.services;

import java.util.List;

import mylib.services.ExportsService;
import mylib.services.ServiceDispatcher;
import mylib.services.annotations.ExportParam;
import mylib.services.components.standardmappers.StringToIntegerMapper;
import mylib.services.exceptions.ServiceException;

public class AddService implements ExportsService {
	
	static {
		// uncomment to test
		//ExportsService.register(AddService.class);
	}
	
	@ExportParam(Ident = "args", MapperClass=StringToIntegerMapper.class)
	private List<Integer> args;
	

	@Override
	public String performService() {
		System.out.println(args.size());
		return null;
	}

	@Override
	public String getServiceName() {
		return "add";
	}

}
