package mylib.services;

import mylib.services.annotations.ExportParam;
import mylib.services.util.ServiceUtils;

public class HelpService implements ExportsService {
	
	@ExportParam(Ident = "n", Optional = true, Help = "Name of service of which help text should be printed")
	private String targetname;

	@Override
	public String performService() {
		if (targetname != null) {
			if (ServiceDispatcher.getRegisteredServices().containsKey(targetname)) {
				ExportsService service = ServiceDispatcher.getRegisteredServices().get(targetname);
				return getServiceName() +": " + service.getHelpText() + ServiceUtils.printAllParamHelp(service);
			} else {
				return "No such servcie";
			}
		} else {
			String res = getServiceName() +": ";
			for (ExportsService service : ServiceDispatcher.getRegisteredServices().values()) {
				res = res + service.getHelpText() + ServiceUtils.printAllParamHelp(service) + System.lineSeparator();
			}
			return res;
		}
	}

	@Override
	public String getServiceName() {
		return "help";
	}
	
	 @Override
	public String getHelpText() {
		return "Prints help text of all services or specific service. Use with parameter -n <servicename> to print help text of specific service.";
	}

}
