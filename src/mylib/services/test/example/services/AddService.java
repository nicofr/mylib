package mylib.services.test.example.services;

import java.util.List;

import mylib.services.ExportsTerminalService;
import mylib.services.TerminalDispatcher;
import mylib.services.annotations.ExportParam;
import mylib.services.components.standardmappers.StringToIntegerMapper;
import mylib.services.exceptions.ServiceException;

public class AddService implements ExportsTerminalService {
	
	static {
		// uncomment to test
//		ExportsTerminalService.register(AddService.class);
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
